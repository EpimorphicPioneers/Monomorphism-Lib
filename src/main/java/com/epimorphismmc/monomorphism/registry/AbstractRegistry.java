package com.epimorphismmc.monomorphism.registry;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractRegistry<K, V> implements IRegistry<K, V> {

    public static final Map<ResourceLocation, AbstractRegistry<?, ?>> REGISTERED = new HashMap<>();

    protected final BiMap<K, V> registry;

    @Getter
    protected final ResourceLocation registryName;

    @Getter
    protected boolean frozen = true;

    public AbstractRegistry(ResourceLocation registryName) {
        registry = initRegistry();
        this.registryName = registryName;

        REGISTERED.put(registryName, this);
    }

    protected BiMap<K, V> initRegistry() {
        return HashBiMap.create();
    }

    @Override
    public boolean containKey(K key) {
        return registry.containsKey(key);
    }

    @Override
    public boolean containValue(V value) {
        return registry.containsValue(value);
    }

    public void freeze() {
        if (frozen) {
            throw new IllegalStateException("Registry is already frozen!");
        }

        if (!checkActiveModContainer()) {
            return;
        }

        this.frozen = true;
    }

    public void unfreeze() {
        if (!frozen) {
            throw new IllegalStateException("Registry is already unfrozen!");
        }

        if (!checkActiveModContainer()) {
            return;
        }

        this.frozen = false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkActiveModContainer() {
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        return container != null
                && (container.getModId().equals(this.registryName.getNamespace())
                        || container.getModId().equals(MonoLib.MODID)
                        || container
                                .getModId()
                                .equals("minecraft")); // check for minecraft modid in case of datagen or a mishap
    }

    @Override
    public void register(K key, V value) {
        if (frozen) {
            throw new IllegalStateException(
                    "[register] registry %s has been frozen".formatted(registryName));
        }
        if (containKey(key)) {
            throw new IllegalStateException(
                    "[register] registry %s contains key %s already".formatted(registryName, key));
        }
        registry.put(key, value);
    }

    @Nullable @Override
    public V replace(K key, V value) {
        if (frozen) {
            throw new IllegalStateException(
                    "[replace] registry %s has been frozen".formatted(registryName));
        }
        if (!containKey(key)) {
            MonoLib.LOGGER.warn(
                    "[replace] couldn't find key %s in registry %s".formatted(registryName, key));
        }
        return registry.put(key, value);
    }

    @Override
    public V registerOrOverride(K key, V value) {
        if (frozen) {
            throw new IllegalStateException(
                    "[register] registry %s has been frozen".formatted(registryName));
        }
        return registry.put(key, value);
    }

    @Override
    public boolean remove(K name) {
        return registry.remove(name) != null;
    }

    @Override
    public Set<V> values() {
        return registry.values();
    }

    @Override
    public Set<K> keys() {
        return registry.keySet();
    }

    @Override
    public Set<Map.Entry<K, V>> entries() {
        return registry.entrySet();
    }

    @Override
    public Map<K, V> registry() {
        return registry;
    }

    @Nullable @Override
    public V get(K key) {
        return registry.get(key);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        return registry.getOrDefault(key, defaultValue);
    }

    @Override
    public K getKey(V value) {
        return registry.inverse().get(value);
    }

    @Override
    public K getOrDefaultKey(V key, K defaultKey) {
        return registry.inverse().getOrDefault(key, defaultKey);
    }

    // ************************ Built-in Registry ************************//

    public static class String<V> extends AbstractRegistry<java.lang.String, V> {

        public String(ResourceLocation registryName) {
            super(registryName);
        }

        @Override
        public void writeBuf(V value, FriendlyByteBuf buf) {
            buf.writeBoolean(containValue(value));
            if (containValue(value)) {
                buf.writeUtf(getKey(value));
            }
        }

        @Override
        public V readBuf(FriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return get(buf.readUtf());
            }
            return null;
        }

        @Override
        public Tag saveToNBT(V value) {
            if (containValue(value)) {
                return StringTag.valueOf(getKey(value));
            }
            return new CompoundTag();
        }

        @Override
        public V loadFromNBT(Tag tag) {
            return get(tag.getAsString());
        }

        @Override
        public Codec<V> codec() {
            return Codec.STRING.flatXmap(
                    str -> Optional.ofNullable(this.get(str))
                            .map(DataResult::success)
                            .orElseGet(() -> DataResult.error(
                                    () -> "Unknown registry key in " + this.registryName + ": " + str)),
                    obj -> Optional.ofNullable(this.getKey(obj))
                            .map(DataResult::success)
                            .orElseGet(() -> DataResult.error(
                                    () -> "Unknown registry element in " + this.registryName + ": " + obj)));
        }
    }

    public static class RL<V> extends AbstractRegistry<ResourceLocation, V> {

        public RL(ResourceLocation registryName) {
            super(registryName);
        }

        @Override
        public void writeBuf(V value, FriendlyByteBuf buf) {
            buf.writeBoolean(containValue(value));
            if (containValue(value)) {
                buf.writeResourceLocation(getKey(value));
            }
        }

        @Override
        public V readBuf(FriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return get(buf.readResourceLocation());
            }
            return null;
        }

        @Override
        public Tag saveToNBT(V value) {
            if (containValue(value)) {
                return StringTag.valueOf(getKey(value).toString());
            }
            return new CompoundTag();
        }

        @Override
        public V loadFromNBT(Tag tag) {
            return get(ResourceLocation.tryParse(tag.getAsString()));
        }

        @Override
        public Codec<V> codec() {
            return ResourceLocation.CODEC.flatXmap(
                    rl -> Optional.ofNullable(this.get(rl))
                            .map(DataResult::success)
                            .orElseGet(() -> DataResult.error(
                                    () -> "Unknown registry key in " + this.registryName + ": " + rl)),
                    obj -> Optional.ofNullable(this.getKey(obj))
                            .map(DataResult::success)
                            .orElseGet(() -> DataResult.error(
                                    () -> "Unknown registry element in " + this.registryName + ": " + obj)));
        }
    }
}
