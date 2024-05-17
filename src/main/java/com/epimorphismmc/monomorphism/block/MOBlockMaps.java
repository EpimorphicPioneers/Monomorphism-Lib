package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.block.tier.ITierType;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class MOBlockMaps {
    public static final Object2ObjectOpenHashMap<ITierType, Supplier<Block>> ALL_MACHINE_CASINGS = new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<ITierType, Supplier<Block>> ALL_COIL_BLOCKS = new Object2ObjectOpenHashMap<>();

    public static void init() {
        GTCEuAPI.HEATING_COILS.forEach((tier, block) -> ALL_COIL_BLOCKS.put((ITierType) tier, block::get));
    }
}
