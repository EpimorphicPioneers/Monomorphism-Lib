package com.epimorphismmc.monomorphism.client.model.swapper;

import com.epimorphismmc.monomorphism.api.render.IModelSwapper;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import com.tterrag.registrate.util.nullness.NonNullFunction;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.epimorphismmc.monomorphism.client.model.ModelFactory.getAllBlockStateModelLocations;
import static com.epimorphismmc.monomorphism.client.model.ModelFactory.getItemModelLocation;

public class ModelSwapper implements IModelSwapper {

    private final CustomBlockModels customBlockModels = new CustomBlockModels();
    private final CustomItemModels customItemModels = new CustomItemModels();

    @Override
    public void registerBlock(
            ResourceLocation block, NonNullFunction<BakedModel, ? extends BakedModel> func) {
        customBlockModels.register(block, func);
    }

    @Override
    public void registerItem(
            ResourceLocation item, NonNullFunction<BakedModel, ? extends BakedModel> func) {
        customItemModels.register(item, func);
    }

    public void registerListeners(IEventBus modEventBus) {
        modEventBus.addListener(this::onModelBake);
    }

    private void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        customBlockModels.forEach((block, modelFunc) ->
                swapModels(modelRegistry, getAllBlockStateModelLocations(block), modelFunc));
        customItemModels.forEach(
                (item, modelFunc) -> swapModels(modelRegistry, getItemModelLocation(item), modelFunc));
    }

    private static <T extends BakedModel> void swapModels(
            Map<ResourceLocation, BakedModel> modelRegistry,
            List<ModelResourceLocation> locations,
            Function<BakedModel, T> factory) {
        locations.forEach(location -> swapModels(modelRegistry, location, factory));
    }

    private static <T extends BakedModel> void swapModels(
            Map<ResourceLocation, BakedModel> modelRegistry,
            ModelResourceLocation location,
            Function<BakedModel, T> factory) {
        modelRegistry.put(location, factory.apply(modelRegistry.get(location)));
    }
}
