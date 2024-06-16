package com.epimorphismmc.monomorphism.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class MOFormattingUtils {
    public static final DecimalFormat DECIMAL_FORMAT_0F = new DecimalFormat("#");
    public static final DecimalFormat DECIMAL_FORMAT_1F = new DecimalFormat("#.#");
    public static final DecimalFormat DECIMAL_FORMAT_2F = new DecimalFormat("#.##");
    public static final DecimalFormat DECIMAL_FORMAT_SIC = new DecimalFormat("0E00");

    private static final TreeMap<BigDecimal, String> UNITS = new TreeMap<>();
    private static final String[] UNIT_SUFFIXES = {
        "", "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q", ""
    };

    static {
        for (int i = 1; i < UNIT_SUFFIXES.length; i++) {
            UNITS.put(BigDecimal.TEN.pow(3 * i), UNIT_SUFFIXES[i]);
        }
    }

    public static String abbreviate0F(double number) {
        return abbreviate(number, DECIMAL_FORMAT_0F);
    }

    public static String abbreviate0F(BigInteger number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_0F);
    }

    public static String abbreviate0F(BigDecimal number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_0F);
    }

    public static String abbreviate1F(double number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate1F(BigInteger number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate1F(BigDecimal number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate2F(double number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String abbreviate2F(BigInteger number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String abbreviate2F(BigDecimal number) {
        return abbreviate(number, MOFormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String abbreviate(double number, DecimalFormat df) {
        if (number < 0L) {
            return "-" + abbreviate(-number, df);
        }

        int unitIndex = 0;
        double temp = number;
        while (temp >= 1000D && unitIndex < UNIT_SUFFIXES.length - 1) {
            temp /= 1000D;
            unitIndex++;
        }

        if (unitIndex >= UNIT_SUFFIXES.length - 1) {
            return DECIMAL_FORMAT_SIC.format(number);
        }

        return df.format(temp) + UNIT_SUFFIXES[unitIndex];
    }

    public static String abbreviate(BigInteger number, DecimalFormat df) {
        return abbreviate(new BigDecimal(number, 0), df);
    }

    public static String abbreviate(BigDecimal number, DecimalFormat df) {
        if (number.compareTo(BigDecimal.ZERO) < 0) {
            return "-" + abbreviate(number.negate(), df);
        }

        Map.Entry<BigDecimal, String> entry = UNITS.floorEntry(number);
        if (entry == null) {
            return df.format(number);
        }
        if (entry.getValue().isEmpty()) {
            return DECIMAL_FORMAT_SIC.format(number);
        }

        BigDecimal divisor = entry.getKey();
        String unit = entry.getValue();
        return df.format(number.divide(divisor, 2, RoundingMode.HALF_DOWN)) + unit;
    }
}
