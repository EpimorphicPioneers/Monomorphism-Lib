package com.epimorphismmc.monomorphism.data.pack;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class DynamicPackProvider implements RepositorySource, PackResources {

    protected final ObjectSet<String> domains = ObjectOpenHashSet.of("minecraft", "forge", "c");

    protected final Map<ResourceLocation, byte[]> data = Maps.newConcurrentMap();

    private final ResourceLocation location;

    private final PackType type;

    private final Pack.Position position;

    public DynamicPackProvider(ResourceLocation location, PackType type, Pack.Position position) {
        this.location = location;
        this.type = type;
        this.position = position;
        domains.add(location.getNamespace());
    }

    protected abstract void addDynamicData();

    @Override
    public void loadPacks(Consumer<Pack> onLoad) {
        data.clear();
        addDynamicData();
        onLoad.accept(Pack.readMetaAndCreate(
                location.toString(),
                Component.literal(location.toString()),
                true,
                name -> this,
                type,
                position,
                PackSource.BUILT_IN));
    }

    @Nullable @Override
    public IoSupplier<InputStream> getRootResource(String... elements) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        if (packType == type) {
            var byteArray = data.get(location);
            if (byteArray != null) {
                return () -> new ByteArrayInputStream(byteArray);
            }
        }
        return null;
    }

    @Override
    public void listResources(
            PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == type) {
            if (!path.endsWith("/")) path += "/";
            final String finalPath = path;
            data.keySet().stream()
                    .filter(Objects::nonNull)
                    .filter(loc -> loc.getPath().startsWith(finalPath))
                    .forEach((id) -> {
                        IoSupplier<InputStream> resource = this.getResource(packType, id);
                        if (resource != null) {
                            resourceOutput.accept(id, resource);
                        }
                    });
        }
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return packType == type ? domains : Collections.emptySet();
    }

    @Nullable @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metaReader) {
        if (metaReader == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(
                    Component.literal("Mono dynamic assets"),
                    SharedConstants.getCurrentVersion().getPackVersion(type));
        }
        return null;
    }

    @Override
    public String packId() {
        return location.toString();
    }

    @Override
    public void close() {
        // NOOP
    }
}
