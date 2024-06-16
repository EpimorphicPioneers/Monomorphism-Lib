package com.epimorphismmc.monomorphism.client.utils;

/*
 * Referenced some code from Immersive Engineering
 *
 * https://github.com/BluSunrize/ImmersiveEngineering
 * */

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

    private static TextureAtlasSprite missingSprite;
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

    public static Minecraft getMC() {
        return Minecraft.getInstance();
    }

    /**
     * Fetches Minecraft's Font Renderer
     *
     * @return the FontRenderer object
     */
    public static Font getFontRenderer() {
        return getMC().font;
    }

    public static Camera getMainCamera() {
        return getGameRenderer().getMainCamera();
    }

    /**
     * Fetches Minecraft's Item Renderer
     *
     * @return the ItemRenderer object
     */
    public static ItemRenderer getItemRenderer() {
        return getMC().getItemRenderer();
    }

    /**
     * Fetches the IBakedModel for a BlockState
     *
     * @param state the BlockState
     * @return the IBakedModel
     */
    public static BakedModel getModelForState(BlockState state) {
        return getBlockRendererDispatcher().getBlockModel(state);
    }

    /**
     * @return an ItemModelGenerator object
     */
    public static ItemModelGenerator getItemModelGenerator() {
        return ITEM_MODEL_GENERATOR;
    }

    /**
     * Fetches Minecraft's Entity Rendering Manager
     *
     * @return the EntityRendererManager object
     */
    public static EntityRenderDispatcher getEntityRendererManager() {
        return getMC().getEntityRenderDispatcher();
    }

    /**
     * Fetches Minecraft's Block Renderer Dispatcher
     *
     * @return the BlockRendererDispatcher object
     */
    public static BlockRenderDispatcher getBlockRendererDispatcher() {
        return getMC().getBlockRenderer();
    }

    /**
     * Fetches Minecraft's Block Model Renderer
     *
     * @return the BlockModelRenderer object
     */
    public static ModelBlockRenderer getBlockRenderer() {
        return getBlockRendererDispatcher().getModelRenderer();
    }

    public static GameRenderer getGameRenderer() {
        return getMC().gameRenderer;
    }

    /**
     * Converts a String to a RenderMaterial for the Block Atlas
     * @param string the String
     * @return the RenderMaterial
     */
    public static Material getRenderMaterial(String string) {
        return getRenderMaterial(new ResourceLocation(string));
    }

    /**
     * Converts a ResourceLocation to a RenderMaterial for the Block Atlas
     * @param texture the ResourceLocation
     * @return the RenderMaterial
     */
    public static Material getRenderMaterial(ResourceLocation texture) {
        return new Material(getTextureAtlasLocation(), texture);
    }

    /**
     * @return the TextureAtlasSprite for the missing texture
     */
    public static TextureAtlasSprite getMissingSprite() {
        if (missingSprite == null) {
            missingSprite = getSprite(MissingTextureAtlasSprite.getLocation());
        }
        return missingSprite;
    }

    /**
     * Fetches the sprite on the Texture Atlas related to a Resource Location
     *
     * @param location the Resource Location
     * @return the sprite
     */
    public static TextureAtlasSprite getSprite(ResourceLocation location) {
        return getTextureAtlas().getSprite(location);
    }

    /**
     * Fetches the sprite on a Texture Atlas related to a render material
     *
     * @param material the render material
     * @return the sprite
     */
    public static TextureAtlasSprite getSprite(Material material) {
        return getTextureAtlas(material.atlasLocation()).getSprite(material.texture());
    }

    /**
     * Binds a texture for rendering
     *
     * @param location the ResourceLocation for the texture
     */
    public static void bindTexture(ResourceLocation location) {
        RenderSystem.setShaderTexture(0, location);
    }

    /**
     * Binds the texture atlas for rendering
     */
    public static void bindTextureAtlas() {
        bindTexture(getTextureAtlasLocation());
    }

    /**
     * Fetches the AtlasTexture object representing the Texture Atlas
     *
     * @return the AtlasTexture object
     */
    public static TextureAtlas getTextureAtlas() {
        return getTextureAtlas(getTextureAtlasLocation());
    }

    /**
     * Fetches the AtlasTexture object representing the Texture Atlas
     *
     * @param location the location for the atlas
     * @return the AtlasTexture object
     */
    public static TextureAtlas getTextureAtlas(ResourceLocation location) {
        return getModelManager().getAtlas(location);
    }

    /**
     * Fetches the ResourceLocation associated with the Texture Atlas
     *
     * @return ResourceLocation for the Texture Atlas
     */
    public static ResourceLocation getTextureAtlasLocation() {
        return InventoryMenu.BLOCK_ATLAS;
    }

    /**
     * Fetches Minecraft's Texture Manager
     *
     * @return the TextureManager object
     */
    public static TextureManager getTextureManager() {
        return getMC().getTextureManager();
    }

    /**
     * Fetches Minecraft's Model Manager
     *
     * @return the ModelManager object
     */
    public static ModelManager getModelManager() {
        return getMC().getModelManager();
    }

    public static BakedModel getBakedModel(ResourceLocation resourceLocation) {
        return getModelManager().getModel(resourceLocation);
    }

    public static float getPartialTick() {
        return getMC().getFrameTime();
    }

    /**
     * Fetches the Player's current Camera Orientation
     *
     * @return a Quaternion object representing the orientation of the camera
     */
    public static Quaternionf getCameraOrientation() {
        return getEntityRendererManager().cameraOrientation();
    }

    /**
     * Fetches the Player's current Point of View (First Person, Third Person over shoulder, Third Person front)
     *
     * @return the PointOfView object
     */
    public static CameraType getPointOfView() {
        return getEntityRendererManager().options.getCameraType();
    }

    /**
     * @return The width in pixels of the Minecraft window
     */
    public static int getScaledWindowWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }
    /**
     * @return The height in pixels of the Minecraft window
     */
    public static int getScaledWindowHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
