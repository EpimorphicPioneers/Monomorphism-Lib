package com.epimorphismmc.monomorphism.block.tier;

import com.gregtechceu.gtceu.api.GTValues;

import javax.annotation.Nonnull;
import java.util.Comparator;

public interface IBlockTier {

    Comparator<IBlockTier> COMPARATOR = Comparator.comparingInt(IBlockTier::tier);

    String typeName();

    int tier();

    record SimpleTierBlockType(String typeName, int tier) implements IBlockTier {
        @Nonnull
        @Override
        public String toString() {
            return typeName();
        }
    }

    enum TierBlockType implements IBlockTier {
        ULV(GTValues.ULV),
        LV(GTValues.LV),
        MV(GTValues.MV),
        HV(GTValues.HV),
        EV(GTValues.EV),
        IV(GTValues.IV),
        LuV(GTValues.LuV),
        ZPM(GTValues.ZPM),
        UV(GTValues.UV),
        UHV(GTValues.UHV),
        UEV(GTValues.UEV),
        UIV(GTValues.UIV),
        UXV(GTValues.UXV),
        OpV(GTValues.OpV),
        MAX(GTValues.MAX);

        private final int tier;

        TierBlockType(int tier) {
            this.tier = tier;
        }

        @Override
        public int tier() {
            return tier;
        }

        @Override
        public String typeName() {
            return name().toLowerCase();
        }
    }
}
