package com.epimorphismmc.monomorphism.machine.feature.multiblock.stats.tier;

import com.epimorphismmc.monomorphism.machine.multiblock.MultiStatsElectricMultiblockMachine;
import com.epimorphismmc.monomorphism.machine.trait.MultiblockStats;
import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.common.block.CoilBlock;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;

public interface ICoilMachine extends IMachineFeature {
    int getCoilTier();

    ICoilType getCoilType();

    class CoilTierStats extends MultiblockStats implements ICoilMachine {

        protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(CoilTierStats.class, MultiblockStats.MANAGED_FIELD_HOLDER);

        @Getter
        private ICoilType coilType  = CoilBlock.CoilType.CUPRONICKEL;

        public CoilTierStats(MultiStatsElectricMultiblockMachine machine) {
            super(machine);
            machine.addStats(this);
        }

        @Override
        public void onStructureFormed(MultiblockState state) {
            var type = state.getMatchContext().get("CoilType");
            if (type instanceof ICoilType coil) {
                this.coilType = coil;
            }
        }

        @Override
        public void onStructureInvalid() {
            this.coilType = CoilBlock.CoilType.CUPRONICKEL;
        }

        @Override
        public int getCoilTier() {
            return coilType.getTier();
        }

        @Override
        public ManagedFieldHolder getFieldHolder() {
            return MANAGED_FIELD_HOLDER;
        }
    }
}
