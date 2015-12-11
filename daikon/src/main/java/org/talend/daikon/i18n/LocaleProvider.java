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

import java.util.Locale;

/**
 * Provide a specific Locale for {@link I18nMessages}.
 */
public interface LocaleProvider {

    /**
     * @return the Locale to be used by {@link I18nMessages}.
     */
    Locale getLocale();
}
