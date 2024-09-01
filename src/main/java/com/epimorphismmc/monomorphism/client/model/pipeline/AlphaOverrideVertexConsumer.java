package com.epimorphismmc.monomorphism.client.model.pipeline;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.VertexConsumerWrapper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AlphaOverrideVertexConsumer extends VertexConsumerWrapper {
    private final int alpha;

    public AlphaOverrideVertexConsumer(VertexConsumer inner, int alpha) {
        super(inner);
        this.alpha = alpha;
    }

    public AlphaOverrideVertexConsumer(VertexConsumer inner, double alpha) {
        super(inner);
        this.alpha = (int) (alpha * 255F);
    }

    @Override
    public void putBulkData(
            PoseStack.Pose arg,
            BakedQuad arg2,
            float[] fs,
            float g,
            float h,
            float m,
            float is,
            int[] n,
            int bl,
            boolean bl2) {
        parent.putBulkData(arg, arg2, fs, g, h, m, this.alpha / 255.0F, n, bl, bl2);
    }

    @Override
    public void vertex(
            float x,
            float y,
            float z,
            float red,
            float green,
            float blue,
            float alpha,
            float texU,
            float texV,
            int overlayUV,
            int lightmapUV,
            float normalX,
            float normalY,
            float normalZ) {
        parent.vertex(
                x,
                y,
                z,
                red,
                green,
                blue,
                this.alpha / 255.0F,
                texU,
                texV,
                overlayUV,
                lightmapUV,
                normalX,
                normalY,
                normalZ);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        parent.color(red, green, blue, this.alpha);
        return this;
    }
}
