package com.epimorphismmc.monomorphism.registry;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <K> The type of the keys to be stored in the registry.
 * @param <V> The type of the values to be stored in the registry.
 *
 * @author GateGuardian
 * @date : 2024/8/28
 */
public interface IRegistry<K, V> extends Iterable<V> {

    @NotNull
    @Override
    default Iterator<V> iterator() {
        return values().iterator();
    }

    /**
     * Determines if the registry contains an entry with the given key.
     *
     * @param key the key to search for in the registry.
     * @return {@literal true} if an element has been registered to the registry with the given key,
     * {@literal false} otherwise.
     */
    boolean containKey(K key);

    /**
     * Determines if the registry contains a registration for this element.
     *
     * @param value the element to determine if registered.
     * @return {@literal true} if the registry has an exact registration for the given element.
     */
    boolean containValue(V value);

    void register(K key, V value);

    @Nullable
    V replace(K key, V value);

    V registerOrOverride(K key, V value);

    boolean remove(K name);

    Set<V> values();

    Set<K> keys();

    Set<Map.Entry<K, V>> entries();

    Map<K, V> registry();

    @Nullable
    V get(K key);

    V getOrDefault(K key, V defaultValue);

    K getKey(V value);

    K getOrDefaultKey(V key, K defaultKey);

    void writeBuf(V value, FriendlyByteBuf buf);

    @Nullable
    V readBuf(FriendlyByteBuf buf);

    Tag saveToNBT(V value);

    @Nullable
    V loadFromNBT(Tag tag);

    Codec<V> codec();
}
