package com.epimorphismmc.monomorphism.client.utils;

import com.epimorphismmc.monomorphism.data.pack.resource.CacheReloadManager;
import com.epimorphismmc.monomorphism.data.pack.resource.IReloadableCache;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

/**
 * Caches and manages lists of baked quads for different directions or no specific direction.
 * Provides thread-safe access and ensures that quads for each direction are baked only once.
 * When client-side resources are reloaded, the cache will be invalidated automatically.
 * <p>
 * Adapted from InfinityLib.
 *
 * @author GateGuardian
 * @date : 2024/8/28
 */
@OnlyIn(Dist.CLIENT)
public class QuadCache {
    private final Map<Direction, SafeQuadStore> dirQuads;
    private final SafeQuadStore nullQuads;

    public QuadCache(Function<Direction, List<BakedQuad>> quadBaker) {
        Map<Direction, SafeQuadStore> map = Maps.newEnumMap(Direction.class);
        for (var face : Direction.values()) {
            map.put(face, new SafeQuadStore(face, quadBaker));
        }
        this.dirQuads = ImmutableMap.copyOf(map);
        this.nullQuads = new SafeQuadStore(null, quadBaker);
    }

    public List<BakedQuad> getQuads(@Nullable Direction face) {
        return face == null ? this.nullQuads.getQuads() : this.dirQuads.get(face).getQuads();
    }

    private static final class SafeQuadStore implements IReloadableCache {
        private final Direction face;

        private final Function<Direction, List<BakedQuad>> quadBaker;

        private List<BakedQuad> quads;

        private SafeQuadStore(
                @Nullable Direction face, Function<Direction, List<BakedQuad>> quadBaker) {
            this.face = face;
            this.quadBaker = quadBaker;
            CacheReloadManager.INSTANCE.registerCache(this);
        }

        public @Nullable Direction getFace() {
            return this.face;
        }

        public synchronized List<BakedQuad> getQuads() {
            if (this.quads == null) {
                var list = this.quadBaker.apply(this.getFace());
                this.quads = list != null ? ImmutableList.copyOf(list) : ImmutableList.of();
            }
            return quads;
        }

        @Override
        public PackType getType() {
            return PackType.CLIENT_RESOURCES;
        }

        @Override
        public synchronized void reload(ResourceManager resourceManager) {
            this.quads = null;
        }
    }
}
