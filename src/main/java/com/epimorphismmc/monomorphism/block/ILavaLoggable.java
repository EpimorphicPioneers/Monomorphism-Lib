package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.block.property.MOProperty;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface ILavaLoggable extends BucketPickup, LiquidBlockContainer {
    @Override
    default boolean canPlaceLiquid(BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return !MOProperty.Defaults.lavalogged().fetch(state) && fluid == Fluids.LAVA;
    }

    @Override
    default boolean placeLiquid(
            LevelAccessor world, BlockPos pos, BlockState state, FluidState fluid) {
        if (this.canPlaceLiquid(world, pos, state, fluid.getType())) {
            if (!world.isClientSide()) {
                world.setBlock(pos, MOProperty.Defaults.lavalogged().apply(state, true), 3);
                world.scheduleTick(pos, fluid.getType(), fluid.getType().getTickDelay(world));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    default ItemStack pickupBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        if (MOProperty.Defaults.lavalogged().fetch(state)) {
            world.setBlock(pos, MOProperty.Defaults.lavalogged().apply(state, false), 3);
            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    default Optional<SoundEvent> getPickupSound() {
        return Fluids.LAVA.getPickupSound();
    }
}
