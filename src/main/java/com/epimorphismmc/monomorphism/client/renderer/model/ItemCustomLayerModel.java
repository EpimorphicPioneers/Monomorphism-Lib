package com.epimorphismmc.monomorphism.client.renderer.model;

import com.epimorphismmc.monomorphism.mixins.accessors.client.ItemModelGeneratorAccessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.ForgeFaceData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ItemCustomLayerModel implements IUnbakedGeometry<ItemCustomLayerModel> {
    @Nullable
    private ImmutableList<Material> textures;
    private final Int2ObjectMap<ForgeFaceData> layerData;
    private final Int2ObjectMap<ResourceLocation> renderTypeNames;
    private final float[][] layerPos;

    private ItemCustomLayerModel(@Nullable ImmutableList<Material> textures, Int2ObjectMap<ForgeFaceData> layerData, float[][] layerPos, Int2ObjectMap<ResourceLocation> renderTypeNames) {
        this.textures = textures;
        this.layerData = layerData;
        this.layerPos = layerPos;
        this.renderTypeNames = renderTypeNames;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        if (textures == null) {
            ImmutableList.Builder<Material> builder = ImmutableList.builder();
            for (int i = 0; context.hasMaterial("layer" + i); i++) {
                builder.add(context.getMaterial("layer" + i));
            }
            textures = builder.build();
        }

        TextureAtlasSprite particle = spriteGetter.apply(
                context.hasMaterial("particle") ? context.getMaterial("particle") : textures.get(0)
        );
        var rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity())
            modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);

        var normalRenderTypes = new RenderTypeGroup(RenderType.cutout(), ForgeRenderTypes.ITEM_LAYERED_CUTOUT.get());
        CompositeModel.Baked.Builder builder = CompositeModel.Baked.builder(context, particle, overrides, context.getTransforms());
        for (int i = 0; i < textures.size(); i++) {
            TextureAtlasSprite sprite = spriteGetter.apply(textures.get(i));
            var unbaked = createUnbakedCustomItemElements(i, layerPos[i], sprite.contents(), this.layerData.get(i));
            var quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState, modelLocation);
            var renderTypeName = renderTypeNames.get(i);
            var renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : null;
            builder.addQuads(renderTypes != null ? renderTypes : normalRenderTypes, quads);
        }

        textures = null;
        return builder.build();
    }

    public static final class Loader implements IGeometryLoader<ItemCustomLayerModel> {
        public static final Loader INSTANCE = new Loader();

        @Override
        public ItemCustomLayerModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            var renderTypeNames = new Int2ObjectOpenHashMap<ResourceLocation>();
            if (jsonObject.has("render_types")) {
                var renderTypes = jsonObject.getAsJsonObject("render_types");
                for (Map.Entry<String, JsonElement> entry : renderTypes.entrySet()) {
                    var renderType = new ResourceLocation(entry.getKey());
                    for (var layer : entry.getValue().getAsJsonArray())
                        if (renderTypeNames.put(layer.getAsInt(), renderType) != null)
                            throw new JsonParseException("Registered duplicate render type for layer " + layer);
                }
            }

            var emissiveLayers = new Int2ObjectArrayMap<ForgeFaceData>();
            if(jsonObject.has("forge_data")) {
                JsonObject forgeData = jsonObject.get("forge_data").getAsJsonObject();
                readLayerData(forgeData, renderTypeNames, emissiveLayers, false);
            }


            float[][] list = !jsonObject.has("layer_pos") ? null :
                    LDLib.GSON.fromJson(jsonObject.getAsJsonArray("layer_pos"), float[][].class);
            return new ItemCustomLayerModel(null, emissiveLayers, list, renderTypeNames);
        }

        private void readLayerData(JsonObject jsonObject, Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames, Int2ObjectMap<ForgeFaceData> layerData, boolean logWarning) {
            if (!jsonObject.has("layers")) return;

            var fullbrightLayers = jsonObject.getAsJsonObject("layers");
            for (var entry : fullbrightLayers.entrySet()) {
                int layer = Integer.parseInt(entry.getKey());
                var data = ForgeFaceData.read(entry.getValue(), ForgeFaceData.DEFAULT);
                layerData.put(layer, data);
            }
        }
    }

    private List<BlockElement> createUnbakedCustomItemElements(int layerIndex, float[] pos, SpriteContents spriteContents, @Nullable ForgeFaceData faceData) {
        var texture= "layer" + layerIndex;
        Map<Direction, BlockElementFace> map = Maps.newHashMap();
        map.put(Direction.SOUTH, new BlockElementFace(null, layerIndex, texture, new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0)));
        map.put(Direction.NORTH, new BlockElementFace(null, layerIndex, texture, new BlockFaceUV(new float[]{16.0F, 0.0F, 0.0F, 16.0F}, 0)));
        List<BlockElement> elements = Lists.newArrayList();
        elements.add(new BlockElement(new Vector3f(pos[0], pos[1], 7.5F - 0.001F * layerIndex), new Vector3f(pos[2], pos[3], 8.5F + 0.001F * layerIndex), map, null, false));
        elements.addAll(createSideElements(spriteContents, texture, pos, layerIndex));
        if(faceData != null) {
            elements.forEach(element -> element.setFaceData(faceData));
        }
        return elements;
    }

    private List<BlockElement> createSideElements(SpriteContents sprite, String texture, float[] pos, int tintIndex) {
        var generator = ((ItemModelGeneratorAccessor) ModelFactory.ITEM_MODEL_GENERATOR);
        float spriteWidth = (float)sprite.width();
        float spriteHeight = (float)sprite.height();
        List<BlockElement> list = Lists.newArrayList();

        for (ItemModelGenerator.Span span : generator.callGetSpans(sprite)) {
            float h = 0.0F;
            float i = 0.0F;
            float j = 0.0F;
            float k = 0.0F;
            float l = 0.0F;
            float m = 0.0F;
            float n = 0.0F;
            float o = 0.0F;
            float p = Math.abs(pos[0] - pos[2]) / spriteWidth;
            float q = Math.abs(pos[1] - pos[3]) / spriteWidth;
            float r = (float) span.getMin();
            float s = (float) span.getMax();
            float t = (float) span.getAnchor();

            ItemModelGenerator.SpanFacing spanFacing = span.getFacing();
            switch (spanFacing) {
                case UP:
                    l = r;
                    h = r;
                    j = m = s + 1.0F;
                    n = t;
                    i = t;
                    k = t;
                    o = t + 1.0F;
                    break;
                case DOWN:
                    n = t;
                    o = t + 1.0F;
                    l = r;
                    h = r;
                    j = m = s + 1.0F;
                    i = t + 1.0F;
                    k = t + 1.0F;
                    break;
                case LEFT:
                    l = t;
                    h = t;
                    j = t;
                    m = t + 1.0F;
                    o = r;
                    i = r;
                    k = n = s + 1.0F;
                    break;
                case RIGHT:
                    l = t;
                    m = t + 1.0F;
                    h = t + 1.0F;
                    j = t + 1.0F;
                    o = r;
                    i = r;
                    k = n = s + 1.0F;
            }

            h *= p;
            j *= p;
            i *= q;
            k *= q;
            i = 16.0F - i;
            k = 16.0F - k;
            p = 16.0F / spriteHeight;
            q = 16.0F / spriteHeight;
            l *= p;
            m *= p;
            n *= q;
            o *= q;
            Map<Direction, BlockElementFace> map = Maps.newHashMap();
            map.put(spanFacing.getDirection(), new BlockElementFace(null, tintIndex, texture, new BlockFaceUV(new float[]{l, n, m, o}, 0)));
            switch (spanFacing) {
                case UP:
                    list.add(new BlockElement(new Vector3f(h + pos[0], i - pos[0], 7.5F), new Vector3f(j + pos[0], i - pos[0], 8.5F), map, null, true));
                    break;
                case DOWN:
                    list.add(new BlockElement(new Vector3f(h + pos[0], k - pos[0], 7.5F), new Vector3f(j + pos[0], k - pos[0], 8.5F), map, null, true));
                    break;
                case LEFT:
                    list.add(new BlockElement(new Vector3f(h + pos[0], i - pos[0], 7.5F), new Vector3f(h + pos[0], k - pos[0], 8.5F), map, null, true));
                    break;
                case RIGHT:
                    list.add(new BlockElement(new Vector3f(j + pos[0], i - pos[0], 7.5F), new Vector3f(j + pos[0], k - pos[0], 8.5F), map, null, true));
            }
        }

        return list;
    }
}
