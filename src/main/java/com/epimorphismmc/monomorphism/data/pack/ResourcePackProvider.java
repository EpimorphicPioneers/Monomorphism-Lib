package com.epimorphismmc.monomorphism.data.pack;

import com.gregtechceu.gtceu.config.ConfigHolder;

import com.lowdragmc.lowdraglib.Platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static com.gregtechceu.gtceu.data.pack.GTDynamicDataPack.writeJson;

public abstract class ResourcePackProvider extends DynamicPackProvider {

    public ResourcePackProvider(ResourceLocation location, Pack.Position position) {
        super(location, PackType.CLIENT_RESOURCES, position);
    }

    @ApiStatus.Internal
    public static void writeByteArray(
            ResourceLocation id, @Nullable String subdir, Path parent, byte[] data) {
        try {
            Path file;
            if (subdir != null) {
                file = parent
                        .resolve(id.getNamespace())
                        .resolve(subdir)
                        .resolve(id.getPath() + ".png"); // assume PNG
            } else {
                file = parent
                        .resolve(id.getNamespace())
                        .resolve(id.getPath()); // assume the file type is also appended
                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBlockModel(ResourceLocation loc, JsonElement obj) {
        ResourceLocation l = getModelLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = Platform.getGamePath().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, obj);
        }
        data.put(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void addBlockModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addBlockModel(loc, obj.get());
    }

    public void addItemModel(ResourceLocation loc, JsonElement obj) {
        ResourceLocation l = getItemModelLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = Platform.getGamePath().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, obj);
        }
        data.put(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void addItemModel(ResourceLocation loc, Supplier<JsonElement> obj) {
        addItemModel(loc, obj.get());
    }

    public void addBlockState(ResourceLocation loc, JsonElement stateJson) {
        ResourceLocation l = getBlockStateLocation(loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = Platform.getGamePath().resolve("gtceu/dumped/assets");
            writeJson(l, null, parent, stateJson);
        }
        data.put(l, stateJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    public void addBlockState(ResourceLocation loc, Supplier<JsonElement> generator) {
        addBlockState(loc, generator.get());
    }

    public void addBlockTexture(ResourceLocation loc, byte[] bytes) {
        ResourceLocation l = getTextureLocation("block", loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = Platform.getGamePath().resolve("gtceu/dumped/assets");
            writeByteArray(l, null, parent, bytes);
        }
        data.put(l, bytes);
    }

    public void addItemTexture(ResourceLocation loc, byte[] bytes) {
        ResourceLocation l = getTextureLocation("item", loc);
        if (ConfigHolder.INSTANCE.dev.dumpAssets) {
            Path parent = Platform.getGamePath().resolve("gtceu/dumped/assets");
            writeByteArray(l, null, parent, bytes);
        }
        data.put(l, bytes);
    }

    public ResourceLocation getBlockStateLocation(ResourceLocation blockId) {
        return new ResourceLocation(
                blockId.getNamespace(), String.join("", "blockstates/", blockId.getPath(), ".json"));
    }

    public ResourceLocation getModelLocation(ResourceLocation blockId) {
        return new ResourceLocation(
                blockId.getNamespace(), String.join("", "models/", blockId.getPath(), ".json"));
    }

    public ResourceLocation getItemModelLocation(ResourceLocation itemId) {
        return new ResourceLocation(
                itemId.getNamespace(), String.join("", "models/item/", itemId.getPath(), ".json"));
    }

    public ResourceLocation getTextureLocation(@Nullable String path, ResourceLocation tagId) {
        if (path == null) {
            return new ResourceLocation(
                    tagId.getNamespace(), String.join("", "textures/", tagId.getPath(), ".png"));
        }
        return new ResourceLocation(
                tagId.getNamespace(), String.join("", "textures/", path, "/", tagId.getPath(), ".png"));
    }
}
