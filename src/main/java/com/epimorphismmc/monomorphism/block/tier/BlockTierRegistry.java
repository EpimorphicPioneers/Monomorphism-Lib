package com.epimorphismmc.monomorphism.block.tier;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockTierRegistry<T extends IBlockTier> {

    public void register(T type, Block block) {

    }

    public IBlockTier getTier(Block block) {
        return null;
    }

    public boolean handleTooltip(@NotNull ItemStack itemStack, @Nullable Player player, List<Component> list, TooltipFlag flags) {
        return false;
    }

}
