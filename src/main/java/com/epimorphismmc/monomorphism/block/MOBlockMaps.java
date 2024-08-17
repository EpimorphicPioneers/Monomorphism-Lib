package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.block.tier.IBlockTier;

import com.gregtechceu.gtceu.api.GTCEuAPI;

import net.minecraft.world.level.block.Block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.function.Supplier;

public class MOBlockMaps {
    public static final Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> ALL_MACHINE_CASINGS =
            new Object2ObjectOpenHashMap<>();
    public static final Object2ObjectOpenHashMap<IBlockTier, Supplier<Block>> ALL_COIL_BLOCKS =
            new Object2ObjectOpenHashMap<>();

    public static void init() {
        GTCEuAPI.HEATING_COILS.forEach(
                (tier, block) -> ALL_COIL_BLOCKS.put((IBlockTier) tier, block::get));
    }
}
