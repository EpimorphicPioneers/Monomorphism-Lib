package com.epimorphismmc.monomorphism.machine.multiblock;

import com.epimorphismmc.monomorphism.machine.feature.multiblock.stats.IParallelMachine;
import com.epimorphismmc.monomorphism.machine.feature.multiblock.stats.tier.ICoilMachine;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;

import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ParallelCoilMultiblockMachine extends MultiStatsElectricMultiblockMachine
        implements IParallelMachine, ICoilMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ParallelCoilMultiblockMachine.class,
            MultiStatsElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    protected final ParallelStats parallelStats;

    protected final CoilTierStats coilTierStats;

    public ParallelCoilMultiblockMachine(
            IMachineBlockEntity holder,
            Function<ParallelCoilMultiblockMachine, Integer> parallelCalculator,
            Object... args) {
        super(holder, parallelCalculator, args);
        this.parallelStats = new ParallelStats(this, machine -> {
            if (machine instanceof ParallelCoilMultiblockMachine parallelCoilMultiblockMachine) {
                return parallelCalculator.apply(parallelCoilMultiblockMachine);
            }
            return 1;
        });
        this.coilTierStats = new CoilTierStats(this);
    }

    //////////////////////////////////////
    // ***       Multiblock Data      ***//
    //////////////////////////////////////

    public int getCoilTier() {
        return coilTierStats.getCoilTier();
    }

    @Override
    public ICoilType getCoilType() {
        return coilTierStats.getCoilType();
    }

    @Override
    public int getMaxParallel() {
        return parallelStats.getMaxParallel();
    }

    @Override
    public int getParallelNumber() {
        return parallelStats.getParallelNumber();
    }

    @Override
    public void setParallelNumber(int number) {
        parallelStats.setParallelNumber(number);
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
