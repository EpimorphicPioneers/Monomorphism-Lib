package com.epimorphismmc.monomorphism.client.utils;

import com.epimorphismmc.monomorphism.utility.MOFluidUtils;

import com.gregtechceu.gtceu.utils.GTUtil;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
@NoArgsConstructor
@AllArgsConstructor
public class Cuboid {
    public float minX, minY, minZ;
    public float maxX, maxY, maxZ;

    public final TextureAtlasSprite[] textures =
            new TextureAtlasSprite[6]; // down, up, north, south, west, east
    public final int[] colors = {0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF
    }; // down, up, north, south, west, east
    public final boolean[] renderSides = {true, true, true, true, true, true
    }; // down, up, north, south, west, east

    public Cuboid shrink(float amount) {
        return grow(-amount);
    }

    public Cuboid grow(float amount) {
        return bounds(
                minX - amount, minY - amount, minZ - amount, maxX + amount, maxY + amount, maxZ + amount);
    }

    public Cuboid xBounds(float min, float max) {
        this.minX = min;
        this.maxX = max;
        return this;
    }

    public Cuboid yBounds(float min, float max) {
        this.minY = min;
        this.maxY = max;
        return this;
    }

    public Cuboid zBounds(float min, float max) {
        this.minZ = min;
        this.maxZ = max;
        return this;
    }

    public Cuboid bounds(float min, float max) {
        return bounds(min, min, min, max, max, max);
    }

    public Cuboid bounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return xBounds(minX, maxX).yBounds(minY, maxY).zBounds(minZ, maxZ);
    }

    public Cuboid prepSingleFaceModelSize(Direction face) {
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

    public Cuboid setSideRender(Predicate<Direction> shouldRender) {
        for (Direction direction : GTUtil.DIRECTIONS) {
            setSideRender(direction, shouldRender.test(direction));
        }
        return this;
    }

    public Cuboid setSideRender(Direction side, boolean value) {
        renderSides[side.ordinal()] = value;
        return this;
    }

    public Cuboid setColor(Direction side, int color) {
        colors[side.ordinal()] = color;
        return this;
    }

    public Cuboid setColor(int color) {
        Arrays.fill(colors, color);
        return this;
    }

    public int getColor(Direction side) {
        return colors[side.ordinal()];
    }

    public @Nullable TextureAtlasSprite getSprite(Direction side) {
        int ordinal = side.ordinal();
        return renderSides[ordinal] ? textures[ordinal] : null;
    }

    // TODO The method FluidHelper.getStillTexture(fluid) should be incorporated into LDLib.
    public Cuboid prepFlowing(@NotNull FluidStack fluid) {
        TextureAtlasSprite still = FluidHelper.getStillTexture(fluid);
        TextureAtlasSprite flowing = MOFluidUtils.getFlowingTexture(fluid);
        return setTextures(still, still, flowing, flowing, flowing, flowing);
    }

    public Cuboid prepStill(@NotNull FluidStack fluid) {
        TextureAtlasSprite still = FluidHelper.getStillTexture(fluid);
        return setTextures(still, still, still, still, still, still);
    }

    public Cuboid setTexture(Direction side, @Nullable TextureAtlasSprite sprite) {
        textures[side.ordinal()] = sprite;
        return this;
    }

    public Cuboid setTexture(TextureAtlasSprite sprite) {
        Arrays.fill(textures, sprite);
        return this;
    }

    public Cuboid setTextures(
            TextureAtlasSprite down,
            TextureAtlasSprite up,
            TextureAtlasSprite north,
            TextureAtlasSprite south,
            TextureAtlasSprite west,
            TextureAtlasSprite east) {
        textures[0] = down;
        textures[1] = up;
        textures[2] = north;
        textures[3] = south;
        textures[4] = west;
        textures[5] = east;
        return this;
    }

    public Cuboid copy() {
        Cuboid copy = new Cuboid(minX, minY, minZ, maxX, maxY, maxZ);
        System.arraycopy(textures, 0, copy.textures, 0, textures.length);
        System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
        return copy;
    }
}
