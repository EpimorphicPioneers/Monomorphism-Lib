package com.epimorphismmc.monomorphism.client.utils;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.*;

@OnlyIn(Dist.CLIENT)
public class MORenderBufferUtils {

    /**
     * @return The render type buffer implementation
     */
    public static MultiBufferSource.BufferSource getRenderTypeBuffer() {
        return getMC().renderBuffers().bufferSource();
    }

    /**
     * Fetches a vertex builder for a RenderType, fetches the IRenderTypeBuffer internally
     * @param renderType the RenderType
     * @return an IVertexBuilder
     */
    public static VertexConsumer getVertexBuilder(RenderType renderType) {
        return getVertexBuilder(getRenderTypeBuffer(), renderType);
    }

    /**
     * Fetches a vertex builder from an IRenderTypeBuffer for a RenderType
     * @param buffer the IRenderTypeBuffer
     * @param renderType the RenderType
     * @return an IVertexBuilder
     */
    public static VertexConsumer getVertexBuilder(MultiBufferSource buffer, RenderType renderType) {
        return buffer.getBuffer(renderType);
    }

    public static void putVertex(
            VertexConsumer b,
            PoseStack mat,
            float x,
            float y,
            float z,
            float u,
            float v,
            float nX,
            float nY,
            float nZ,
            int light) {
        b.vertex(mat.last().pose(), x, y, z)
                .color(1F, 1F, 1F, 1F)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(mat.last().normal(), nX, nY, nZ)
                .endVertex();
    }
}
