package com.epimorphismmc.monomorphism.integration.gtm.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface IConfigurableMachine extends IMachineFeature {
    CompoundTag serializeConfig();

    void deserializeConfig(CompoundTag tag);

    ResourceLocation getConfigType();
}
