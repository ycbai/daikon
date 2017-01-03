/*
 * ============================================================================
 *
 * Copyright (C) 2006-2015 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement
 * along with this program; if not, write to Talend SA
 * 9 rue Pages 92150 Suresnes, France
 *
 * ============================================================================
 */

package org.talend.daikon.mongo.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to add to implementations of {@link org.springframework.core.convert.converter.Converter}
 * to be added as Spring Data Mongo customer converters.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoCustomConverter {

}
