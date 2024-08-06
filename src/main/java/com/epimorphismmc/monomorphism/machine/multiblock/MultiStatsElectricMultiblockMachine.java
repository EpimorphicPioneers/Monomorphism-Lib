package com.epimorphismmc.monomorphism.machine.multiblock;

import com.epimorphismmc.monomorphism.machine.feature.IEnhanceFancyUIMachine;
import com.epimorphismmc.monomorphism.machine.trait.MultiblockTrait;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;

import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MultiStatsElectricMultiblockMachine extends WorkableElectricMultiblockMachine
        implements IEnhanceFancyUIMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            MultiStatsElectricMultiblockMachine.class,
            WorkableElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Getter
    protected List<MultiblockTrait> multiblockStats;

    public MultiStatsElectricMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        multiblockStats = new ArrayList<>();
    }

    //////////////////////////////////////
    // ***    Multiblock LifeCycle    ***//
    //////////////////////////////////////

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        multiblockStats.forEach(stats -> stats.onStructureFormed(getMultiblockState()));
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        multiblockStats.forEach(MultiblockTrait::onStructureInvalid);
    }

    //////////////////////////////////////
    // ***        Multiblock UI       ***//
    //////////////////////////////////////
    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        super.addDisplayText(textList);
        multiblockStats.forEach(stats -> stats.addDisplayText(textList));
    }

    //////////////////////////////////////
    // ***       Multiblock Data      ***//
    //////////////////////////////////////
    public void addStats(MultiblockTrait state) {
        multiblockStats.add(state);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
