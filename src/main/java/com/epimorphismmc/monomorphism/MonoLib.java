package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.utility.DistLogger;

import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

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
        return new ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path));
    }

    /**
     * Can be used to get the current level the client is in.
     *
     * @return null if no client level is available (i.e. on a dedicated server)
     */
    @Nullable Level getClientLevel();

    @Nullable ResourceManager getResourceManager(PackType type);

    RecipeManager getRecipeManager();

    /**
     * @return a registry access object based on the logical side
     */
    RegistryAccess getRegistryAccess();

    /** Queues a task to be executed on this side */
    void queueTask(Runnable task);
}
