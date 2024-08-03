package com.epimorphismmc.monomorphism.math;

import java.math.BigInteger;

/**
 * Math class of BigInteger.
 *
 * @author GateGuardian
 * @date : 2024/5/24
 */
public class BigIntMath {
    public static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    public static long getLongValue(BigInteger value) {
        if (value != null) {
            return value.compareTo(LONG_MAX) < 0 ? value.longValue() : Long.MAX_VALUE;
        }
        return 0L;
    }

    public static BigInteger clamp(BigInteger value, BigInteger min, BigInteger max) {
        if (value.compareTo(min) < 0) {
            return min;
        } else {
            return max.min(value);
        }
    }

    public static BigInteger summarize(long... values) {
        BigInteger retValue = BigInteger.ZERO;
        long currentSum = 0;

        for (long value : values) {
            if (currentSum != 0 && value > Long.MAX_VALUE - currentSum) {
                retValue = retValue.add(BigInteger.valueOf(currentSum));
                currentSum = 0;
            }
            currentSum += value;
        }

        if (currentSum != 0) {
            retValue = retValue.add(BigInteger.valueOf(currentSum));
        }
        return retValue;
    }
}
