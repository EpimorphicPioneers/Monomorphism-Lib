package com.epimorphismmc.monomorphism.mixins.gtm;

import com.epimorphismmc.monomorphism.integration.gtm.item.IMOItemRendererProvider;

import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = ComponentItem.class, remap = false)
public abstract class ComponentItemMixin extends Item implements IMOItemRendererProvider {
    @Shadow
    protected List<IItemComponent> components;

    public ComponentItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public ICustomRenderer getRenderInfo(ItemStack itemStack) {
        for (IItemComponent component : components) {
            if (component instanceof ICustomRenderer customRenderer) {
                return customRenderer;
            }
        }
        return null;
    }
}
