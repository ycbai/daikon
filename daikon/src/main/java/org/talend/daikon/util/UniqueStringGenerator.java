// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.util;

import java.util.List;

/**
 * work for getting the unique name
 */
public class UniqueStringGenerator {

    private String baseName;

    private List<String> existNames;

    public UniqueStringGenerator(String name, List<String> existNames) {
        super();
        this.baseName = name;
        this.existNames = existNames;
    }

    public String getUniqueString() {
        boolean found = false;
        int indexNewColumn = 0;
        String newColumnName = null;
        boolean firstTime = true;
        while (!found) {
            newColumnName = firstTime ? baseName : baseName + (++indexNewColumn);
            firstTime = false;
            boolean allAreDifferent = true;
            for (int j = 0; j < existNames.size(); j++) {
                String existName = existNames.get(j);
                if (existName != null) {
                    if (existName.equals(newColumnName)) {
                        allAreDifferent = false;
                        break;
                    }
                }
            }
            if (allAreDifferent) {
                found = true;
            }
        }

        return newColumnName;
    }

}
