package com.epimorphismmc.monomorphism.mixins.gtm;

import com.epimorphismmc.monomorphism.block.tier.ITierType;

import com.gregtechceu.gtceu.api.block.ICoilType;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ICoilType.class, remap = false)
public interface ICoilTypeMixin extends ITierType {
    @Shadow
    int getTier();

    @Shadow
    @NotNull String getName();

    @Override
    default int tier() {
        return getTier();
    }

    @Override
    default String typeName() {
        return getName();
    }
}
