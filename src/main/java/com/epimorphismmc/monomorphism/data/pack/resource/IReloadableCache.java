package com.epimorphismmc.monomorphism.data.pack.resource;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public interface IReloadableCache {

    PackType getType();

    void reload(ResourceManager resourceManager);
}
