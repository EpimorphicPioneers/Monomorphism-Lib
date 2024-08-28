package com.epimorphismmc.monomorphism.client.tessellator;

import com.epimorphismmc.monomorphism.client.model.ModelFactory;

import com.epimorphismmc.monomorphism.client.model.pipeline.VertexData;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Helper class to construct vertices
 */
@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class TessellatorBakedQuad extends TessellatorAbstractBase {
    /** Draw mode when no vertices are being constructed */
    public static final int DRAW_MODE_NOT_DRAWING = -1;

    /** Draw mode when vertices are being constructed for quads */
    public static final int DRAW_MODE_QUADS = 4;

    /** Currently constructed quads */
    private final List<BakedQuad> quads;

    /** Currently constructed vertices */
    private final List<VertexData> vertexData;

    /** Current drawing mode */
    private int drawMode;

    /** Icon currently drawing with */
    private TextureAtlasSprite icon;

    /** Texture function */
    private Function<Material, TextureAtlasSprite> textureFunction;

    /**
     * constructor
     */
    public TessellatorBakedQuad() {
        super();
        this.quads = new ArrayList<>();
        this.vertexData = new ArrayList<>();
        this.drawMode = DRAW_MODE_NOT_DRAWING;
    }

    /**
     * Sub delegated method call of the startDrawingQuads() method to ensure
     * correct call chain
     */
    @Override
    protected void onStartDrawingQuadsCall() {
        this.startDrawing(DRAW_MODE_QUADS);
    }

    /**
     * Method to start constructing vertices
     *
     * @param mode draw mode
     */
    public void startDrawing(int mode) {
        if (drawMode == DRAW_MODE_NOT_DRAWING) {
            this.drawMode = mode;
        } else {
            throw new RuntimeException("ALREADY CONSTRUCTING VERTICES");
        }
    }

    /**
     * Method to get all quads constructed
     *
     * @return list of quads, may be empty but never null
     */
    @Override
    public ImmutableList<BakedQuad> getQuads() {
        return ImmutableList.copyOf(this.quads);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return DefaultVertexFormat.BLOCK;
    }

    /**
     * Sub delegated method call of the draw() method to ensure correct call
     * chain
     */
    @Override
    protected void onDrawCall() {
        if (drawMode != DRAW_MODE_NOT_DRAWING) {
            quads.clear();
            vertexData.clear();
            this.drawMode = DRAW_MODE_NOT_DRAWING;
            this.textureFunction = null;
        } else {
            throw new RuntimeException("NOT CONSTRUCTING VERTICES");
        }
    }

    /**
     * Adds a list of quads to be rendered
     *
     * @param quads list of quads
     */
    @Override
    public TessellatorBakedQuad addQuads(List<BakedQuad> quads) {
        if (drawMode != DRAW_MODE_NOT_DRAWING) {
            for (BakedQuad quad : quads) {
                final BakedQuad trans = transformQuads(quad);
                if (this.getFace().accepts(trans.getDirection())) {
                    this.quads.add(trans);
                }
            }
        } else {
            throw new RuntimeException("NOT CONSTRUCTING VERTICES");
        }
        return this;
    }

    /**
     * Adds a vertex
     *
     * @param x the x-coordinate for the vertex
     * @param y the y-coordinate for the vertex
     * @param z the z-coordinate for the vertex
     * @param sprite the icon
     * @param u u value for the vertex
     * @param v v value for the vertex
     */
    @Override
    public TessellatorBakedQuad addVertexWithUV(
            float x, float y, float z, TextureAtlasSprite sprite, float u, float v) {
        if (sprite == null) {
            sprite = ModelFactory.getMissingSprite();
        }
        this.icon = sprite;
        return this.addVertexWithUV(x, y, z, sprite.getU(u), sprite.getV(v));
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
    public TessellatorBakedQuad addVertexWithUV(float x, float y, float z, float u, float v) {
        if (this.drawMode == DRAW_MODE_NOT_DRAWING) {
            throw new RuntimeException("NOT CONSTRUCTING VERTICES");
        }

        // Create and transform the point.
        final Vector4f pos = new Vector4f(x, y, z, 1);
        this.transform(pos);

        // Create the new vertex data element.
        final VertexData vert = new VertexData(getVertexFormat());
        vert.setXYZ(pos.x(), pos.y(), pos.z());
        vert.setUV(u, v);
        vert.setRGBA(this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
        vert.setNormal(this.getNormal().x(), this.getNormal().y(), this.getNormal().z());
        this.vertexData.add(vert);

        if (this.vertexData.size() == this.drawMode) {
            final Direction dir = Direction.getNearest(
                    this.getNormal().x(), this.getNormal().y(), this.getNormal().z());
            if (this.getFace().accepts(dir)) {
                QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(quads::add);
                builder.setTintIndex(this.getTintIndex());
                builder.setShade(this.getApplyDiffuseLighting());
                builder.setDirection(dir);
                builder.setSprite(this.icon);
                for (VertexData vertex : this.vertexData) {
                    vertex.applyVertexData(builder);
                    builder.endVertex();
                }
            }
            vertexData.clear();
        }
        return this;
    }

    @Override
    public TessellatorBakedQuad drawScaledFace(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            TextureAtlasSprite icon,
            float offset) {
        if (this.getFace().accepts(face)) {
            super.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset);
        }
        return this;
    }

    @Override
    public TextureAtlasSprite getIcon(Material source) {
        if (this.textureFunction == null || source == null) {
            return super.getIcon(source);
        } else {
            return this.textureFunction.apply(source);
        }
    }

    @Override
    protected void applyColorMultiplier(Direction side) {}

    public TessellatorBakedQuad setTextureFunction(Function<Material, TextureAtlasSprite> function) {
        this.textureFunction = function;
        return this;
    }
}
