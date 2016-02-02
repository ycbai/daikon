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

import org.talend.daikon.SimpleNamedThing;
import org.talend.daikon.properties.presentation.Form;

/**
 * A widget that appears in the UI that is not backed by a component property.
 */
public class PresentationItem extends SimpleNamedThing {

    /**
     * The Form to show when this {@code PresentationItem} is activated (the button is pressed).
     */
    private Form formtoShow;

    public PresentationItem(String name, String displayName) {
        super(name, displayName);
    }

    public Form getFormtoShow() {
        return formtoShow;
    }

    public void setFormtoShow(Form formtoShow) {
        this.formtoShow = formtoShow;
    }

    public String toString() {
        return "Presentation Item: " + getName() + " - " + getTitle();
    }

}
