package com.epimorphismmc.monomorphism.data.pack.resource;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CacheReloadManager implements ICacheReloadManager {

    public static final CacheReloadManager INSTANCE = new CacheReloadManager();

    private final Listener client;

    private final Listener server;

    public CacheReloadManager() {
        this.client = new Listener();
        this.server = new Listener();
    }

    public void registerListener(PackType type, Consumer<PreparableReloadListener> register) {
        if (type == PackType.SERVER_DATA) {
            register.accept(server);
        } else {
            register.accept(client);
        }
    }

    @Override
    public void registerCache(IReloadableCache cache) {
        if (cache.getType() == PackType.SERVER_DATA) {
            server.caches.add(cache);
        } else {
            client.caches.add(cache);
        }
    }

    private static class Listener implements ResourceManagerReloadListener {
        private final Set<IReloadableCache> caches = Collections.newSetFromMap(new WeakHashMap<>());

        @Override
        public void onResourceManagerReload(ResourceManager resourceManager) {
            caches.forEach(cache -> cache.reload(resourceManager));
        }
    }
}
