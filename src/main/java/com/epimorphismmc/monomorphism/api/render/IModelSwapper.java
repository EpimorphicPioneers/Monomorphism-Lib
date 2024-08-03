package com.epimorphismmc.monomorphism.api.render;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import com.tterrag.registrate.util.nullness.NonNullFunction;

public interface IModelSwapper {

    void registerBlock(
            ResourceLocation block, NonNullFunction<BakedModel, ? extends BakedModel> func);

    void registerItem(ResourceLocation item, NonNullFunction<BakedModel, ? extends BakedModel> func);
}
