package com.epimorphismmc.monomorphism.utility;

import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static com.gregtechceu.gtceu.api.GTValues.RNG;

public class MOMathUtils {

    public static final double PI2 = Math.PI * 2;

    public static final BigInteger LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    public static long clamp(long value, long min, long max) {
        if (value < min) {
            return min;
        }else {
            return Math.min(value, max);
        }
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }else {
            return Math.min(value, max);
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }else {
            return Math.min(value, max);
        }
    }

    public static int max(int... nums) {
        return Arrays.stream(nums).max().orElse(0);
    }

    public static long getLongNumber(BigInteger number) {
        if (number != null) {
            return number.compareTo(LONG_MAX_VALUE) >= 0 ? Long.MAX_VALUE : number.longValue();
        }
        return 0;
    }

    public static int getIntegerNumber(long number) {
        return (int) clamp(number, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static float nextFloat(float a, float b) {
        return RNG.nextFloat() * (b - a) + a;
    }

    public static Vector3f randomSpherePoint(double x0, double y0, double z0, Vec3 radius) {
        double u = RNG.nextDouble();
        double v = RNG.nextDouble();
        double theta = PI2 * u;
        double phi = Math.acos(2 * v - 1);
        double x = x0 + (radius.x * Math.sin(phi) * Math.cos(theta));
        double y = y0 + (radius.y * Math.sin(phi) * Math.sin(theta));
        double z = z0 + (radius.z * Math.cos(phi));
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public static Vec2 randomCirclePoint(float radius) {
        double u = RNG.nextDouble();
        double theta = PI2 * u;
        double x = (radius * Math.cos(theta));
        double y = (radius * Math.sin(theta));
        return new Vec2((float) x, (float) y);
    }

    public static Vec2 getRandomPointInRectangle(Vec2 p1, Vec2 p2) {
        float minX = Math.min(p1.x, p2.x);
        float minY = Math.min(p1.y, p2.y);
        float maxX = Math.max(p1.x, p2.x);
        float maxY = Math.max(p1.y, p2.y);

        float randomX = minX + RNG.nextFloat() * (maxX - minX);
        float randomY = minY + RNG.nextFloat() * (maxY - minY);

        return new Vec2(randomX, randomY);
    }

    public static ThreadLocalRandom localRandom() {
        return ThreadLocalRandom.current();
    }
}
