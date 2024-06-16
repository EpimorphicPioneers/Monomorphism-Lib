package com.epimorphismmc.monomorphism.machine.trait;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MultiblockStats extends MachineTrait {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER =
            new ManagedFieldHolder(MultiblockStats.class);

    public MultiblockStats(MetaMachine machine) {
        super(machine);
    }

    public void onStructureFormed(MultiblockState state) {
        /**/
    }

    public void onStructureInvalid() {
        /**/
    }

    public void addDisplayText(@NotNull List<Component> textList) {
        /**/
    }

    //////////////////////////////////////
    // **********     Data     **********//
    //////////////////////////////////////
    protected @Nullable IMultiController getMultiblock() {
        if (machine instanceof IMultiController controller) {
            return controller;
        }
        return null;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
