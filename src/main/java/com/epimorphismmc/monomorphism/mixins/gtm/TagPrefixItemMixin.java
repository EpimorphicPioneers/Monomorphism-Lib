package com.epimorphismmc.monomorphism.mixins.gtm;

import com.epimorphismmc.monomorphism.integration.gtm.data.chemical.material.info.MOMaterialIconSet;
import com.epimorphismmc.monomorphism.integration.gtm.data.tag.MOTagPrefix;
import com.epimorphismmc.monomorphism.integration.gtm.item.IMOItemRendererProvider;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.TagPrefixItem;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.Platform;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TagPrefixItem.class, remap = false)
public abstract class TagPrefixItemMixin extends Item implements IMOItemRendererProvider {

    @Unique private ICustomRenderer monomorphism$customRenderer;

    private TagPrefixItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method =
                    "<init>(Lnet/minecraft/world/item/Item$Properties;Lcom/gregtechceu/gtceu/api/data/tag/TagPrefix;Lcom/gregtechceu/gtceu/api/data/chemical/material/Material;)V",
            at = @At(value = "RETURN"))
    private void TagPrefixItem(
            Properties properties, TagPrefix tagPrefix, Material material, CallbackInfo ci) {
        if (Platform.isClient()) {
            if (material.getMaterialIconSet() instanceof MOMaterialIconSet iconSet) {
                this.monomorphism$customRenderer = iconSet.getCustomRenderer();
            }

            if (tagPrefix instanceof MOTagPrefix prefix) {
                this.monomorphism$customRenderer = prefix.getCustomRenderer();
            }
        }
    }

    @Override
    public ICustomRenderer getRenderInfo(ItemStack itemStack) {
        return monomorphism$customRenderer;
    }

    @Nullable @Override
    public IRenderer getRenderer(ItemStack stack) {
        if (monomorphism$customRenderer != null) {
            return monomorphism$customRenderer.getRenderer();
        }
        return null;
    }
}
