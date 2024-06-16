package com.epimorphismmc.monomorphism.item;

import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;

import net.minecraft.world.item.ItemStack;

public interface IMOItemRendererProvider extends IItemRendererProvider {
    ICustomRenderer getRenderInfo(ItemStack itemStack);
}
