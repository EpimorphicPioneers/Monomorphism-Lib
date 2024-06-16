package com.epimorphismmc.monomorphism.client.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public interface IRenderUtilities {

    Set<IRenderUtilities> EVENT_REGISTERS = new HashSet<>();

    /**
     * Register TextureSprite here.
     */
    default void onPrepareTextureAtlas(
            ResourceLocation atlasName, Consumer<ResourceLocation> register) {}

    /**
     * Register additional model here.
     */
    default void onAdditionalModel(Consumer<ResourceLocation> registry) {}

    /**
     * If the renderer requires event registration either {@link #onPrepareTextureAtlas} or {@link #onAdditionalModel}, call this method in the constructor.
     */
    default void registerEvent() {
        synchronized (EVENT_REGISTERS) {
            EVENT_REGISTERS.add(this);
        }
    }
}
