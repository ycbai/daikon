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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class will provide ResourceBundle string handler with formatted messages using java.text.MessageFormat It will
 * also work with user definined Local that will be used for each message retrieval. This way web application may
 * provide their how LocalProvder to use dynamic local according to the current Request for instance.
 * 
 */
public class FormatedMessage {

    LocaleProvider localeProvider;

    MessageFormat  formatter = new MessageFormat(""); //$NON-NLS-1$

    private String baseName;

    private String unknowKeyPrefix;

    /**
     * return the value associated to the key found in the bundle associated to the localeProvider and the baseName
     * 
     * @param localeProvider, if null the java.util.Locale.getDefault() shall be used
     * @param baseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     * @param unknowKeyPrefix string used to prefix the returned key if the value was not found (if null then an empty
     * String is used)
     */
    public FormatedMessage(LocaleProvider localeProvider, String baseName, String unknowKeyPrefix) {
        this.localeProvider = localeProvider;
        this.baseName = baseName;
        this.unknowKeyPrefix = unknowKeyPrefix == null ? "" : unknowKeyPrefix; //$NON-NLS-1$
    }

    /**
     * same as {@link FormatedMessage#FormatedMessage(LocaleProvider, String, String)} with unknowKeyPrefix set to null
     * 
     * @param localeProvider, if null the java.util.Locale.getDefault() shall be used
     * @param baseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     */
    public FormatedMessage(LocaleProvider localeProvider, String baseName) {
        this(localeProvider, baseName, null);
    }

    /**
     * same as {@link FormatedMessage#FormatedMessage(LocaleProvider, String, String)} with localeProvider set to null
     * 
     * @param baseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     * @param unknowKeyPrefix string used to prefix the returned key if the value was not found (if null then an empty
     * String is used)
     */
    public FormatedMessage(String baseName, String unknowKeyPrefix) {
        this(null, baseName, unknowKeyPrefix);
    }

    /**
     * same as {@link FormatedMessage#FormatedMessage(String, String)} with unknowKeyPrefix set to null
     * 
     * @param baseName, used to create the underlying resource bundle, see {@link ResourceBundle#getBundle(String)}
     * @param unknowKeyPrefix string used to prefix the returned key if the value was not found (default is empty)
     */
    public FormatedMessage(String baseName) {
        this(baseName, null);
    }

    public String getMessage(String key, Object... arguments) {
        // get the ResouceBundle Value
        Locale currentLocale = getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, currentLocale);
        try {
            String bundleValue = bundle.getString(key);
            // format it now
            formatter.setLocale(currentLocale);
            formatter.applyPattern(bundleValue);
            return formatter.format(arguments);
        } catch (MissingResourceException mre) {
            return unknowKeyPrefix + key;
        }
    }

    Locale getLocale() {
        if (localeProvider != null) {
            Locale locale = localeProvider.getLocale();
            return locale != null ? locale : Locale.getDefault();
        } else {
            return Locale.getDefault();
        }
    }
}
