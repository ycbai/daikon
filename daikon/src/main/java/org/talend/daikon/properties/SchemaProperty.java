// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.properties;

import org.apache.avro.Schema;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;

/**
 * Schema Property that get and set an Avro Schema but store a String internally for serialization optimization. The set
 * Value accepts both Schema and json string for schema. The evaluator is also called with the Schema instance.
 */
public class SchemaProperty extends Property implements AnyProperty {

    public SchemaProperty(String name) {
        super(Type.SCHEMA, name);
    }

    public SchemaProperty(String name, String title) {
        super(Type.SCHEMA, name, title);
    }

    @Override
    public void setValue(Object value) {
        Object valueToSet = value;
        if (value != null && value instanceof Schema) {
            valueToSet = value.toString();
        } else if (value instanceof String) {
            valueToSet = value;
        } else {
            throw new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION,
                    new IllegalArgumentException("value should be a String or a Schema."));
        }
        storedValue = valueToSet;
    }

    /**
     * @return the value of the property. This value may not be the one Stored with setValue(), it may be evaluated with
     * {@link PropertyValueEvaluator}.
     * 
     * 
     */
    @Override
    public Object getValue() {
        Object returnValue = null;
        if (storedValue != null) {
            returnValue = new Schema.Parser().parse(storedValue.toString());
            if (propertyValueEvaluator != null) {
                returnValue = propertyValueEvaluator.evaluate(this, returnValue);
            } // else not evaluator so return the storedValue
        }
        return returnValue;
    }

}
