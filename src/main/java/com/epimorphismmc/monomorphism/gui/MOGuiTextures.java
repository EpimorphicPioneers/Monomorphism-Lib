package com.epimorphismmc.monomorphism.gui;

import com.epimorphismmc.monomorphism.MonoLib;

import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public class MOGuiTextures {

    public static final ResourceTexture OVERLAY_INVENTORY_CONFIGURATOR =
            createTexture("overlay/inventory_configurator.png");
    public static final ResourceTexture OVERLAY_TANK_CONFIGURATOR =
            createTexture("overlay/tank_configurator.png");
    public static final ResourceTexture OVERLAY_PARALLEL_CONFIGURATOR =
            createTexture("overlay/parallel_configurator.png");

    private static ResourceTexture createTexture(String imageLocation) {
        return new ResourceTexture("%s:textures/gui/%s".formatted(MonoLib.MODID, imageLocation));
    }

    private static ResourceBorderTexture createBorderTexture(
            String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        return new ResourceBorderTexture(
                "%s:textures/gui/%s".formatted(MonoLib.MODID, imageLocation),
                imageWidth,
                imageHeight,
                cornerWidth,
                cornerHeight);
    }
}
