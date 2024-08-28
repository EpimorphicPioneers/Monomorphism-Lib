package com.epimorphismmc.monomorphism.client.tessellator;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import lombok.Getter;
import org.joml.Vector4f;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class TessellatorVertexBuffer extends TessellatorAbstractBase {
    private final MultiBufferSource.BufferSource buffer;
    /**
     * -- GETTER --
     *
     * @return The RenderType this tessellator is currently drawing with
     */
    @Getter
    private final RenderType renderType;

    private VertexConsumer builder;

    public TessellatorVertexBuffer(MultiBufferSource.BufferSource buffer, RenderType renderType) {
        this.buffer = buffer;
        this.renderType = renderType;
    }

    /**
     * @return IRenderTypeBuffer.Impl object which this is currently tessellating vertices for
     */
    public MultiBufferSource.BufferSource getVertexBuffer() {
        return this.buffer;
    }

    /**
     * Sub delegated method call of the startDrawingQuads() method to ensure
     * correct call chain
     */
    @Override
    protected void onStartDrawingQuadsCall() {
        this.builder = this.getVertexBuffer().getBuffer(this.getRenderType());
    }

    /**
     * Method to get all quads constructed.
     *
     * @return emtpy list, no quads are constructed here
     */
    @Override
    public ImmutableList<BakedQuad> getQuads() {
        return ImmutableList.of();
    }

    @Override
    public VertexFormat getVertexFormat() {
        return this.getRenderType().format();
    }

    /**
     * Sub delegated method call of the draw() method to ensure correct call
     * chain.
     */
    @Override
    protected void onDrawCall() {
        if (this.builder != null) {
            this.getVertexBuffer().endBatch(this.getRenderType());
            this.builder = null;
        }
    }

    /**
     * Adds a list of quads to be rendered
     *
     * @param quads list of quads
     */
    @Override
    public TessellatorVertexBuffer addQuads(List<BakedQuad> quads) {
        quads.forEach(quad -> this.builder.putBulkData(
                this.getMatrixStackEntry(),
                quad,
                this.getRed(),
                this.getBlue(),
                this.getGreen(),
                this.getBrightness(),
                OverlayTexture.NO_OVERLAY));
        return this;
    }

    /**
     * Adds a vertex
     *
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param u u value for the vertex
     * @param v v value for the vertex
     */
    @Override
    public TessellatorVertexBuffer addVertexWithUV(float x, float y, float z, float u, float v) {
        final Vector4f pos = new Vector4f(x, y, z, 1);
        this.transform(pos);
        List<VertexFormatElement> elements = this.getVertexFormat().getElements();
        // Note: the order this vertex data is defined is important
        for (VertexFormatElement element : elements) {
            if (element == DefaultVertexFormat.ELEMENT_POSITION) {
                builder.vertex(pos.x(), pos.y(), pos.z());
            } else if (element == DefaultVertexFormat.ELEMENT_COLOR) {
                builder.color(
                        (int) (this.getRed() * 255),
                        (int) (this.getGreen() * 255),
                        (int) (this.getBlue() * 255),
                        (int) (this.getAlpha() * 255));
            } else if (element == DefaultVertexFormat.ELEMENT_UV0) {
                builder.uv(u, v);
            } else if (element == DefaultVertexFormat.ELEMENT_UV1) {
                builder.overlayCoords(this.getOverlay());
            } else if (element == DefaultVertexFormat.ELEMENT_UV2) {
                builder.uv2(this.getBrightness());
            } else if (element == DefaultVertexFormat.ELEMENT_NORMAL) {
                builder.normal(
                        this.getNormal().x(), this.getNormal().y(), this.getNormal().z());
            }
        }
        builder.endVertex();
        return this;
    }

    @Override
    protected void applyColorMultiplier(Direction side) {
        // I gave up on transforming the side. This probably is faster anyway...
        final float preMultiplier = getMultiplier(side);
        this.setColorRGB(
                preMultiplier * this.getRed(),
                preMultiplier * this.getGreen(),
                preMultiplier * this.getBlue());
    }

    private float getMultiplier(Direction side) {
        return switch (side) {
            case DOWN -> 0.5F;
            case NORTH, SOUTH -> 0.8F;
            case EAST, WEST -> 0.6F;
            default -> 1;
        };
    }
}
