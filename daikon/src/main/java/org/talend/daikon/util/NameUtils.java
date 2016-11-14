package org.talend.daikon.util;

import java.util.ArrayList;
import java.util.List;

public class NameUtils {

    public static String validateColumnName(final String columnName, final int index, List<String> existNames) {
        String name = validateColumnName(columnName, index);
        UniqueStringGenerator uniqueStringGenerator = new UniqueStringGenerator(name, existNames);
        return uniqueStringGenerator.getUniqueString();
    }

    private static String validateColumnName(final String columnName, final int index) {
        String originalColumnName = mapSpecialChar(columnName);
        final String underLine = "_";

        boolean isKeyword = KeywordsValidator.isKeyword(originalColumnName);

        StringBuilder returnedColumnName = new StringBuilder();

        if (!isKeyword) {
            for (int i = 0; i < originalColumnName.length(); i++) {
                Character c = originalColumnName.charAt(i);
                if (((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z')) || c == '_'
                        || ((c >= '0') && (c <= '9') && (i != 0))) {
                    returnedColumnName.append(c);
                } else {
                    returnedColumnName.append(underLine);
                }
            }
        } else {
            returnedColumnName.append("Column").append(index);
        }

        return returnedColumnName.toString();
    }

    private static List<String> accentsMap = null;

    private static String mapSpecialChar(String columnName) {
        if (accentsMap == null) {
            accentsMap = new ArrayList<String>();
            String c = null;

            c = new String("A");
            accentsMap.add(c); /* '\u00C0' alt-0192 */
            accentsMap.add(c); /* '\u00C1' alt-0193 */
            accentsMap.add(c); /* '\u00C2' alt-0194 */
            accentsMap.add(c); /* '\u00C3' alt-0195 */
            accentsMap.add(c); /* '\u00C4' alt-0196 */
            accentsMap.add(c); /* '\u00C5' alt-0197 */
            c = new String("AE");
            accentsMap.add(c); /* '\u00C6' alt-0198 */
            c = new String("C");
            accentsMap.add(c); /* '\u00C7' alt-0199 */
            c = new String("E");
            accentsMap.add(c); /* '\u00C8' alt-0200 */
            accentsMap.add(c); /* '\u00C9' alt-0201 */
            accentsMap.add(c); /* '\u00CA' alt-0202 */
            accentsMap.add(c); /* '\u00CB' alt-0203 */
            c = new String("I");
            accentsMap.add(c); /* '\u00CC' alt-0204 */
            accentsMap.add(c); /* '\u00CD' alt-0205 */
            accentsMap.add(c); /* '\u00CE' alt-0206 */
            accentsMap.add(c); /* '\u00CF' alt-0207 */
            c = new String("D");
            accentsMap.add(c); /* '\u00D0' alt-0208 */
            c = new String("N");
            accentsMap.add(c); /* '\u00D1' alt-0209 */
            c = new String("O");
            accentsMap.add(c); /* '\u00D2' alt-0210 */
            accentsMap.add(c); /* '\u00D3' alt-0211 */
            accentsMap.add(c); /* '\u00D4' alt-0212 */
            accentsMap.add(c); /* '\u00D5' alt-0213 */
            accentsMap.add(c); /* '\u00D6' alt-0214 */
            c = new String("*");
            accentsMap.add(c); /* '\u00D7' alt-0215 */
            c = new String("0");
            accentsMap.add(c); /* '\u00D8' alt-0216 */
            c = new String("U");
            accentsMap.add(c); /* '\u00D9' alt-0217 */
            accentsMap.add(c); /* '\u00DA' alt-0218 */
            accentsMap.add(c); /* '\u00DB' alt-0219 */
            accentsMap.add(c); /* '\u00DC' alt-0220 */
            c = new String("Y");
            accentsMap.add(c); /* '\u00DD' alt-0221 */
            c = new String("_");
            accentsMap.add(c); /* '\u00DE' alt-0222 */
            c = new String("B");
            accentsMap.add(c); /* '\u00DF' alt-0223 */
            c = new String("a");
            accentsMap.add(c); /* '\u00E0' alt-0224 */
            accentsMap.add(c); /* '\u00E1' alt-0225 */
            accentsMap.add(c); /* '\u00E2' alt-0226 */
            accentsMap.add(c); /* '\u00E3' alt-0227 */
            accentsMap.add(c); /* '\u00E4' alt-0228 */
            accentsMap.add(c); /* '\u00E5' alt-0229 */
            c = new String("ae");
            accentsMap.add(c); /* '\u00E6' alt-0230 */
            c = new String("c");
            accentsMap.add(c); /* '\u00E7' alt-0231 */
            c = new String("e");
            accentsMap.add(c); /* '\u00E8' alt-0232 */
            accentsMap.add(c); /* '\u00E9' alt-0233 */
            accentsMap.add(c); /* '\u00EA' alt-0234 */
            accentsMap.add(c); /* '\u00EB' alt-0235 */
            c = new String("i");
            accentsMap.add(c); /* '\u00EC' alt-0236 */
            accentsMap.add(c); /* '\u00ED' alt-0237 */
            accentsMap.add(c); /* '\u00EE' alt-0238 */
            accentsMap.add(c); /* '\u00EF' alt-0239 */
            c = new String("d");
            accentsMap.add(c); /* '\u00F0' alt-0240 */
            c = new String("n");
            accentsMap.add(c); /* '\u00F1' alt-0241 */
            c = new String("o");
            accentsMap.add(c); /* '\u00F2' alt-0242 */
            accentsMap.add(c); /* '\u00F3' alt-0243 */
            accentsMap.add(c); /* '\u00F4' alt-0244 */
            accentsMap.add(c); /* '\u00F5' alt-0245 */
            accentsMap.add(c); /* '\u00F6' alt-0246 */
            c = new String("/");
            accentsMap.add(c); /* '\u00F7' alt-0247 */
            c = new String("0");
            accentsMap.add(c); /* '\u00F8' alt-0248 */
            c = new String("u");
            accentsMap.add(c); /* '\u00F9' alt-0249 */
            accentsMap.add(c); /* '\u00FA' alt-0250 */
            accentsMap.add(c); /* '\u00FB' alt-0251 */
            accentsMap.add(c); /* '\u00FC' alt-0252 */
            c = new String("y");
            accentsMap.add(c); /* '\u00FD' alt-0253 */
            c = new String("_");
            accentsMap.add(c); /* '\u00FE' alt-0254 */
            c = new String("y");
            accentsMap.add(c); /* '\u00FF' alt-0255 */
            accentsMap.add(c); /* '\u00FF' alt-0255 */

            accentsMap.set(4, "AE");
            accentsMap.set(22, "OE");
            accentsMap.set(28, "UE");
            accentsMap.set(31, "ss");
            accentsMap.set(36, "ae");
            accentsMap.set(54, "oe");
            accentsMap.set(60, "ue");

            for (int i = 257; i < 304; i++) {
                accentsMap.add(String.valueOf((char) i));
            }

            accentsMap.add("I");
        }

        return initSpecificMapping(columnName, accentsMap);
    }

    private static final int MIN = 192;

    private static String initSpecificMapping(String columnName, List<String> map) {
        for (int i = 0; i < columnName.toCharArray().length; i++) {
            int carVal = columnName.charAt(i);
            if (carVal >= MIN && carVal <= MIN + map.size()) {
                String oldVal = String.valueOf(columnName.toCharArray()[i]);
                String newVal = map.get(carVal - MIN);
                if (!(oldVal.equals(newVal))) {
                    columnName = columnName.replaceAll(oldVal, newVal);
                }
            }
        }

        return columnName;
    }

}
