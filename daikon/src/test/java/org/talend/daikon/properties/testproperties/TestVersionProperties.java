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
package org.talend.daikon.properties.testproperties;

import org.talend.daikon.serialize.SerializeSetVersion;

public class TestVersionProperties extends TestProperties implements SerializeSetVersion {

    public TestVersionProperties(String name) {
        super(name);
    }

    @Override
    public int getVersionNumber() {
        return 0;
    }

}
