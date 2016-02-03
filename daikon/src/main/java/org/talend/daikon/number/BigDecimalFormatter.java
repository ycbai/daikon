package org.talend.daikon.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Main goal of this class is to provide BigDecimal instance from a String.
 */
public class BigDecimalFormatter {

    private BigDecimalFormatter() {
    }

    public static String format(BigDecimal bd, DecimalFormat format) {
        return format.format(bd).trim();
    }

}
