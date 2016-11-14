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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * the keyword validator
 */
public class KeywordsValidator {

    private static Set<String> keywordsSet = null;

    public static boolean isKeyword(String word) {
        Set<String> words = getKeywords();
        if (words == null) {
            return false;
        }
        if (word == null) {
            return false;
        }
        if (word.equalsIgnoreCase("class")) {
            return true;
        }
        if (word.equalsIgnoreCase("org")) {
            return true;
        }

        if (word.equalsIgnoreCase("log")) {
            return true;
        }
        return words.contains(word);
    }

    private static Set<String> getKeywords() {
        // case sensitive
        String[] keywords = { "abstract", "break", "case", "catch", "continue", "default", "do", "else", "extends", "final",
                "finally", "for", "if", "implements", "instanceof", "native", "new", "private", "protected", "public", "return",
                "static", "switch", "synchronized", "throw", "throws", "transient", "try", "volatile", "while", "assert", "enum",
                "strictfp", "package", "import", "boolean", "byte", "char", "class", "double", "float", "int", "interface",
                "long", "short", "void", "java", "org", "String", "etc", "com", "net", "fr", "sf", "routines", "javax", "false",
                "null", "super", "this", "true", "goto", "const" };

        if (keywordsSet == null) {
            keywordsSet = new HashSet<String>();
            keywordsSet.addAll(Arrays.asList(keywords));
        }

        return keywordsSet;
    }

}
