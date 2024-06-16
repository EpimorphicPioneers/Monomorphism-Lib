package com.epimorphismmc.monomorphism.machine.feature.multiblock.stats;

import com.epimorphismmc.monomorphism.machine.trait.MultiblockStats;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.util.Mth;

import java.util.function.Function;

public interface IParallelMachine extends IMachineFeature {
    int getMaxParallel();

    int getParallelNumber();

    void setParallelNumber(int number);

    class ParallelStats extends MultiblockStats implements IParallelMachine {
        protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER =
                new ManagedFieldHolder(ParallelStats.class, MultiblockStats.MANAGED_FIELD_HOLDER);

        @Persisted
        private int parallelNumber;

        private final Function<IParallelMachine, Integer> parallelCalculator;

        public ParallelStats(
                MetaMachine machine, Function<IParallelMachine, Integer> parallelCalculator) {
            super(machine);
            this.parallelCalculator = parallelCalculator;
        }

        @Override
        public int getMaxParallel() {
            if (getMultiblock() instanceof IParallelMachine parallelMachine) {
                return parallelCalculator.apply(parallelMachine);
            }
            return 1;
        }

        @Override
        public int getParallelNumber() {
            return Math.max(1, parallelNumber);
        }

        @Override
        public void setParallelNumber(int number) {
            var multiblock = getMultiblock();
            if (multiblock == null || !multiblock.isFormed()) return;

            if (multiblock instanceof WorkableMultiblockMachine workableMultiblockMachine) {
                this.parallelNumber = Mth.clamp(number, 1, getMaxParallel());
                workableMultiblockMachine.getRecipeLogic().markLastRecipeDirty();
            }
        }

        @Override
        public void onStructureFormed(MultiblockState state) {
            if (parallelNumber == 0) parallelNumber = getMaxParallel();
        }

        @Override
        public void onStructureInvalid() {
            parallelNumber = 0;
        }

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }
    }
}
