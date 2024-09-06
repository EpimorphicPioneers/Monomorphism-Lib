package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.utility.DistLogger;
import com.epimorphismmc.monomorphism.utility.FormattingUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public interface MonoLib {

    String MODID = "monomorphism";
    String NAME = "Monomorphism";
    Logger LOGGER = new DistLogger(NAME);

    static MonoLib instance() {
        return MonoLibCommon.instance;
    }

    static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, FormattingUtils.toLowerCaseUnder(path));
    }

    @Nullable ResourceManager getResourceManager(PackType type);

    RecipeManager getRecipeManager();

    /**
     * @return a registry access object based on the logical side
     */
    RegistryAccess getRegistryAccess();
}
