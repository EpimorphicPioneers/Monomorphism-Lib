package com.epimorphismmc.monomorphism.api.render;

import com.epimorphismmc.monomorphism.client.model.swapper.ModelSwapper;

public class RenderApi implements IRendererApi {

    private static RenderApi instance;

    private final ModelSwapper modelSwapper = new ModelSwapper();

    public static IRendererApi instance() {
        return instance;
    }

    @Override
    public IModelSwapper getModelSwapper() {
        return modelSwapper;
    }
}
