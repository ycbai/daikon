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

import java.util.ArrayList;
import java.util.List;

import org.talend.daikon.NamedThing;

/**
 * This property has an extra method to handle possible values of type NamedThing
 */
public class StringProperty extends Property<String> {

    private List<NamedThing> possibleValues2;

    public StringProperty(String name) {
        super(String.class, name);
    }

    /**
     * this is package protected because this constructor should only be used when copying a Property at runtime, so it
     * does not need to be typed.
     */
    StringProperty(String type, String name) {
        super(type, name);
    }

    public Property<String> setPossibleNamedThingValues(List<NamedThing> possibleValues) {
        this.possibleValues2 = possibleValues;
        // stores the real possible values which are the NameThing names
        ArrayList<String> realPossibleValues = new ArrayList<>();
        for (NamedThing nt : possibleValues2) {
            realPossibleValues.add(nt.getName());
        }
        setPossibleValues(realPossibleValues);
        return this;
    }

    @Override
    public String getPossibleValuesDisplayName(String possibleValue) {
        String displayName = possibleValue;
        // look for the named thing named possibleValue and return it's display name if not null
        for (NamedThing nt : possibleValues2) {
            if (possibleValue != null && possibleValue.equals(nt.getName())) {
                displayName = nt.getDisplayName();
                break;
            } // else keep looking
        }
        return displayName != null ? displayName : "null";
    }

}
