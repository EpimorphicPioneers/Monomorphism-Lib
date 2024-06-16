package com.epimorphismmc.monomorphism.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

public class MOModels {

    public static <T extends Item>
            NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> simpleCustomModel(
                    ResourceLocation modelLocation, ResourceLocation... textureLocations) {
        return (ctx, prov) -> {
            var builder = prov.getBuilder("item/" + prov.name(ctx::getEntry))
                    .parent(new ModelFile.UncheckedModelFile(modelLocation));
            for (int i = 0; i < textureLocations.length; ++i) {
                builder.texture("layer%s".formatted(i), textureLocations[i]);
            }
        };
    }

    public static void simpleCustomBlockItemModel(
            DataGenContext<Item, BlockItem> context, RegistrateItemModelProvider provider) {
        provider.generated(context, provider.modLoc("block/" + provider.name(context)));
    }
}
