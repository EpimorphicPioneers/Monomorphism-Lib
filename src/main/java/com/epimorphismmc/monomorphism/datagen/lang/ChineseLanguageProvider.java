package com.epimorphismmc.monomorphism.datagen.lang;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ChineseLanguageProvider extends LanguageProvider {

    public ChineseLanguageProvider(PackOutput packOutput) {
        super(packOutput, MonoLib.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("monomorphism.shift_info", "§7按住§6SHIFT§7以显示更多信息");
        add("monomorphism.ctrl_info", "§7按住§6CTRL§7以显示更多信息");
        add("monomorphism.alt_info", "§7按住§6ALT§7以显示更多信息");
        add("monomorphism.universal.tier", "§7等级：%s");
        add("gui.monomorphism.machine_parallel.title", "当前机器并行");
        add("gui.monomorphism.change_parallel.desc", "调整机器并行数");
        add("monomorphism.machine.owner", "所有者：%s");
    }
}
