package com.epimorphismmc.monomorphism.client.tessellator;

import com.epimorphismmc.monomorphism.client.model.ModelFactory;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.*;
import lombok.Getter;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import static com.epimorphismmc.monomorphism.MOValues.UNIT;
import static com.epimorphismmc.monomorphism.MOValues.WHOLE;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public abstract class TessellatorAbstractBase implements ITessellator {
    /** Current transformation matrix */
    private final PoseStack matrices;

    /** Face being drawn */
    @Getter
    private Face face;

    /** Current normal */
    private Vector3f normal;

    /** Current color */
    private float r, g, b, a;

    /** Current brightness value */
    private int l;

    /** Current overlay value */
    private int o;

    /** Current tint index for the quad */
    private int tintIndex;

    /** Current diffuse lighting setting for the quad */
    private boolean applyDiffuseLighting;

    /** Cached transformer, constructed when necessary, deconstructed when transformation state changes */
    private IQuadTransformer cachedTransformer;

    protected TessellatorAbstractBase() {
        this.matrices = new PoseStack();
        this.face = Face.NONE;
        this.normal = Defaults.NORMAL;
        this.setColorRGBA(Defaults.COLOR, Defaults.COLOR, Defaults.COLOR, Defaults.COLOR);
        this.setBrightness(Defaults.BRIGHTNESS);
        this.setOverlay(Defaults.OVERLAY);
        this.tintIndex = -1;
        this.applyDiffuseLighting = false;
    }

    private void manipulateMatrixStack(Consumer<PoseStack> operator) {
        this.cachedTransformer = null;
        operator.accept(this.matrices);
    }

    private PoseStack getMatrixStack() {
        return this.matrices;
    }

    protected PoseStack.Pose getMatrixStackEntry() {
        return this.getMatrixStack().last();
    }

    protected Matrix4f getCurrentMatrix() {
        return this.getMatrixStackEntry().pose();
    }

    protected IQuadTransformer getCachedQuadTransformer() {
        if (this.cachedTransformer == null) {
            this.cachedTransformer =
                    QuadTransformers.applying(new Transformation(this.getCurrentMatrix()));
        }
        return this.cachedTransformer;
    }

    @Override
    public final TessellatorAbstractBase startDrawingQuads() {
        this.face = Face.NONE;
        this.onStartDrawingQuadsCall();
        return this;
    }

    /**
     * Sub delegated method call of the startDrawingQuads() method to ensure
     * correct call chain
     */
    protected abstract void onStartDrawingQuadsCall();

    @Override
    public final TessellatorAbstractBase draw() {
        this.onDrawCall();
        this.normal = Defaults.NORMAL;
        this.setColorRGBA(Defaults.COLOR, Defaults.COLOR, Defaults.COLOR, Defaults.COLOR);
        this.setBrightness(Defaults.BRIGHTNESS);
        this.setOverlay(Defaults.OVERLAY);
        this.tintIndex = -1;
        this.applyDiffuseLighting = false;
        this.manipulateMatrixStack(PoseStack::clear);
        return this;
    }

    /**
     * Sub delegated method call of the draw() method to ensure correct call
     * chain
     */
    protected abstract void onDrawCall();

    @Override
    public TessellatorAbstractBase pushMatrix() {
        this.manipulateMatrixStack(PoseStack::pushPose);
        return this;
    }

    @Override
    public TessellatorAbstractBase popMatrix() {
        this.manipulateMatrixStack(PoseStack::popPose);
        return this;
    }

    @Override
    public TessellatorAbstractBase addVertexWithUV(
            float x, float y, float z, TextureAtlasSprite sprite, float u, float v) {
        if (sprite == null) {
            sprite = ModelFactory.getMissingSprite();
        }
        return this.addVertexWithUV(x, y, z, sprite.getU(u), sprite.getV(v));
    }

    @Override
    public abstract TessellatorAbstractBase addVertexWithUV(
            float x, float y, float z, float u, float v);

    @Override
    public TessellatorAbstractBase addScaledVertexWithUV(
            float x, float y, float z, float u, float v) {
        return this.addVertexWithUV(x * UNIT, y * UNIT, z * UNIT, u, v);
    }

    @Override
    public TessellatorAbstractBase addScaledVertexWithUV(
            float x, float y, float z, TextureAtlasSprite icon, float u, float v) {
        return this.addVertexWithUV(x * UNIT, y * UNIT, z * UNIT, icon, u, v);
    }

    @Override
    public TessellatorAbstractBase drawScaledFace(
            float minX, float minY, float maxX, float maxY, Direction face, float offset) {
        return this.drawScaledFace(
                minX, minY, maxX, maxY, face, offset, minX % 17, minY % 17, maxX % 17, maxY % 17);
    }

    @Override
    public TessellatorAbstractBase drawScaledFace(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            float offset,
            float u1,
            float v1,
            float u2,
            float v2) {
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        float u1f, u2f, u3f, u4f;
        float v1f, v2f, v3f, v4f;
        switch (face) {
            case UP: {
                x1 = x4 = maxX;
                x2 = x3 = minX;
                z1 = z2 = minY;
                z3 = z4 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u2f = u3f = u1;
                u1f = u4f = u2;
                v3f = v4f = v2;
                v1f = v2f = v1;
                break;
            }
            case DOWN: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = minY;
                z2 = z3 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case WEST: {
                z1 = z2 = maxX;
                z3 = z4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case EAST: {
                z1 = z2 = minX;
                z3 = z4 = maxX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1f = u2f = 16 - u1;
                u3f = u4f = 16 - u2;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case NORTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = maxY;
                y2 = y3 = minY;
                z1 = z2 = z3 = z4 = offset;
                u1f = u2f = 16 - u2;
                u3f = u4f = 16 - u1;
                v1f = v4f = 16 - v2;
                v2f = v3f = 16 - v1;
                break;
            }
            case SOUTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            default:
                return this;
        }
        float rPrev = this.r;
        float gPrev = this.g;
        float bPrev = this.b;
        float aPrev = this.a;
        this.applyColorMultiplier(face);
        this.setNormal(face.getStepX(), face.getStepY(), face.getStepZ());
        addScaledVertexWithUV(x1, y1, z1, u1f, v1f);
        addScaledVertexWithUV(x2, y2, z2, u2f, v2f);
        addScaledVertexWithUV(x3, y3, z3, u3f, v3f);
        addScaledVertexWithUV(x4, y4, z4, u4f, v4f);
        return this.setColorRGBA(rPrev, gPrev, bPrev, aPrev);
    }

    @Override
    public TessellatorAbstractBase drawScaledFace(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            TextureAtlasSprite icon,
            float offset) {
        return this.drawScaledFace(
                minX, minY, maxX, maxY, face, icon, offset, minX % 17, minY % 17, maxX % 17, maxY % 17);
    }

    @Override
    public TessellatorAbstractBase drawScaledFace(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            TextureAtlasSprite icon,
            float offset,
            float u1,
            float v1,
            float u2,
            float v2) {
        float x1, x2, x3, x4;
        float y1, y2, y3, y4;
        float z1, z2, z3, z4;
        float u1f, u2f, u3f, u4f;
        float v1f, v2f, v3f, v4f;
        switch (face) {
            case UP: {
                x1 = x4 = maxX;
                x2 = x3 = minX;
                z1 = z2 = minY;
                z3 = z4 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u2f = u3f = u1;
                u1f = u4f = u2;
                v3f = v4f = v2;
                v1f = v2f = v1;
                break;
            }
            case DOWN: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                z1 = z4 = minY;
                z2 = z3 = maxY;
                y1 = y2 = y3 = y4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case WEST: {
                z1 = z2 = maxX;
                z3 = z4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case EAST: {
                z1 = z2 = minX;
                z3 = z4 = maxX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                x1 = x2 = x3 = x4 = offset;
                u1f = u2f = 16 - u1;
                u3f = u4f = 16 - u2;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            case NORTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = maxY;
                y2 = y3 = minY;
                z1 = z2 = z3 = z4 = offset;
                u1f = u2f = 16 - u2;
                u3f = u4f = 16 - u1;
                v1f = v4f = 16 - v2;
                v2f = v3f = 16 - v1;
                break;
            }
            case SOUTH: {
                x1 = x2 = maxX;
                x3 = x4 = minX;
                y1 = y4 = minY;
                y2 = y3 = maxY;
                z1 = z2 = z3 = z4 = offset;
                u1f = u2f = u2;
                u3f = u4f = u1;
                v1f = v4f = 16 - v1;
                v2f = v3f = 16 - v2;
                break;
            }
            default:
                return this;
        }
        float rPrev = this.r;
        float gPrev = this.g;
        float bPrev = this.b;
        float aPrev = this.a;
        this.applyColorMultiplier(face);
        this.setNormal(face.getStepX(), face.getStepY(), face.getStepZ());
        addScaledVertexWithUV(x1, y1, z1, icon, u1f, v1f);
        addScaledVertexWithUV(x2, y2, z2, icon, u2f, v2f);
        addScaledVertexWithUV(x3, y3, z3, icon, u3f, v3f);
        addScaledVertexWithUV(x4, y4, z4, icon, u4f, v4f);
        return this.setColorRGBA(rPrev, gPrev, bPrev, aPrev);
    }

    @Override
    public TessellatorAbstractBase drawScaledFaceDouble(
            float minX, float minY, float maxX, float maxY, Direction face, float offset) {
        if (face == null) {
            return this;
        }
        Direction opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, offset);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, offset);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledFaceDouble(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            float offset,
            float u1,
            float v1,
            float u2,
            float v2) {
        if (face == null) {
            return this;
        }
        Direction opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, offset, u1, v1, u2, v2);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, offset, u1, v1, u2, v2);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledFaceDouble(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            TextureAtlasSprite icon,
            float offset) {
        if (face == null) {
            return this;
        }
        Direction opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, icon, offset);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledFaceDouble(
            float minX,
            float minY,
            float maxX,
            float maxY,
            Direction face,
            TextureAtlasSprite icon,
            float offset,
            float u1,
            float v1,
            float u2,
            float v2) {
        if (face == null) {
            return this;
        }
        Direction opposite = face.getOpposite();
        this.drawScaledFace(minX, minY, maxX, maxY, face, icon, offset, u1, v1, u2, v2);
        this.drawScaledFace(minX, minY, maxX, maxY, opposite, icon, offset, u1, v1, u2, v2);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledPrism(
            float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        // bottom
        drawScaledFace(minX, minZ, maxX, maxZ, Direction.DOWN, minY);
        // top
        drawScaledFace(minX, minZ, maxX, maxZ, Direction.UP, maxY);
        // north
        drawScaledFace(minX, minY, maxX, maxY, Direction.NORTH, minZ);
        // south
        drawScaledFace(minX, minY, maxX, maxY, Direction.SOUTH, maxZ);
        // west
        drawScaledFace(minZ, minY, maxZ, maxY, Direction.WEST, minX);
        // east
        drawScaledFace(minZ, minY, maxZ, maxY, Direction.EAST, maxX);

        // return
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledPrism(
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ,
            TextureAtlasSprite icon) {
        // bottom
        drawScaledFace(minX, minZ, maxX, maxZ, Direction.DOWN, icon, minY);
        // top
        drawScaledFace(minX, minZ, maxX, maxZ, Direction.UP, icon, maxY);
        // north
        drawScaledFace(minX, minY, maxX, maxY, Direction.NORTH, icon, minZ);
        // south
        drawScaledFace(minX, minY, maxX, maxY, Direction.SOUTH, icon, maxZ);
        // west
        drawScaledFace(minZ, minY, maxZ, maxY, Direction.WEST, icon, minX);
        // east
        drawScaledFace(minZ, minY, maxZ, maxY, Direction.EAST, icon, maxX);

        // return
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinder(
            float x, float y, float z, float r, float h, float vMax, int quads) {
        this.drawScaledCylinderOutside(x, y, z, r, h, vMax, quads);
        this.drawScaledCylinderInside(x, y, z, r, h, vMax, quads);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinder(
            float x,
            float y,
            float z,
            float r,
            float h,
            TextureAtlasSprite texture,
            float vMax,
            int quads) {
        this.drawScaledCylinderOutside(x, y, z, r, h, texture, vMax, quads);
        this.drawScaledCylinderInside(x, y, z, r, h, texture, vMax, quads);
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinderOutside(
            float x, float y, float z, float r, float h, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for (int i = 0; i < quads; i++) {
            double angle = ((i + 1) % quads) * 2 * Math.PI / quads;
            float newX = (float) (r * Math.cos(angle)) + x;
            float newZ = (float) (r * Math.sin(angle)) + z;
            float newU = WHOLE * ((float) (i + 1)) / quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, prevU, vMax);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, prevU, 0);
            this.addScaledVertexWithUV(newX, y + h, newZ, newU, 0);
            this.addScaledVertexWithUV(newX, y, newZ, newU, vMax);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinderOutside(
            float x,
            float y,
            float z,
            float r,
            float h,
            TextureAtlasSprite texture,
            float vMax,
            int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for (int i = 0; i < quads; i++) {
            double angle = ((i + 1) % quads) * 2 * Math.PI / quads;
            float newX = (float) (r * Math.cos(angle)) + x;
            float newZ = (float) (r * Math.sin(angle)) + z;
            float newU = WHOLE * ((float) (i + 1)) / quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, texture, prevU, vMax);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, texture, prevU, 0);
            this.addScaledVertexWithUV(newX, y + h, newZ, texture, newU, 0);
            this.addScaledVertexWithUV(newX, y, newZ, texture, newU, vMax);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinderInside(
            float x, float y, float z, float r, float h, float vMax, int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for (int i = 0; i < quads; i++) {
            double angle = ((i + 1) % quads) * 2 * Math.PI / quads;
            float newX = (float) (r * Math.cos(angle)) + x;
            float newZ = (float) (r * Math.sin(angle)) + z;
            float newU = WHOLE * ((float) (i + 1)) / quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, prevU, vMax);
            this.addScaledVertexWithUV(newX, y, newZ, newU, vMax);
            this.addScaledVertexWithUV(newX, y + h, newZ, newU, 0);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, prevU, 0);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
        return this;
    }

    @Override
    public TessellatorAbstractBase drawScaledCylinderInside(
            float x,
            float y,
            float z,
            float r,
            float h,
            TextureAtlasSprite texture,
            float vMax,
            int quads) {
        float prevX = x + r;
        float prevZ = z;
        float prevU = 0;
        for (int i = 0; i < quads; i++) {
            double angle = ((i + 1) % quads) * 2 * Math.PI / quads;
            float newX = (float) (r * Math.cos(angle)) + x;
            float newZ = (float) (r * Math.sin(angle)) + z;
            float newU = WHOLE * ((float) (i + 1)) / quads;
            this.addScaledVertexWithUV(prevX, y, prevZ, texture, prevU, vMax);
            this.addScaledVertexWithUV(newX, y, newZ, texture, newU, vMax);
            this.addScaledVertexWithUV(newX, y + h, newZ, texture, newU, 0);
            this.addScaledVertexWithUV(prevX, y + h, prevZ, texture, prevU, 0);
            prevX = newX;
            prevZ = newZ;
            prevU = newU;
        }
        return this;
    }

    @Override
    public TessellatorAbstractBase translate(BlockPos pos) {
        this.translate(pos.getX(), pos.getY(), pos.getZ());
        return this;
    }

    @Override
    public TessellatorAbstractBase translate(float x, float y, float z) {
        this.manipulateMatrixStack(stack -> stack.translate(x, y, z));
        return this;
    }

    @Override
    public TessellatorAbstractBase rotate(float angle, float x, float y, float z) {
        return this.rotate(new Quaternionf().fromAxisAngleDeg(new Vector3f(x, y, z), angle));
    }

    @Override
    public TessellatorAbstractBase rotate(Quaternionf quaternion) {
        this.manipulateMatrixStack(stack -> stack.mulPose(quaternion));
        return this;
    }

    @Override
    public TessellatorAbstractBase scale(float x, float y, float z) {
        this.manipulateMatrixStack(stack -> stack.scale(x, y, z));
        return this;
    }

    @Override
    public TessellatorAbstractBase applyTransformation(Matrix4f matrix) {
        this.manipulateMatrixStack(stack -> {
            stack.last().pose().mul(matrix);
            stack.last().normal().mul(new Matrix3f(matrix));
        });
        return this;
    }

    @Override
    public TextureAtlasSprite getIcon(Material source) {
        if (source != null) {
            return source.sprite();
        } else {
            return ModelFactory.getMissingSprite();
        }
    }

    @Override
    public TessellatorAbstractBase setFace(@Nonnull Face face) {
        this.face = face;
        if (this.getNormal() == Defaults.NORMAL) {
            this.setNormal(this.getFace().getNormal());
        }
        return this;
    }

    @Override
    public TessellatorAbstractBase setNormal(float x, float y, float z) {
        this.normal.set(x, y, z);
        return this;
    }

    @Override
    public TessellatorAbstractBase setNormal(Vector3f vec) {
        this.normal = vec;
        return this;
    }

    @Override
    public Vector3f getNormal() {
        return this.normal;
    }

    @Override
    public int getColor() {
        return ((int) (this.r * 255) << 16) | ((int) (this.g * 255) << 8) | ((int) (this.b * 255));
    }

    @Override
    public TessellatorAbstractBase setColorRGB(Vector3f color) {
        return this.setColorRGB(color.x(), color.y(), color.z());
    }

    @Override
    public TessellatorAbstractBase setColorRGB(float red, float green, float blue) {
        return this.setColorRGBA(red, green, blue, 1);
    }

    @Override
    public TessellatorAbstractBase setColorRGBA(float red, float green, float blue, float alpha) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = alpha;
        return this;
    }

    @Override
    public TessellatorAbstractBase setAlpha(float alpha) {
        this.a = alpha;
        return this;
    }

    @Override
    public float getRed() {
        return this.r;
    }

    @Override
    public float getGreen() {
        return this.g;
    }

    @Override
    public float getBlue() {
        return this.b;
    }

    @Override
    public float getAlpha() {
        return this.a;
    }

    @Override
    public TessellatorAbstractBase setBrightness(int value) {
        this.l = value;
        return this;
    }

    @Override
    public int getBrightness() {
        return this.l;
    }

    @Override
    public TessellatorAbstractBase setOverlay(int value) {
        this.o = value;
        return this;
    }

    @Override
    public int getOverlay() {
        return this.o;
    }

    @Override
    public TessellatorAbstractBase setTintIndex(int index) {
        this.tintIndex = index;
        return this;
    }

    @Override
    public int getTintIndex() {
        return this.tintIndex;
    }

    @Override
    public TessellatorAbstractBase setApplyDiffuseLighting(boolean value) {
        this.applyDiffuseLighting = value;
        return this;
    }

    @Override
    public boolean getApplyDiffuseLighting() {
        return this.applyDiffuseLighting;
    }

    protected abstract void applyColorMultiplier(Direction side);

    @Override
    public final TessellatorAbstractBase transform(Vector4f pos) {
        pos.mul(this.getCurrentMatrix());
        return this;
    }

    public final BakedQuad transformQuads(BakedQuad quad) {
        return this.getCachedQuadTransformer().process(quad);
    }

    public final List<BakedQuad> transformQuads(List<BakedQuad> quad) {
        return this.getCachedQuadTransformer().process(quad);
    }

    public final void transformUnpackedVertexDataElement(
            VertexFormatElement.Usage type, float[] data) {
        switch (type) {
            case POSITION:
            case NORMAL:
                Vector4f temp = new Vector4f(data[0], data[1], data[2], 1);
                temp.mul(this.getCurrentMatrix());
                data[0] = temp.x();
                data[1] = temp.y();
                data[2] = temp.z();
                break;
            case COLOR:
                data[0] = getRed();
                data[1] = getGreen();
                data[2] = getBlue();
                data[3] = getAlpha();
                break;
        }
    }
}
