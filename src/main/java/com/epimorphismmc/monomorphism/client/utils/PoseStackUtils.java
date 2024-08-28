package com.epimorphismmc.monomorphism.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class PoseStackUtils {

    public static void moveToFace(PoseStack poseStack, double x, double y, double z, Direction face) {
        poseStack.translate(x + 0.5 + face.getStepX() * 0.5, y + 0.5 + face.getStepY() * 0.5, z + 0.5 + face.getStepZ() * 0.5);
    }

    public static void rotateToFace(PoseStack poseStack, Direction face, @Nullable Direction spin) {
        float angle = spin == Direction.EAST ? Mth.HALF_PI : spin == Direction.SOUTH ? Mth.PI : spin == Direction.WEST ? -Mth.HALF_PI : 0;
        switch (face) {
            case UP -> {
                poseStack.scale(1.0f, -1.0f, 1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(Mth.HALF_PI, new Vector3f(1, 0, 0)));
                poseStack.mulPose(new Quaternionf().rotateAxis(angle, new Vector3f(0, 0, 1)));
            }
            case DOWN -> {
                poseStack.scale(1.0f, -1.0f, 1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(-Mth.HALF_PI, new Vector3f(1, 0, 0)));
                poseStack.mulPose(new Quaternionf().rotateAxis(spin == Direction.EAST ? Mth.HALF_PI : spin == Direction.NORTH ? Mth.PI : spin == Direction.WEST ? -Mth.HALF_PI : 0, new Vector3f(0, 0, 1)));
            }
            case EAST -> {
                poseStack.scale(-1.0f, -1.0f, -1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(-Mth.HALF_PI, new Vector3f(0, 1, 0)));
                poseStack.mulPose(new Quaternionf().rotateAxis(angle, new Vector3f(0, 0, 1)));
            }
            case WEST -> {
                poseStack.scale(-1.0f, -1.0f, -1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(Mth.HALF_PI, new Vector3f(0, 1, 0)));
                poseStack.mulPose(new Quaternionf().rotateAxis(angle, new Vector3f(0, 0, 1)));
            }
            case NORTH -> {
                poseStack.scale(-1.0f, -1.0f, -1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(angle, new Vector3f(0, 0, 1)));
            }
            case SOUTH -> {
                poseStack.scale(-1.0f, -1.0f, -1.0f);
                poseStack.mulPose(new Quaternionf().rotateAxis(Mth.PI, new Vector3f(0, 1, 0)));
                poseStack.mulPose(new Quaternionf().rotateAxis(angle, new Vector3f(0, 0, 1)));
            }
            default -> {
            }
        }
    }
}
