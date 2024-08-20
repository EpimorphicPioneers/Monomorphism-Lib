package com.epimorphismmc.monomorphism.integration.gtm.item.component;

import com.epimorphismmc.monomorphism.integration.gtm.client.renderer.item.MOItemRenderers;

import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

public interface IHaloEffect extends ICustomRenderer {
    ResourceLocation haloTexture();

    boolean shouldDrawHalo();

    int haloColour();

    int haloSize();

    boolean shouldDrawPulse();

    @Override
    default @NotNull IRenderer getRenderer() {
        return MOItemRenderers.HALO_ITEM_RENDERER;
    }
}
