package com.epimorphismmc.monomorphism.client;

import com.epimorphismmc.monomorphism.api.render.IModelSwapper;
import com.epimorphismmc.monomorphism.api.render.IRendererApi;
import com.epimorphismmc.monomorphism.client.model.swapper.ModelSwapper;

public class RenderApi implements IRendererApi {

    private final ModelSwapper modelSwapper = new ModelSwapper();

    @Override
    public IModelSwapper getModelSwapper() {
        return modelSwapper;
    }
}
