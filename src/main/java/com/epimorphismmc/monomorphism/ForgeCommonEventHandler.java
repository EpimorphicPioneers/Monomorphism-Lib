package com.epimorphismmc.monomorphism;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MonoLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeCommonEventHandler {
    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {

    }
}
