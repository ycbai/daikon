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
 * This provider will be used by {@link I18nMessages} in order to compute the local for every message to be
 * retreived.
 */
public interface LocaleProvider {

    /**
     * This shall return the current Local at a given point in time or request.
     * 
     * @return the current local to be used by I18nMessages.
     */
    Locale getLocale();
}
