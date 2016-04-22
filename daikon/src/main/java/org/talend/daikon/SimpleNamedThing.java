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
package org.talend.daikon;

import org.talend.daikon.i18n.TranslatableImpl;

/**
 * Something that is named.
 */
public class SimpleNamedThing extends TranslatableImpl implements NamedThing {

    protected String name;

    protected String displayName;

    protected String title;

    public SimpleNamedThing() {
        this(null, null, null);
    }

    public SimpleNamedThing(String name) {
        this(name, null, null);
    }

    public SimpleNamedThing(String name, String displayName) {
        this(name, displayName, null);
    }

    public SimpleNamedThing(String name, String displayName, String title) {
        this.name = name;
        this.displayName = displayName;
        this.title = title;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return (getName() != null ? getName() : "") + "/" + (getDisplayName() != null ? getDisplayName() : "") + "/"
                + (getTitle() != null ? getTitle() : "");
    }

}
