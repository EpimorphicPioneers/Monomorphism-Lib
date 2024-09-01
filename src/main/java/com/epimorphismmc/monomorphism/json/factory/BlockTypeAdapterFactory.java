package com.epimorphismmc.monomorphism.json.factory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BlockTypeAdapterFactory implements TypeAdapterFactory {
    public static final BlockTypeAdapterFactory INSTANCE = new BlockTypeAdapterFactory();

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!Block.class.isAssignableFrom(type.getRawType())) return null;
        return (TypeAdapter<T>) new BlockTypeAdapter(gson);
    }

    private static final class BlockTypeAdapter extends TypeAdapter<Block> {

        private final Gson gson;

        private BlockTypeAdapter(final Gson gson) {
            this.gson = gson;
        }

        @Override
        public void write(final JsonWriter out, final Block value) {
            if (value == null) {
                gson.toJson(JsonNull.INSTANCE, out);
                return;
            }
            final JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", BuiltInRegistries.BLOCK.getKey(value).toString());
            gson.toJson(jsonObject, out);
        }

        @Override
        public Block read(final JsonReader in) {
            final JsonElement jsonElement = gson.fromJson(in, JsonElement.class);
            if (jsonElement.isJsonNull()) return null;
            return BuiltInRegistries.BLOCK.get(
                    new ResourceLocation(jsonElement.getAsJsonObject().get("id").getAsString()));
        }
    }
}
