package com.epimorphismmc.monomorphism.block.tier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import lombok.experimental.Delegate;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CoilTier {

    private static final BiMap<ICoilType, ICoilTier> MAP = HashBiMap.create();

    public static void init() {
        var coils = GTCEuAPI.HEATING_COILS.keySet().stream().sorted(Comparator.comparingInt(ICoilType::getCoilTemperature)).toList();
        for (int i = 0; i < coils.size(); i++) {
            var type = coils.get(i);
            MAP.put(type, new CoilTypeWrapper(type, i));
        }
    }

    public static ICoilType toType(ICoilTier tier) {
        return MAP.inverse().get(tier);
    }

    public static ICoilTier toTier(ICoilType type) {
        return MAP.get(type);
    }

    public static Collection<ICoilTier> tiers() {
        return MAP.values();
    }

    public static Collection<ICoilType> types() {
        return MAP.keySet();
    }

    public static class CoilTypeWrapper implements ICoilTier {

        @Delegate
        private final ICoilType type;

        private final int tier;

        public CoilTypeWrapper(ICoilType type, int tier) {
            this.type = type;
            this.tier = tier;
        }

        @Override
        public int tier() {
            return tier;
        }

        @Override
        public List<Component> getTooltips(BlockTierRegistry<?> registry) {
            var location = registry.getLocation();
            var key = "block_tier.%s.%s".formatted(location.getNamespace(), location.getPath());
            var material = getMaterial();
            if (material != null) {
                return List.of(
                        Component.translatable(key,
                        Component.literal(tier + " (")
                                .append(Component.translatable(getMaterial().getUnlocalizedName()))
                                .append(")")));
            }
            return Collections.emptyList();
        }
    }
}
