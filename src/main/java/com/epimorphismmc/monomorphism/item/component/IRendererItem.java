package com.epimorphismmc.monomorphism.item.component;

import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import net.minecraft.world.item.ItemStack;

public interface IRendererItem extends IItemRendererProvider {
    ICustomRenderer getRenderInfo(ItemStack itemStack);

}
