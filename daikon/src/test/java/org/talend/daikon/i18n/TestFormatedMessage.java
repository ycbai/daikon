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
package org.talend.daikon.i18n;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

/**
 * created by sgandon on 11 sept. 2015
 */
public class TestFormatedMessage {

    class MutableLocalProvider implements LocaleProvider {

        Locale locale;

        @Override
        public Locale getLocale() {
            return locale;
        }

        public void setLocale(String language) {
            locale = new Locale(language);
        }
    }

    /**
     * Test method for {@link org.talend.daikon.i18n.FormatedMessage#getMessage(java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testGetMessageAllDefaultStringFormat() {
        FormatedMessage formatedMessage = new FormatedMessage("org.talend.daikon.i18n.testMessage"); //$NON-NLS-1$
        assertEquals("", formatedMessage.getMessage("ze.empty.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", "foo"));
        assertEquals("test {0} and {1}", formatedMessage.getMessage("ze.message.key", null));
        assertEquals("test foo and bar", formatedMessage.getMessage("ze.message.key", "foo", "bar"));
        assertEquals("unknown.key", formatedMessage.getMessage("unknown.key", null));
    }

    /**
     * Test method for {@link org.talend.daikon.i18n.FormatedMessage#getMessage(java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testGetMessageMutableLocalProvider() {
        MutableLocalProvider mutableLocaleProvider = new MutableLocalProvider();
        FormatedMessage formatedMessage = new FormatedMessage(mutableLocaleProvider, "org.talend.daikon.i18n.testMessage"); //$NON-NLS-1$
        // first test with a null local which shall result in the default locale
        assertEquals("", formatedMessage.getMessage("ze.empty.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", "foo"));
        assertEquals("test {0} and {1}", formatedMessage.getMessage("ze.message.key", null));
        assertEquals("test foo and bar", formatedMessage.getMessage("ze.message.key", "foo", "bar"));

        // set another locale
        mutableLocaleProvider.setLocale("ru"); //$NON-NLS-1$
        assertEquals("", formatedMessage.getMessage("ze.empty.key", null));
        assertEquals("norrrrmal", formatedMessage.getMessage("ze.normal.key", null));
        assertEquals("test foo and bar in russian", formatedMessage.getMessage("ze.message.key", "foo", "bar"));

    }

    /**
     * Test method for {@link org.talend.daikon.i18n.FormatedMessage#getMessage(java.lang.String, java.lang.Object[])}.
     */
    @Test
    public void testGetMessageWithSpecificUnknownKeyPrefix() {
        FormatedMessage formatedMessage = new FormatedMessage("org.talend.daikon.i18n.testMessage", "!"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("", formatedMessage.getMessage("ze.empty.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", null));
        assertEquals("normal", formatedMessage.getMessage("ze.normal.key", "foo"));
        assertEquals("test {0} and {1}", formatedMessage.getMessage("ze.message.key", null));
        assertEquals("test foo and bar", formatedMessage.getMessage("ze.message.key", "foo", "bar"));
        assertEquals("!unknown.key", formatedMessage.getMessage("unknown.key", null));
    }

}
