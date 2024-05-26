package com.epimorphismmc.monomorphism.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MOFormattingUtils {

    public static final DecimalFormat DECIMAL_FORMAT_0F = new DecimalFormat("#");
    public static final DecimalFormat DECIMAL_FORMAT_1F = new DecimalFormat("#.#");
    public static final DecimalFormat DECIMAL_FORMAT_2F = new DecimalFormat("#.##");

    private static final String[] UNITS = {"", "K", "M", "G", "T", "P", "E"};

    public static String formatNumber(int number) {
        return formatNumber(BigInteger.valueOf(number));
    }

    public static String formatNumber(int number, DecimalFormat df) {
        return formatNumber(BigInteger.valueOf(number), df);
    }

    public static String formatNumber(long number) {
        return formatNumber(BigInteger.valueOf(number));
    }

    public static String formatNumber(long number, DecimalFormat df) {
        return formatNumber(BigInteger.valueOf(number), df);
    }

    public static String formatNumber(BigInteger number) {
        return formatNumber(number, MOFormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String formatNumber(BigInteger number, DecimalFormat df) {
        if (number.compareTo(BigInteger.ZERO) < 0) {
            return "-" + formatNumber(number.negate(), df);
        }

        int unitIndex = 0;
        BigDecimal temp = new BigDecimal(number, 0);
        while (temp.compareTo(BigDecimal.TEN.pow(3)) >= 0 && unitIndex < UNITS.length) {
            temp = temp.divide(BigDecimal.TEN.pow(3), 2, RoundingMode.HALF_DOWN);
            unitIndex++;
        }

        String formattedNumber = df.format(temp.doubleValue());

        if (unitIndex >= UNITS.length) {
            return String.format("%.2e", number.doubleValue());
        }

        return formattedNumber + UNITS[unitIndex];
    }
}
