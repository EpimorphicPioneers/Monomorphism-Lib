package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.blockentity.IMOBlockEntity;
import com.epimorphismmc.monomorphism.blockentity.IMOBlockEntityType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

import java.util.function.BiFunction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IMOEntityBlock<T extends BlockEntity & IMOBlockEntity>
        extends IMOBlock, IForgeBlock, EntityBlock {
    @Override
    @Nullable default T newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return this.getTileEntityFactory().apply(pos, state);
    }

    BiFunction<BlockPos, BlockState, T> getTileEntityFactory();

    @Nullable @Override
    default <TE extends BlockEntity> BlockEntityTicker<TE> getTicker(
            Level level, BlockState state, BlockEntityType<TE> type) {
        if (type instanceof IMOBlockEntityType) {
            if (((IMOBlockEntityType) type).isTicking()) {
                return (l, p, s, t) -> {
                    if (t instanceof IMOBlockEntity) {
                        ((IMOBlockEntity) t).tick();
                    }
                };
            }
        }
        return EntityBlock.super.getTicker(level, state, type);
    }
}
