package com.epimorphismmc.monomorphism.utility;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.concurrent.ThreadLocalRandom;

import static com.gregtechceu.gtceu.api.GTValues.RNG;

/**
 * Utility class for math operations.
 * <p>
 * Commonly used math Utilities：
 * {@link Mth},
 * {@link org.joml.Math},
 * {@link com.google.common.primitives.Ints},
 * {@link com.google.common.primitives.Longs}
 *
 * @author GateGuardian
 * @date : 2024/5/24
 */
public class MOMathUtils {

    public static final double PI2 = Math.PI * 2;

    public static long clamp(long value, long min, long max) {
        return value < min ? min : Math.min(value, max);
    }

    public static int nextInt(int minimum, int maximum) {
        return Mth.nextInt(RNG, minimum, maximum);
    }

    public static float nextFloat(float minimum, float maximum) {
        return Mth.nextFloat(RNG, minimum, maximum);
    }

    public static double nextDouble(double minimum, double maximum) {
        return Mth.nextDouble(RNG, minimum, maximum);
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
