package com.epimorphismmc.monomorphism.mixins.accessors;

import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(value = ItemStackTransfer.class, remap = false)
public interface ItemStackTransferAccessor {

    @Accessor
    Function<ItemStack, Boolean> getFilter();
}
