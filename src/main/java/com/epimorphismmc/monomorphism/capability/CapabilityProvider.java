package com.epimorphismmc.monomorphism.capability;

import com.epimorphismmc.monomorphism.Monomorphism;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CapabilityProvider<V> implements ICapabilitySerializable<CompoundTag> {
    private final Supplier<Capability<V>> capabilitySupplier;
    private final IMOCapabilityImplementation.Serializer<V> serializer;
    private final V value;
    private final LazyOptional<V> valueHolder;

    public CapabilityProvider(
            Supplier<Capability<V>> capability,
            IMOCapabilityImplementation.Serializer<V> serializer,
            V value) {
        this.capabilitySupplier = capability;
        this.serializer = serializer;
        this.value = value;
        this.valueHolder = LazyOptional.of(this::getCapabilityValue);
    }

    public Capability<V> getCapability() {
        return this.capabilitySupplier.get();
    }

    @Nonnull
    public V getCapabilityValue() {
        return value;
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.checkCapability()
                .map(capability -> this.serializer.writeToNBT(this.getCapabilityValue()))
                .orElse(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.checkCapability()
                .ifPresent(capability -> this.serializer.readFromNBT(this.getCapabilityValue(), nbt));
    }

    protected Optional<Capability<V>> checkCapability() {
        Capability<V> capability = this.getCapability();
        if (capability == null) {
            Monomorphism.instance
                    .getLogger()
                    .error(
                            "[SEVERE] Capability implementation is requested before injection, report this to the mod author");
            new RuntimeException("[SEVERE] Encountered null capability for: "
                            + this.getCapabilityValue().getClass().getName())
                    .printStackTrace();
        }
        return Optional.ofNullable(capability);
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(
            @Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == this.getCapability()) {
            return valueHolder.cast();
        }
        return LazyOptional.empty();
    }
}
