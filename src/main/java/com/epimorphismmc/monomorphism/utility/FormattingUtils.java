package com.epimorphismmc.monomorphism.utility;

import com.google.common.base.CaseFormat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

public class FormattingUtils {
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
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_0F);
    }

    public static String abbreviate0F(BigDecimal number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_0F);
    }

    public static String abbreviate1F(double number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate1F(BigInteger number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate1F(BigDecimal number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_1F);
    }

    public static String abbreviate2F(double number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String abbreviate2F(BigInteger number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_2F);
    }

    public static String abbreviate2F(BigDecimal number) {
        return abbreviate(number, FormattingUtils.DECIMAL_FORMAT_2F);
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

    /**
     * Does almost the same thing as .to(LOWER_UNDERSCORE, string), but it also inserts underscores between words and
     * numbers.
     *
     * @param string Any string with ASCII characters.
     * @return A string that is all lowercase, with underscores inserted before word/number boundaries:
     *         "maragingSteel300" -> "maraging_steel_300"
     */
    public static String toLowerCaseUnderscore(String string) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            if (i != 0
                    && (Character.isUpperCase(string.charAt(i))
                            || (Character.isDigit(string.charAt(i - 1)) ^ Character.isDigit(string.charAt(i)))))
                result.append("_");
            result.append(Character.toLowerCase(string.charAt(i)));
        }
        return result.toString();
    }

    /**
     * Does almost the same thing as .to(LOWER_UNDERSCORE, string), but it also inserts underscores between words and
     * numbers.
     *
     * @param string Any string with ASCII characters.
     * @return A string that is all lowercase, with underscores inserted before word/number boundaries:
     *         "maragingSteel300" -> "maraging_steel_300"
     */
    public static String toLowerCaseUnder(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    /**
     * Converts integers to roman numerals.
     * e.g. 17 => XVII, 2781 => MMDCCLXXXI
     */
    public static String toRomanNumeral(int number) {
        return "I"
                .repeat(number)
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }
}
