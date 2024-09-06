package com.epimorphismmc.monomorphism.data.pack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class DataPackProvider extends DynamicPackProvider {

    public DataPackProvider(ResourceLocation location, Pack.Position position) {
        super(location, PackType.SERVER_DATA, position);
    }

    /**
     * if subdir is null, no file ending is appended.
     *
     * @param id     the resource location of the file to be written.
     * @param subdir a nullable subdirectory for the data.
     * @param parent the parent folder where to write data to.
     * @param json   the json to write.
     */
    @ApiStatus.Internal
    public static void writeJson(
            ResourceLocation id, @Nullable String subdir, Path parent, JsonElement json) {
        try {
            Path file;
            if (subdir != null) {
                file = parent
                        .resolve(id.getNamespace())
                        .resolve(subdir)
                        .resolve(id.getPath() + ".json"); // assume JSON
            } else {
                file = parent
                        .resolve(id.getNamespace())
                        .resolve(id.getPath()); // assume the file type is also appended
                // if a full path is given.
            }
            Files.createDirectories(file.getParent());
            try (OutputStream output = Files.newOutputStream(file)) {
                output.write(json.toString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAdvancement(ResourceLocation loc, JsonObject obj) {
        ResourceLocation l = getAdvancementLocation(loc);
        data.put(l, obj.toString().getBytes(StandardCharsets.UTF_8));
    }

    public ResourceLocation getRecipeLocation(ResourceLocation recipeId) {
        return new ResourceLocation(
                recipeId.getNamespace(), String.join("", "recipes/", recipeId.getPath(), ".json"));
    }

    public ResourceLocation getAdvancementLocation(ResourceLocation advancementId) {
        return new ResourceLocation(
                advancementId.getNamespace(),
                String.join("", "advancements/", advancementId.getPath(), ".json"));
    }

    public ResourceLocation getTagLocation(String identifier, ResourceLocation tagId) {
        return new ResourceLocation(
                tagId.getNamespace(), String.join("", "tags/", identifier, "/", tagId.getPath(), ".json"));
    }
}
