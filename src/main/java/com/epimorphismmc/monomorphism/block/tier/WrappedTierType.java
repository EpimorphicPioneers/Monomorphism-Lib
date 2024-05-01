package com.epimorphismmc.monomorphism.block.tier;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class WrappedTierType<T extends Block> implements ITierType {

    private final Supplier<T> supplier;
    private final int tier;

    public WrappedTierType(Supplier<T> supplier, int tier) {
        this.supplier = supplier;
        this.tier = tier;
    }

    @Override
    public String typeName() {
        return supplier.get().getDescriptionId();
    }

    @Override
    public int tier() {
        return tier;
    }
}
