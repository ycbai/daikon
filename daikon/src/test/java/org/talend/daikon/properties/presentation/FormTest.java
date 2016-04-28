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
package org.talend.daikon.properties.presentation;

import static org.junit.Assert.*;
import static org.talend.daikon.properties.PropertyFactory.*;
import static org.talend.daikon.properties.presentation.Widget.*;

import org.junit.Test;
import org.talend.daikon.properties.Properties;

public class FormTest {

    @Test
    public void testNameEqualsDisplayNameWithUsualConstructor() {
        Form form = new Form(new Properties("bar") { //$NON-NLS-1$
        }, "foo"); //$NON-NLS-1$
        assertEquals("foo", form.getName()); //$NON-NLS-1$
        assertEquals("foo", form.getDisplayName()); //$NON-NLS-1$
    }

    @Test
    public void testGetI18NFields() {
        Form form = new Form(new Properties("bar") { //$NON-NLS-1$
        }, "foo", null); //$NON-NLS-1$
        assertEquals("Ze Form DisplayName", form.getDisplayName()); //$NON-NLS-1$
        assertEquals("Ze Form Title", form.getTitle()); //$NON-NLS-1$
        assertEquals("Ze Form SubTitle", form.getSubtitle()); //$NON-NLS-1$
    }

    @Test
    public void testGetI18NFiledsWithDefaultValue() {
        String displayName = "Default Display Name"; //$NON-NLS-1$
        String subTitle = "Default SubTitle"; //$NON-NLS-1$
        Form form = new Form(new Properties("bar") { //$NON-NLS-1$
        }, "foo", displayName); //$NON-NLS-1$
        form.setSubtitle(subTitle);
        assertEquals(displayName, form.getDisplayName());
        assertEquals("Ze Form Title", form.getTitle());
        assertEquals(subTitle, form.getSubtitle());
    }

    @Test
    public void testSetVisible() {
        Form form = new Form(new Properties("bar") { //$NON-NLS-1$
        }, "foo"); //$NON-NLS-1$
        form.addRow(widget(newString("w1")));
        form.addRow(widget(newString("w2")));
        form.addRow(widget(newString("w3")));
        assertTrue(form.getWidget("w1").isVisible());
        assertTrue(form.getWidget("w2").isVisible());
        assertTrue(form.getWidget("w3").isVisible());
        form.setVisible(false);
        assertFalse(form.getWidget("w1").isVisible());
        assertFalse(form.getWidget("w2").isVisible());
        assertFalse(form.getWidget("w3").isVisible());
        form.setVisible(true);
        assertTrue(form.getWidget("w1").isVisible());
        assertTrue(form.getWidget("w2").isVisible());
        assertTrue(form.getWidget("w3").isVisible());
    }

}
