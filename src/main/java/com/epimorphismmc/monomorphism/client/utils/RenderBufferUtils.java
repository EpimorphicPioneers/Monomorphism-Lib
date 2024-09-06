package com.epimorphismmc.monomorphism.client.utils;

import com.lowdragmc.lowdraglib.client.utils.RenderUtils;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;

import javax.annotation.Nonnull;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.*;

@OnlyIn(Dist.CLIENT)
public class RenderBufferUtils {

    /**
     * @return The MultiBufferSource.BufferSource
     */
    public static MultiBufferSource.BufferSource getBufferSource() {
        return mc().renderBuffers().bufferSource();
    }

    /**
     * Fetches a vertex builder for a RenderType, fetches the BufferSource internally
     * @param renderType the RenderType
     * @return a VertexConsumer
     */
    public static VertexConsumer getVertexBuilder(RenderType renderType) {
        return getBufferSource().getBuffer(renderType);
    }

    public static void renderCubeFrame(
            PoseStack poseStack,
            BufferBuilder buffer,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            float r,
            float g,
            float b,
            float a) {
        var mat = poseStack.last().pose();
        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).normal(1, 0, 0).endVertex();
        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).normal(1, 0, 0).endVertex();

        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).normal(0, 1, 0).endVertex();

        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).normal(0, 0, 1).endVertex();

        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).normal(1, 0, 0).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).normal(1, 0, 0).endVertex();

        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).normal(0, 1, 0).endVertex();

        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).normal(0, 0, 1).endVertex();

        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).normal(0, 0, 1).endVertex();

        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).normal(1, 0, 0).endVertex();
        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).normal(1, 0, 0).endVertex();

        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).normal(0, 0, 1).endVertex();
        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).normal(0, 0, 1).endVertex();

        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).normal(0, 1, 0).endVertex();

        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).normal(1, 0, 0).endVertex();
        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).normal(1, 0, 0).endVertex();

        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).normal(0, 1, 0).endVertex();
    }

    public static void renderCubeFace(
            PoseStack poseStack,
            BufferBuilder buffer,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            float red,
            float green,
            float blue,
            float a,
            boolean shade) {
        Matrix4f mat = poseStack.last().pose();
        float r = red, g = green, b = blue;

        if (shade) {
            r *= 0.6;
            g *= 0.6;
            b *= 0.6;
        }
        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).endVertex();

        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).endVertex();

        if (shade) {
            r = red * 0.5f;
            g = green * 0.5f;
            b = blue * 0.5f;
        }
        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).endVertex();

        if (shade) {
            r = red;
            g = green;
            b = blue;
        }
        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).endVertex();

        if (shade) {
            r = red * 0.8f;
            g = green * 0.8f;
            b = blue * 0.8f;
        }
        buffer.vertex(mat, minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, minY, minZ).color(r, g, b, a).endVertex();

        buffer.vertex(mat, minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.vertex(mat, minX, maxY, maxZ).color(r, g, b, a).endVertex();
    }

    public static void renderCubeFace(
            PoseStack poseStack,
            VertexConsumer buffer,
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            int color,
            int combinedLight,
            TextureAtlasSprite textureSprite) {
        Matrix4f mat = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();

        buffer
                .vertex(mat, minX, minY, minZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, -1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, minX, minY, maxZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, -1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, minX, maxY, maxZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, -1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, minX, maxY, minZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, -1, 0, 0)
                .endVertex();

        buffer
                .vertex(mat, maxX, minY, minZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, minZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, maxZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 1, 0, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, minY, maxZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 1, 0, 0)
                .endVertex();

        buffer
                .vertex(mat, minX, minY, minZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, -1, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, minY, minZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, -1, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, minY, maxZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, -1, 0)
                .endVertex();
        buffer
                .vertex(mat, minX, minY, maxZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, -1, 0)
                .endVertex();

        buffer
                .vertex(mat, minX, maxY, minZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 1, 0)
                .endVertex();
        buffer
                .vertex(mat, minX, maxY, maxZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 1, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, maxZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 1, 0)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, minZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 1, 0)
                .endVertex();

        buffer
                .vertex(mat, minX, minY, minZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, -1)
                .endVertex();
        buffer
                .vertex(mat, minX, maxY, minZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, -1)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, minZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, -1)
                .endVertex();
        buffer
                .vertex(mat, maxX, minY, minZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, -1)
                .endVertex();

        buffer
                .vertex(mat, minX, minY, maxZ)
                .color(color)
                .uv(uMin, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, 1)
                .endVertex();
        buffer
                .vertex(mat, maxX, minY, maxZ)
                .color(color)
                .uv(uMax, vMax)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, 1)
                .endVertex();
        buffer
                .vertex(mat, maxX, maxY, maxZ)
                .color(color)
                .uv(uMax, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, 1)
                .endVertex();
        buffer
                .vertex(mat, minX, maxY, maxZ)
                .color(color)
                .uv(uMin, vMin)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(combinedLight)
                .normal(normal, 0, 0, 1)
                .endVertex();
    }

    public static void drawColorLines(
            @Nonnull PoseStack poseStack,
            VertexConsumer builder,
            List<Vec2> points,
            int colorStart,
            int colorEnd,
            float width) {
        if (points.size() < 2) return;
        Matrix4f mat = poseStack.last().pose();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3f vec = null;
        int sa = (colorStart >> 24) & 0xff,
                sr = (colorStart >> 16) & 0xff,
                sg = (colorStart >> 8) & 0xff,
                sb = colorStart & 0xff;
        int ea = (colorEnd >> 24) & 0xff,
                er = (colorEnd >> 16) & 0xff,
                eg = (colorEnd >> 8) & 0xff,
                eb = colorEnd & 0xff;
        ea = (ea - sa);
        er = (er - sr);
        eg = (eg - sg);
        eb = (eb - sb);
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1f) / points.size();
            float e = i * 1f / points.size();
            point = points.get(i);
            vec = new Vector3f(point.x - lastPoint.x, point.y - lastPoint.y, 0)
                    .rotateZ(Mth.HALF_PI)
                    .normalize()
                    .mul(-width);
            builder
                    .vertex(mat, lastPoint.x + vec.x, lastPoint.y + vec.y, 0)
                    .color((sr + er * s) / 255, (sg + eg * s) / 255, (sb + eb * s) / 255, (sa + ea * s) / 255)
                    .endVertex();
            vec.mul(-1);
            builder
                    .vertex(mat, lastPoint.x + vec.x, lastPoint.y + vec.y, 0)
                    .color((sr + er * e) / 255, (sg + eg * e) / 255, (sb + eb * e) / 255, (sa + ea * e) / 255)
                    .endVertex();
            lastPoint = point;
        }
        vec.mul(-1);
        builder
                .vertex(mat, point.x + vec.x, point.y + vec.y, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
        vec.mul(-1);
        builder
                .vertex(mat, point.x + vec.x, point.y + vec.y, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
    }

    public static void drawColorTexLines(
            @Nonnull PoseStack poseStack,
            VertexConsumer builder,
            List<Vec2> points,
            int colorStart,
            int colorEnd,
            float width) {
        if (points.size() < 2) return;
        Matrix4f mat = poseStack.last().pose();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3f vec = null;
        int sa = (colorStart >> 24) & 0xff,
                sr = (colorStart >> 16) & 0xff,
                sg = (colorStart >> 8) & 0xff,
                sb = colorStart & 0xff;
        int ea = (colorEnd >> 24) & 0xff,
                er = (colorEnd >> 16) & 0xff,
                eg = (colorEnd >> 8) & 0xff,
                eb = colorEnd & 0xff;
        ea = (ea - sa);
        er = (er - sr);
        eg = (eg - sg);
        eb = (eb - sb);
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1f) / points.size();
            float e = i * 1f / points.size();
            point = points.get(i);
            float u = (i - 1f) / points.size();
            vec = new Vector3f(point.x - lastPoint.x, point.y - lastPoint.y, 0)
                    .rotateZ(Mth.HALF_PI)
                    .normalize()
                    .mul(-width);
            builder
                    .vertex(mat, lastPoint.x + vec.x, lastPoint.y + vec.y, 0)
                    .uv(u, 0)
                    .color((sr + er * s) / 255, (sg + eg * s) / 255, (sb + eb * s) / 255, (sa + ea * s) / 255)
                    .endVertex();
            vec.mul(-1);
            builder
                    .vertex(mat, lastPoint.x + vec.x, lastPoint.y + vec.y, 0)
                    .uv(u, 1)
                    .color((sr + er * e) / 255, (sg + eg * e) / 255, (sb + eb * e) / 255, (sa + ea * e) / 255)
                    .endVertex();
            lastPoint = point;
        }
        vec.mul(-1);
        builder
                .vertex(mat, point.x + vec.x, point.y + vec.y, 0)
                .uv(1, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
        vec.mul(-1);
        builder
                .vertex(mat, point.x + vec.x, point.y + vec.y, 0)
                .uv(1, 1)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
    }

    public static void renderBlockOverLay(
            @Nonnull PoseStack poseStack, BlockPos pos, float r, float g, float b, float scale) {
        if (pos == null) return;
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        poseStack.pushPose();
        poseStack.translate((pos.getX() + 0.5), (pos.getY() + 0.5), (pos.getZ() + 0.5));
        poseStack.scale(scale, scale, scale);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        RenderUtils.renderCubeFace(
                poseStack, buffer, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, r, g, b, 1);
        tessellator.end();

        poseStack.popPose();

        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
