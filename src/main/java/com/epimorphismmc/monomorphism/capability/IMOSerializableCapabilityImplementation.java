package com.epimorphismmc.monomorphism.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMOSerializableCapabilityImplementation<C extends ICapabilityProvider, V extends IMOSerializableCapabilityImplementation.Serializable<V>> extends IMOCapabilityImplementation<C, V> {
    @Override
    default IMOCapabilityImplementation.Serializer<V> getSerializer() {
        return new IMOCapabilityImplementation.Serializer<>() {
            @Override
            public CompoundTag writeToNBT(V object) {
                return object.serializeNBT();
            }

            @Override
            public void readFromNBT(V object, CompoundTag nbt) {
                object.deserializeNBT(nbt);
            }
        };
    }

    @Override
    default void copyData(V from, V to) {
        to.copyDataFrom(from);
    }

    interface Serializable<F> extends INBTSerializable<CompoundTag> {
        void copyDataFrom(F from);
    }
}
