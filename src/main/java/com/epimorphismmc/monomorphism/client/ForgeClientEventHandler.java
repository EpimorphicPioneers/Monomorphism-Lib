package com.epimorphismmc.monomorphism.client;

import com.epimorphismmc.monomorphism.MonoLib;
import com.epimorphismmc.monomorphism.block.tier.BlockTierManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MonoLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        BlockTierManager.INSTANCE.onItemTooltip(event);
    }
}
