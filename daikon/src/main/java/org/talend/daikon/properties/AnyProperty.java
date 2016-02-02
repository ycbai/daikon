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

import org.talend.daikon.NamedThing;

/**
 * defines all named properties and provide a way to visit them.
 */
public interface AnyProperty extends NamedThing {

    /**
     * be visited from it's parent.
     * 
     * @param visitor, the visitor of the object
     * @param parent Properties that issued the visit or null if no parent is set.
     * 
     */
    public void accept(AnyPropertyVisitor visitor, Properties parent);
}
