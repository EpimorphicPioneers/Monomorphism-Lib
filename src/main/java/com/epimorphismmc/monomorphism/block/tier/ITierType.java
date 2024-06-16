package com.epimorphismmc.monomorphism.block.tier;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public interface ITierType extends StringRepresentable {
    String typeName();

    int tier();

    @Override
    @NotNull default String getSerializedName() {
        return typeName();
    }

    record SimpleTierBlockType(String typeName, int tier) implements ITierType {
        @Nonnull
        @Override
        public String toString() {
            return typeName();
        }
    }

    enum TierBlockType implements ITierType {
        DUMMY(-1),
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
