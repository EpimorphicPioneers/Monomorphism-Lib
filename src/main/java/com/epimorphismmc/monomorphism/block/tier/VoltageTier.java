package com.epimorphismmc.monomorphism.block.tier;

import com.google.common.collect.Lists;
import com.gregtechceu.gtceu.api.GTValues;
import net.minecraft.network.chat.Component;

import java.util.List;

public enum VoltageTier implements IBlockTier {
    ULV(GTValues.ULV),
    LV(GTValues.LV),
    MV(GTValues.MV),
    HV(GTValues.HV),
    EV(GTValues.EV),
    IV(GTValues.IV),
    LuV(GTValues.LuV),
    ZPM(GTValues.ZPM),
    UV(GTValues.UV),
    UHV(GTValues.UHV),
    UEV(GTValues.UEV),
    UIV(GTValues.UIV),
    UXV(GTValues.UXV),
    OpV(GTValues.OpV),
    MAX(GTValues.MAX);

    private final int tier;

    VoltageTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int tier() {
        return tier;
    }

    @Override
    public String typeName() {
        return name().toLowerCase();
    }

    @Override
    public List<Component> getTooltips(BlockTierRegistry<?> registry) {
        var location = registry.getLocation();
        var key = "block_tier.%s.%s".formatted(location.getNamespace(), location.getPath());
        return List.of(Component.translatable(key, GTValues.VNF[tier]));
    }
}
