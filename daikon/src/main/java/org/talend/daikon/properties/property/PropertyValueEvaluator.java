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

/**
 * Implement this to translate the stored value associated with a {@link Property} object into an actual value for the property.
 * When this is implemented, the stored value is typically some kind of key which can be looked up in a context to provide the
 * actual value.
 */
public interface PropertyValueEvaluator {

    /**
     * Compute the actual value of the given property according to the storedValue
     * 
     * @param storedValue value stored for this property that may be transformed.
     * @return the evaluated value for the property based on the given stored value.
     */
    <T> T evaluate(Property<T> property, Object storedValue);
}
