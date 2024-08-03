package com.epimorphismmc.monomorphism.client.utils;

import com.lowdragmc.lowdraglib.client.model.ModelFactory;

import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.math.Transformation;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class RenderMath {

    // Rotation
    public static Quaternionf degreeToQuaternion(double x, double y, double z) {
        x = Math.toRadians(x);
        y = Math.toRadians(y);
        z = Math.toRadians(z);
        Quaternionf qYaw = new Quaternionf(0, (float) Math.sin(y / 2), 0, (float) Math.cos(y / 2));
        Quaternionf qPitch = new Quaternionf((float) Math.sin(x / 2), 0, 0, (float) Math.cos(x / 2));
        Quaternionf qRoll = new Quaternionf(0, 0, (float) Math.sin(z / 2), (float) Math.cos(z / 2));

        qYaw.mul(qRoll);
        qYaw.mul(qPitch);
        return qYaw;
    }

    public static Transformation rotateTo(Direction d) {
        return new Transformation(null).compose(ModelFactory.getRotation(d).getRotation());
    }
}
