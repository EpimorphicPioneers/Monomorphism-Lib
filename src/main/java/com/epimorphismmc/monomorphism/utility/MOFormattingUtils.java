package com.epimorphismmc.monomorphism.utility;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class MOFormattingUtils {

    private static final TreeMap<BigInteger, String> UNITS = new TreeMap<>();
    public static final DecimalFormat DECIMAL_FORMAT_0F = new DecimalFormat("#");
    public static final DecimalFormat DECIMAL_FORMAT_1F = new DecimalFormat("#.#");
    public static final DecimalFormat DECIMAL_FORMAT_2F = new DecimalFormat("#.##");

    static {
        UNITS.put(BigInteger.TEN.pow(30), "Q");
        UNITS.put(BigInteger.TEN.pow(27), "R");
        UNITS.put(BigInteger.TEN.pow(24), "Y");
        UNITS.put(BigInteger.TEN.pow(21), "Z");
        UNITS.put(BigInteger.TEN.pow(18), "E");
        UNITS.put(BigInteger.TEN.pow(15), "P");
        UNITS.put(BigInteger.TEN.pow(12), "T");
        UNITS.put(BigInteger.TEN.pow(9), "G");
        UNITS.put(BigInteger.TEN.pow(6), "M");
        UNITS.put(BigInteger.TEN.pow(3), "K");
    }

    public static String abbreviate1F(int number) {
        Map.Entry<BigInteger, String> entry = UNITS.floorEntry(BigInteger.valueOf(number));
        if (entry == null) {
            return FormattingUtil.formatNumbers(number);
        }
        BigInteger divisor = entry.getKey();
        String unit = entry.getValue();
        return DECIMAL_FORMAT_1F.format(number / divisor.doubleValue()) + unit;
    }

    public static String abbreviate0F(int number) {
        Map.Entry<BigInteger, String> entry = UNITS.floorEntry(BigInteger.valueOf(number));
        if (entry == null) {
            return FormattingUtil.formatNumbers(number);
        }
        BigInteger divisor = entry.getKey();
        String unit = entry.getValue();
        return DECIMAL_FORMAT_0F.format(number / divisor.doubleValue()) + unit;
    }

    public static String abbreviate2F(long number) {
        Map.Entry<BigInteger, String> entry = UNITS.floorEntry(BigInteger.valueOf(number));
        if (entry == null) {
            return FormattingUtil.formatNumbers(number);
        }
        BigInteger divisor = entry.getKey();
        String unit = entry.getValue();
        return DECIMAL_FORMAT_2F.format(number / divisor.doubleValue()) + unit;
    }

    public static String abbreviate2F(BigInteger number) {
        Map.Entry<BigInteger, String> entry = UNITS.floorEntry(number);
        if (entry == null) {
            return FormattingUtil.formatNumbers(number);
        }
        BigInteger divisor = entry.getKey();
        String unit = entry.getValue();
        return DECIMAL_FORMAT_2F.format(number.doubleValue() / divisor.doubleValue()) + unit;
    }

}
