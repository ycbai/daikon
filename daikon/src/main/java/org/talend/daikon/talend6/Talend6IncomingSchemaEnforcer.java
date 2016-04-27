// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.talend6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.avro.util.AvroUtils;

/**
 * <b>You should almost certainly not be using this class.</b>
 * 
 * This class acts as a wrapper around arbitrary values to coerce the Talend 6 Studio types in a generated POJO to a
 * {@link IndexedRecord} object that can be processed in the next component..
 * <p>
 * A wrapper like this should be attached before an output component, for example, to ensure that its incoming data with
 * the constraints imposed by the Studio meet the contract of the component framework, for example:
 * <ul>
 * <li>Coercing the types of the Talend POJO objects to expected Avro schema types.</li>
 * <li>Unwrapping data in a routines.system.Dynamic column into flat fields.</li>
 * </ul>
 * <p>
 * One instance of this object can be created per incoming schema and reused.
 */
public class Talend6IncomingSchemaEnforcer implements IndexedRecord, Talend6SchemaConstants {

    /**
     * The design-time schema from the Studio that determines how incoming java column data will be interpreted.
     */
    private final Schema incomingDesignTimeSchema;

    /**
     * The position of the dynamic column in the incoming schema. This is -1 if there is no dynamic column. There can be
     * a maximum of one dynamic column in the schema.
     */
    private final int incomingDynamicColumn;

    /**
     * The {@link Schema} of the actual runtime data that will be provided by this object. This will only be null if
     * dynamic columns exist, but they have not been finished initializing.
     */
    private Schema incomingRuntimeSchema;

    /** The fields constructed from dynamic columns. This will only be non-null during construction. */
    private List<Schema.Field> fieldsFromDynamicColumns = null;

    /** The values wrapped by this object. */
    private Object[] wrapped = null;

    /**
     * Access the indexed fields by their name. We should prefer accessing them by index for performance, but this
     * complicates the logic of dynamic columns quite a bit.
     */
    private final Map<String, Integer> columnToFieldIndex = new HashMap<>();

    public Talend6IncomingSchemaEnforcer(Schema incoming) {
        this.incomingDesignTimeSchema = incoming;
        this.incomingRuntimeSchema = incoming;

        // Find the dynamic column, if any.
        incomingDynamicColumn = AvroUtils.isIncludeAllFields(incoming)
                ? Integer.valueOf(incoming.getProp(Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION)) : -1;
        if (incomingDynamicColumn != -1) {
            incomingRuntimeSchema = null;
            fieldsFromDynamicColumns = new ArrayList<>();
        }

        // Add all of the runtime columns except any dynamic column to the index map.
        for (Schema.Field f : incoming.getFields()) {
            if (f.pos() != incomingDynamicColumn) {
                columnToFieldIndex.put(f.name(), f.pos());
            }
        }
    }

    /**
     * Take all of the parameters from the dynamic metadata and adapt it to a field for the runtime Schema.
     */
    public void initDynamicColumn(String name, String dbName, String type, String dbType, int dbTypeId, int length, int precision,
            String format, String description, boolean isKey, boolean isNullable, String refFieldName, String refModuleName) {
        if (!needsInitDynamicColumns())
            return;

        // Add each column to the field index and the incoming runtime schema.
        // TODO(rskraba): do something other than STRING!
        Schema fieldSchema = Schema.create(Schema.Type.STRING);
        if (isNullable) {
            fieldSchema = SchemaBuilder.nullable().type(fieldSchema);
        }
        fieldsFromDynamicColumns.add(new Schema.Field(name, fieldSchema, description, (Object) null));
    }

    /**
     * Called when dynamic columns have finished being initialized. After this call, the {@link #getSchema()} can be
     * used to get the runtime schema.
     */
    public void initDynamicColumnsFinished() {
        if (!needsInitDynamicColumns())
            return;

        // Copy all of the fields that were initialized from dynamic columns into the runtime Schema.
        List<Schema.Field> fields = new ArrayList<Schema.Field>();
        for (Schema.Field designField : incomingDesignTimeSchema.getFields()) {
            // Replace the dynamic column by all of its contents.
            if (designField.pos() == incomingDynamicColumn) {
                fields.addAll(fieldsFromDynamicColumns);
            } else {
                // Make a complete copy of the field (it can't be reused).
                Schema.Field designFieldCopy = new Schema.Field(designField.name(), designField.schema(), designField.doc(),
                        designField.defaultVal());
                for (Map.Entry<String, Object> e : designField.getObjectProps().entrySet()) {
                    designFieldCopy.addProp(e.getKey(), e.getValue());
                }
                fields.add(designFieldCopy);
            }
        }
        incomingRuntimeSchema = Schema.createRecord(incomingDesignTimeSchema.getName(), incomingDesignTimeSchema.getDoc(),
                incomingDesignTimeSchema.getNamespace(), incomingDesignTimeSchema.isError());
        incomingRuntimeSchema.setFields(fields);

        // Map all of the fields from the runtime Schema to their index.
        for (Schema.Field f : incomingRuntimeSchema.getFields()) {
            columnToFieldIndex.put(f.name(), f.pos());
        }

        // And indicate that initialization is finished.
        fieldsFromDynamicColumns = null;
    }

    /**
     * @return true only if there is a dynamic column and they haven't been finished initializing yet. When this returns
     * true, the enforcer can't be used yet and {@link #getSchema()} is guaranteed to return null.
     */
    public boolean needsInitDynamicColumns() {
        return fieldsFromDynamicColumns != null;
    }

    @Override
    public Schema getSchema() {
        return incomingRuntimeSchema;
    }

    public Schema getDesignSchema() {
        return incomingDesignTimeSchema;
    }

    public void put(String name, Object v) {
        put(columnToFieldIndex.get(name), v);
    }

    @Override
    public void put(int i, Object v) {
        if (wrapped == null)
            wrapped = new Object[incomingRuntimeSchema.getFields().size()];

        // TODO(rskraba): do something other than STRING!
        wrapped[i] = v == null ? v : String.valueOf(v);
    }

    @Override
    public Object get(int i) {
        return wrapped[i];
    }
}