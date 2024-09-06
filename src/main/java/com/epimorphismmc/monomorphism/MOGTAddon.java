package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.block.tier.CoilTier;
import com.epimorphismmc.monomorphism.block.tier.MOBlockTiers;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.registry.GTRegistration;

@GTAddon
public class MOGTAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return GTRegistration.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        CoilTier.init();
        MOBlockTiers.init();
    }

    @Override
    public String addonModId() {
        return MonoLib.MODID;
    }
}
