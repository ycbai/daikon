package org.talend.daikon.di;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.Test;
import org.talend.daikon.avro.SchemaConstants;

/**
 * Unit-tests for {@link DiOutgoingDynamicSchemaEnforcer} class
 */
public class DiOutgoingDynamicSchemaEnforcerTest {
    
    /**
     * Checks {@link DiOutgoingDynamicSchemaEnforcer#getSchema()} returns design schema, which was passed to constructor without
     * any changes
     */
    @Test
    public void testGetSchema() {
        
        Schema designSchema = SchemaBuilder.builder().record("Record")
                .prop(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "0").prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true").fields()
                .name("col1").type().intType().noDefault()
                .name("col2").type().stringType().noDefault()
                .name("col3").type().intType().noDefault()
                .endRecord();
        
        Schema runtimeSchema = SchemaBuilder.builder().record("Record").fields()
                .name("col0_1").type().intType().noDefault()
                .name("col0_2").type().intType().noDefault()
                .name("col1").type().intType().noDefault()
                .name("col2").type().stringType().noDefault()
                .name("col3").type().intType().noDefault()
                .endRecord();

        DynamicIndexMapper indexMapper = new DynamicIndexMapperByIndex(designSchema, runtimeSchema);
        DiOutgoingDynamicSchemaEnforcer enforcer = new DiOutgoingDynamicSchemaEnforcer(designSchema, runtimeSchema, indexMapper);
        Schema actualSchema = enforcer.getSchema();
        assertEquals(designSchema, actualSchema);
    }
    
    /**
     * Checks {@link DiOutgoingDynamicSchemaEnforcer#getDynamicFieldsSchema()} returns schema, which contains only dynamic fields
     * (i.e. fields which are present in runtime schema, but are not present in design schema)
     */
    @Test
    public void testGetDynamicFieldsSchema() {
        
        Schema expectedDynamicSchema = SchemaBuilder.builder().record("dynamic").fields()
                .name("col0_1").type().intType().noDefault()
                .name("col0_2").type().intType().noDefault()
                .endRecord();
        
        Schema designSchema = SchemaBuilder.builder().record("Record")
                .prop(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "0").prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true").fields()
                .name("col1").type().intType().noDefault()
                .name("col2").type().stringType().noDefault()
                .name("col3").type().intType().noDefault()
                .endRecord();
        
        Schema runtimeSchema = SchemaBuilder.builder().record("Record").fields()
                .name("col0_1").type().intType().noDefault()
                .name("col0_2").type().intType().noDefault()
                .name("col1").type().intType().noDefault()
                .name("col2").type().stringType().noDefault()
                .name("col3").type().intType().noDefault()
                .endRecord();

        DynamicIndexMapper indexMapper = new DynamicIndexMapperByIndex(designSchema, runtimeSchema);
        DiOutgoingDynamicSchemaEnforcer enforcer = new DiOutgoingDynamicSchemaEnforcer(designSchema, runtimeSchema, indexMapper);
        Schema actualDynamicSchema = enforcer.getDynamicFieldsSchema();
        assertEquals(expectedDynamicSchema, actualDynamicSchema);
    }
    
    /**
     * Checks {@link DiOutgoingDynamicSchemaEnforcer#getDynamicFieldsSchema()} returns schema, which contains only dynamic fields
     * (i.e. fields which are present in runtime schema, but are not present in design schema)
     */
    @Test
    public void testGetDynamicAtStart() {
        
        Schema designSchema = SchemaBuilder.builder().record("Record")
                .prop(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "0").prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true").fields()
                .name("name").type().intType().noDefault()
                .name("valid").type().stringType().noDefault()
                .name("createdDate").type().intType().noDefault()
                .endRecord();
        
        Schema runtimeSchema = SchemaBuilder.builder().record("Record").fields()
                .name("id").type().intType().noDefault()
                .name("name").type().stringType().noDefault()
                .name("age").type().intType().noDefault()
                .name("valid").type().booleanType().noDefault()
                .name("address").type().stringType().noDefault()
                .name("comment").prop(DiSchemaConstants.TALEND6_COLUMN_LENGTH, "255").type().stringType().noDefault()
                .name("createdDate").prop(DiSchemaConstants.TALEND6_COLUMN_TALEND_TYPE, "id_Date")
                .prop(DiSchemaConstants.TALEND6_COLUMN_PATTERN, "yyyy-MM-dd'T'HH:mm:ss'000Z'").type().nullable().longType().noDefault()
                .endRecord();

        IndexedRecord record = new GenericData.Record(runtimeSchema);
        record.put(0, 1);
        record.put(1, "User");
        record.put(2, 100);
        record.put(3, true);
        record.put(4, "Main Street");
        record.put(5, "This is a record with six columns.");
        record.put(6, new Date(1467170137872L));

        DynamicIndexMapper indexMapper = new DynamicIndexMapperByName(designSchema, runtimeSchema);
        DiOutgoingDynamicSchemaEnforcer enforcer = new DiOutgoingDynamicSchemaEnforcer(designSchema, runtimeSchema, indexMapper);
        enforcer.setWrapped(record);

        assertThat(enforcer.get(1), equalTo((Object) "User"));
        assertThat(enforcer.get(2), equalTo((Object) true));
        assertThat(enforcer.get(3), equalTo((Object) new Date(1467170137872L)));

        Map<String, Object> dynamicValues = (Map<String, Object>) enforcer.get(0);
        assertThat(dynamicValues.size(), equalTo(4));
        assertThat(dynamicValues, hasEntry("id", (Object) 1));
        assertThat(dynamicValues, hasEntry("age", (Object) 100));
        assertThat(dynamicValues, hasEntry("address", (Object) "Main Street"));
        assertThat(dynamicValues, hasEntry("comment", (Object) "This is a record with six columns."));
    }
}