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
package org.talend.daikon.properties;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.talend.daikon.NamedThing;
import org.talend.daikon.SimpleNamedThing;

public class StringPropertyTest {

    /**
     * Test method for {@link org.talend.daikon.properties.StringProperty#setPossibleNamedThingValues(java.util.List)}.
     */
    @Test
    public void testSetPossibleValuesWithNamedThing() {
        StringProperty stringProperty = new StringProperty("foo");
        ArrayList<NamedThing> namedThings = new ArrayList<>();
        namedThings.add(new SimpleNamedThing("foo", "fo o"));
        namedThings.add(new SimpleNamedThing("bar", "ba r"));
        namedThings.add(new SimpleNamedThing("a null", null));
        stringProperty.setPossibleNamedThingValues(namedThings);
        assertThat(stringProperty.getPossibleValues(), contains("foo", "bar", "a null"));
        assertEquals("fo o", stringProperty.getPossibleValuesDisplayName("foo"));
        assertEquals("ba r", stringProperty.getPossibleValuesDisplayName("bar"));
        assertEquals("null", stringProperty.getPossibleValuesDisplayName("a null"));
    }
}
