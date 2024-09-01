package com.epimorphismmc.monomorphism.utility;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ResourceUtils {

    public static @Nullable ResourceManager resourceManager(PackType type) {
        return MonoLib.instance().getResourceManager(type);
    }

    public static Optional<Resource> getResource(PackType type, ResourceLocation resource) {
        return Optional.ofNullable(resourceManager(type))
                .flatMap(manager -> manager.getResource(resource));
    }

    public static byte @Nullable [] getAsBytes(PackType type, ResourceLocation resource) {
        var manager = resourceManager(type);
        if (manager != null) {
            try (BufferedInputStream in = new BufferedInputStream(manager.open(resource))) {
                return IOUtils.toByteArray(in);
            } catch (IOException ignored) {

            }
        }
        return null;
    }

    public static @Nullable String getAsString(PackType type, ResourceLocation resource) {
        var manager = resourceManager(type);
        if (manager != null) {
            try (BufferedInputStream in = new BufferedInputStream(manager.open(resource))) {
                return IOUtils.toString(in, StandardCharsets.UTF_8);
            } catch (IOException ignored) {

            }
        }
        return null;
    }

    public static @Nullable JsonElement getAsJson(PackType type, ResourceLocation resource) {
        var manager = resourceManager(type);
        if (manager != null) {
            try (BufferedReader in = manager.openAsReader(resource)) {
                return JsonParser.parseReader(in);
            } catch (IOException ignored) {

            }
        }
        return null;
    }
}
