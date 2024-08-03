package com.epimorphismmc.monomorphism.datagen.lang;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {
    public EnglishLanguageProvider(PackOutput packOutput) {
        super(packOutput, MonoLib.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("monomorphism.shift_info", "§7Hold down §6SHIFT§7 to show more information");
        add("monomorphism.ctrl_info", "§7Hold down §6CTRL§7 to show more information");
        add("monomorphism.alt_info", "§7Hold down §6ALT§7 to show more information");
        add("monomorphism.universal.tier", "§7Tier: %s");
        add("gui.monomorphism.machine_parallel.title", "The number of current machine parallels");
        add("gui.monomorphism.change_parallel.desc", "Adjust the number of machine parallels");
        add("monomorphism.machine.owner", "Owner: %s");
    }
}
