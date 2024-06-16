package com.epimorphismmc.monomorphism.client.utils;

import com.epimorphismmc.monomorphism.utility.MOFluidUtils;

import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

/*
 * Referenced some code from Mekanism
 *
 * https://github.com/mekanism/Mekanism/
 * */

public class Model3D {
    public float minX, minY, minZ;
    public float maxX, maxY, maxZ;

    private final SpriteInfo[] textures = new SpriteInfo[6];
    private final boolean[] renderSides = {true, true, true, true, true, true};

    public Model3D setSideRender(Predicate<Direction> shouldRender) {
        for (Direction direction : GTUtil.DIRECTIONS) {
            setSideRender(direction, shouldRender.test(direction));
        }
        return this;
    }

    public Model3D setSideRender(Direction side, boolean value) {
        renderSides[side.ordinal()] = value;
        return this;
    }

    public Model3D copy() {
        Model3D copy = new Model3D();
        System.arraycopy(textures, 0, copy.textures, 0, textures.length);
        System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
        return copy.bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Nullable public Model3D.SpriteInfo getSpriteToRender(Direction side) {
        int ordinal = side.ordinal();
        return renderSides[ordinal] ? textures[ordinal] : null;
    }

    public Model3D shrink(float amount) {
        return grow(-amount);
    }

    public Model3D grow(float amount) {
        return bounds(
                minX - amount, minY - amount, minZ - amount, maxX + amount, maxY + amount, maxZ + amount);
    }

    public Model3D xBounds(float min, float max) {
        this.minX = min;
        this.maxX = max;
        return this;
    }

    public Model3D yBounds(float min, float max) {
        this.minY = min;
        this.maxY = max;
        return this;
    }

    public Model3D zBounds(float min, float max) {
        this.minZ = min;
        this.maxZ = max;
        return this;
    }

    public Model3D bounds(float min, float max) {
        return bounds(min, min, min, max, max, max);
    }

    public Model3D bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return xBounds(minX, maxX).yBounds(minY, maxY).zBounds(minZ, maxZ);
    }

    public Model3D prepSingleFaceModelSize(Direction face) {
        bounds(0, 1);
        return switch (face) {
            case DOWN -> yBounds(-0.01F, -0.001F);
            case UP -> yBounds(1.001F, 1.01F);
            case NORTH -> zBounds(-0.01F, -0.001F);
            case SOUTH -> zBounds(1.001F, 1.01F);
            case WEST -> xBounds(-0.01F, -0.001F);
            case EAST -> xBounds(1.001F, 1.01F);
        };
    }

    public Model3D prepFlowing(@NotNull FluidStack fluid) {
        SpriteInfo still = new SpriteInfo(FluidHelper.getStillTexture(fluid), 16);
        SpriteInfo flowing = new SpriteInfo(MOFluidUtils.getFlowingTexture(fluid), 8);
        return setTextures(still, still, flowing, flowing, flowing, flowing);
    }

    public Model3D prepStill(@NotNull FluidStack fluid) {
        SpriteInfo still = new SpriteInfo(FluidHelper.getStillTexture(fluid), 16);
        return setTextures(still, still, still, still, still, still);
    }

    public Model3D setTexture(Direction side, @Nullable Model3D.SpriteInfo spriteInfo) {
        textures[side.ordinal()] = spriteInfo;
        return this;
    }

    public Model3D setTexture(TextureAtlasSprite tex) {
        return setTexture(tex, 16);
    }

    public Model3D setTexture(TextureAtlasSprite tex, int size) {
        Arrays.fill(textures, new SpriteInfo(tex, size));
        return this;
    }

    public Model3D setTextures(
            SpriteInfo down,
            SpriteInfo up,
            SpriteInfo north,
            SpriteInfo south,
            SpriteInfo west,
            SpriteInfo east) {
        textures[0] = down;
        textures[1] = up;
        textures[2] = north;
        textures[3] = south;
        textures[4] = west;
        textures[5] = east;
        return this;
    }

    public record SpriteInfo(TextureAtlasSprite sprite, int size) {

        public float getU(float u) {
            return sprite.getU(u * size);
        }

        public float getV(float v) {
            return sprite.getV(v * size);
        }
    }

    public interface ModelBoundsSetter {

        Model3D set(float min, float max);
    }

    public static class LazyModel implements Supplier<Model3D> {

        private final Supplier<Model3D> supplier;

        @Nullable private Model3D model;

        public LazyModel(Supplier<Model3D> supplier) {
            this.supplier = supplier;
        }

        public void reset() {
            model = null;
        }

        @Override
        public Model3D get() {
            if (model == null) {
                model = supplier.get();
            }
            return model;
        }
    }
}
