package com.epimorphismmc.monomorphism.datagen;

import com.epimorphismmc.monomorphism.MonoLib;
import com.epimorphismmc.monomorphism.datagen.lang.ChineseLanguageProvider;
import com.epimorphismmc.monomorphism.datagen.lang.EnglishLanguageProvider;

import com.epimorphismmc.monomorphism.datagen.tag.MOBlockTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class Datagen {
    public static void init(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        var helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        gen.addProvider(event.includeClient(), new EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new ChineseLanguageProvider(packOutput));

        gen.addProvider(event.includeServer(), new MOBlockTagsProvider(packOutput, lookupProvider, MonoLib.MODID, helper));
    }
}
