package com.epimorphismmc.monomorphism.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import javax.annotation.Nonnull;

public interface IMOBlock {

    /**
     * Retrieves the block's item form, if the block has one.
     *
     * @param <T>
     * @return an optional containing the block's item form, or the empty optional.
     */
    @Nonnull
    default <T extends BlockItem> Optional<T> getBlockItem() {
        return Optional.empty();
    }

    default void spawnItem(Level world, BlockPos pos, ItemStack stack) {
        if(world == null || world.isClientSide()) {
            return;
        }
        world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
    }

}
