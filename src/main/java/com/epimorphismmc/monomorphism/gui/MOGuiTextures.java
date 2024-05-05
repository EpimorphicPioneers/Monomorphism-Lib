package com.epimorphismmc.monomorphism.gui;

import com.epimorphismmc.monomorphism.Monomorphism;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;

public class MOGuiTextures {

    public static final ResourceTexture OVERLAY_PARALLEL_CONFIGURATOR = createTexture("overlay/parallel_configurator.png");

    private static ResourceTexture createTexture(String imageLocation) {
        return new ResourceTexture("%s:textures/gui/%s".formatted(Monomorphism.MODID, imageLocation));
    }

    private static ResourceBorderTexture createBorderTexture(String imageLocation, int imageWidth, int imageHeight, int cornerWidth, int cornerHeight) {
        return new ResourceBorderTexture("%s:textures/gui/%s".formatted(Monomorphism.MODID, imageLocation), imageWidth, imageHeight, cornerWidth, cornerHeight);
    }
}
