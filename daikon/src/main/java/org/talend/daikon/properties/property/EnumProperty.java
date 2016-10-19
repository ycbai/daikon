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
package org.talend.daikon.properties.property;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.serialize.PostDeserializeHandler;
import org.talend.daikon.serialize.PostDeserializeSetup;

/**
 * Property that contains an enum
 */
public class EnumProperty<T extends Enum<T>> extends Property<T> implements PostDeserializeHandler {

    public EnumProperty(Class<T> zeEnumType, String name) {
        super(zeEnumType, name, null);
        // set the possible values accoording with all the enum types.
        T[] enumConstants = zeEnumType.getEnumConstants();
        this.setPossibleValues(enumConstants);

    }

    /*
     * This is package protected because this constructor should only be used when copying a Property at runtime, so it
     * does not need to be typed and for json-io deserialization
     */
    EnumProperty(String type, String name) {
        super(type, name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        Object value = storedValue;
        if (propertyValueEvaluator != null) {
            value = propertyValueEvaluator.evaluate(this, storedValue);
        } // else not evaluator so return the storedValue
          // try to convert the String to the enum
        if (value instanceof String) {
            try {
                Class<T> enumClass = (Class<T>) ClassUtils.getClass(getType());
                return Enum.valueOf(enumClass, (String) value);
            } catch (ClassNotFoundException e) {
                TalendRuntimeException.unexpectedException(e);
            }
        }
        return (T) value;
    }

    /*
     * this will replace the "." with a "$" for all String starting with a Capital letter except for the first one.
     */
    String convertToInnerClassString(String type) {
        return StringUtils.replacePattern(type, "([a-z0-9]*\\\\.[A-Z][^.]*)?((\\\\.)([A-Z][a-z0-9]*))", "$1\\$$4");
    }

    @Override
    public boolean postDeserialize(int version, PostDeserializeSetup setup, boolean persistent) {
        // initialize the possible values
        try {
            @SuppressWarnings("unchecked")
            Class<T> enumClass = (Class<T>) ClassUtils.getClass(getType());
            T[] enumConstants = enumClass.getEnumConstants();
            // enum has changed so update it
            if (enumConstants.length != getPossibleValues().size()) {
                setPossibleValues(enumConstants);
                return true;
            }
        } catch (ClassNotFoundException e) {
            TalendRuntimeException.unexpectedException(e);
        }
        return false;
    }

}
