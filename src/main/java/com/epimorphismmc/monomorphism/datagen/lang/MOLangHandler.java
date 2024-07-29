package com.epimorphismmc.monomorphism.datagen.lang;

import com.epimorphismmc.monomorphism.registry.registrate.providers.MOLangProvider;

public class MOLangHandler {

    private MOLangHandler() {
        /**/
    }

    public static void init(MOLangProvider provider) {
        provider.add(
                "monomorphism.shift_desc_extended_info",
                "§7Hold down §6SHIFT§7 to show more information",
                "§7按住§6SHIFT§7以显示更多信息");

        provider.add(
                "monomorphism.ctrl_desc_extended_info",
                "§7Hold down §6CTRL§7 to show more information",
                "§7按住§6CTRL§7以显示更多信息");

        provider.add(
                "monomorphism.alt_desc_extended_info",
                "§7Hold down §6ALT§7 to show more information",
                "§7按住§6ALT§7以显示更多信息");

        provider.add("monomorphism.universal.desc.tier", "§7Tier: %s", "§7等级：%s");

        provider.add(
                "gui.monomorphism.machine_parallel.title",
                "The number of current machine parallels",
                "当前机器并行");
        provider.add(
                "gui.monomorphism.change_parallel.desc",
                "Adjust the number of machine parallels",
                "调整机器并行数");

        provider.add("monomorphism.machine.owner", "Owner: %s", "所有者：%s");
    }
}
