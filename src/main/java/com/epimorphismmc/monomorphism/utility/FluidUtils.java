package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.client.model.ModelFactory;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import org.jetbrains.annotations.Nullable;

public class FluidUtils {

    /**
     * The representation of 1 bucket(b) in millibuckets(mB).
     */
    public static final int BUCKET = FluidType.BUCKET_VOLUME;

    /**
     * The representation of 1/2 a bucket(b) in millibuckets(mB).
     */
    public static final int HALF_BUCKET = BUCKET / 2;

    /**
     * The representation of 1/4 a bucket(b) in millibuckets(mB).
     */
    public static final int QUARTER_BUCKET = BUCKET / 4;

    public static final int INGOT = 144;

    public static final String UNIT_SUFFIX = "mB";

    @OnlyIn(Dist.CLIENT)
    public static @Nullable TextureAtlasSprite getStillTexture(FluidStack fluidStack) {
        var texture = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getStillTexture(fluidStack);
        return texture == null ? null : ModelFactory.getBlockSprite(texture);
    }

    @OnlyIn(Dist.CLIENT)
    public static @Nullable TextureAtlasSprite getFlowingTexture(FluidStack fluidStack) {
        var texture =
                IClientFluidTypeExtensions.of(fluidStack.getFluid()).getFlowingTexture(fluidStack);
        return texture == null ? null : ModelFactory.getBlockSprite(texture);
    }

    public static int getColor(FluidStack fluidStack) {
        return IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
    }

    public static Component getDisplayName(FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getDescription(fluidStack);
    }

    public static int getTemperature(FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getTemperature(fluidStack);
    }

    public static boolean isLighterThanAir(FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().isLighterThanAir();
    }

    public static boolean canBePlacedInWorld(
            FluidStack fluidStack, BlockAndTintGetter level, BlockPos pos) {
        return fluidStack.getFluid().getFluidType().canBePlacedInLevel(level, pos, fluidStack);
    }

    public static boolean doesVaporize(FluidStack fluidStack, Level level, BlockPos pos) {
        return fluidStack.getFluid().getFluidType().isVaporizedOnPlacement(level, pos, fluidStack);
    }

    public static @Nullable SoundEvent getEmptySound(FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getSound(fluidStack, SoundActions.BUCKET_EMPTY);
    }

    public static @Nullable SoundEvent getFillSound(FluidStack fluidStack) {
        return fluidStack.getFluid().getFluidType().getSound(fluidStack, SoundActions.BUCKET_FILL);
    }
}
