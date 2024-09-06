package com.epimorphismmc.monomorphism.integration.gtm.item.component;

import com.epimorphismmc.monomorphism.integration.gtm.client.renderer.item.MOItemRenderers;

import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import org.jetbrains.annotations.NotNull;

public interface INumberSuperscriptEffect extends ICustomRenderer {
    int tier();

    boolean isRoma();

    @Override
    default @NotNull IRenderer getRenderer() {
        return MOItemRenderers.SUPERSCRIPT_ITEM_RENDERER;
    }
}
