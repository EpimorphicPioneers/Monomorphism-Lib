package com.epimorphismmc.monomorphism.integration.gtm.client.renderer.item;

public class MOItemRenderers {

    private MOItemRenderers() {
        /**/
    }

    public static final HaloItemRenderer HALO_ITEM_RENDERER = new HaloItemRenderer();
    public static final SuperscriptItemRenderer SUPERSCRIPT_ITEM_RENDERER =
            new SuperscriptItemRenderer();

    public static void init() {}
}
