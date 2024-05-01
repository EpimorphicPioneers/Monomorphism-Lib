package com.epimorphismmc.monomorphism.client.utils;

/*
 * Referenced some code from Immersive Engineering
 *
 * https://github.com/BluSunrize/ImmersiveEngineering
 * */

import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Transformation;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.*;

import javax.annotation.Nullable;
import java.lang.Math;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderHelper {
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

    /**
     * @return The render type buffer implementation
     */
    public static MultiBufferSource.BufferSource getRenderTypeBuffer() {
        return getMC().renderBuffers().bufferSource();
    }

    /**
     * Fetches a vertex builder for a RenderType, fetches the IRenderTypeBuffer internally
     * @param renderType the RenderType
     * @return an IVertexBuilder
     */
    public static VertexConsumer getVertexBuilder(RenderType renderType) {
        return getVertexBuilder(getRenderTypeBuffer(), renderType);
    }

    /**
     * Fetches a vertex builder from an IRenderTypeBuffer for a RenderType
     * @param buffer the IRenderTypeBuffer
     * @param renderType the RenderType
     * @return an IVertexBuilder
     */
    public static VertexConsumer getVertexBuilder(MultiBufferSource buffer, RenderType renderType) {
        return buffer.getBuffer(renderType);
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
        return Objects.ITEM_MODEL_GENERATOR;
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
        if (Objects.missingSprite == null) {
            Objects.missingSprite = getSprite(MissingTextureAtlasSprite.getLocation());
        }
        return Objects.missingSprite;
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

//    /**
//     * Renders an item
//     */
//    public static void renderItem(ItemStack stack, ItemDisplayContext transformType, int light,
//                            PoseStack transforms, MultiBufferSource buffer) {
//        renderItem(stack, transformType, light, OverlayTexture.NO_OVERLAY, transforms, buffer);
//    }

//    /**
//     * Renders an item
//     */
//    public static void renderItem(ItemStack stack, ItemDisplayContext transformType, int light, int overlay,
//                            PoseStack transforms, MultiBufferSource buffer) {
//        getItemRenderer().renderStatic(stack, transformType, light, overlay, transforms, buffer, 0);
//    }

//    /**
//     * Renders a block state
//     */
//    public static boolean renderBlockState(BlockState state, PoseStack transforms, VertexConsumer buffer) {
//        return renderBlockState(state, Objects.DEFAULT_POS, transforms, buffer, OverlayTexture.NO_OVERLAY);
//    }

//    /**
//     * Renders a block state
//     */
//    public static boolean renderBlockState(BlockState state, BlockPos pos, PoseStack transforms, VertexConsumer buffer, int overlay) {
//        Level world =InfinityLib.instance.getClientWorld();
//        return renderBlockModel(world, getModelForState(state), state, pos,
//                transforms, buffer, false, world.getRandom(), state.getSeed(pos), overlay, ModelData.EMPTY);
//    }

//    /**
//     * Renders a block model
//     */
//    public static boolean renderBlockModel(BlockAndTintGetter world, BakedModel model, BlockState state, BlockPos pos, PoseStack transforms,
//                                     VertexConsumer buffer, boolean checkSides, Random random, long rand, int overlay, ModelData modelData) {
//        return getBlockRenderer().tesselateBlock(world, model, state, pos, transforms, buffer, checkSides, random, rand, overlay, modelData);
//    }
    
    public static void renderFluid(PoseStack poseStack, MultiBufferSource buffer, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, FluidStack fluidStack) {
        if (fluidStack.isEmpty()) return;

        var fluidTexture = FluidHelper.getStillTexture(fluidStack);
        if (fluidTexture == null) {
            fluidTexture = ModelFactory.getBlockSprite(MissingTextureAtlasSprite.getLocation());
        }
        poseStack.pushPose();
        VertexConsumer builder = buffer.getBuffer(Sheets.translucentCullBlockSheet());
        RenderBufferUtils.renderCubeFace(poseStack, builder, minX, minY, minZ, maxX, maxY, maxZ, FluidHelper.getColor(fluidStack) | 0xff000000, LightTexture.FULL_BRIGHT, fluidTexture);
        poseStack.popPose();
    }

    /**
     * Method to render the coordinate system for the current matrix. Renders three lines with
     * length 1 starting from (0, 0, 0): red line along x axis, green line along y axis and blue
     * line along z axis.
     */
    public static void renderCoordinateSystem(PoseStack transforms, MultiBufferSource buffer) {
        VertexConsumer builder = getVertexBuilder(buffer, RenderType.lines());
        Matrix4f matrix = transforms.last().pose();
        // X-axis
        builder.vertex(matrix, 0, 0, 0).color(255, 0, 0, 255).endVertex();
        builder.vertex(matrix, 1, 0, 0).color(255, 0, 0, 255).endVertex();
        // Y-axis
        builder.vertex(matrix, 0, 0, 0).color(0, 255, 0, 255).endVertex();
        builder.vertex(matrix, 0, 1, 0).color(0, 255, 0, 255).endVertex();
        // Z-axis
        builder.vertex(matrix, 0, 0, 0).color(0, 0, 255, 255).endVertex();
        builder.vertex(matrix, 0, 0, 1).color(0, 0, 255, 255).endVertex();
    }
    
    // Rotation
    public static Quaternionf degreeToQuaterion(double x, double y, double z) {
        x = Math.toRadians(x);
        y = Math.toRadians(y);
        z = Math.toRadians(z);
        Quaternionf qYaw = new Quaternionf(0, (float)Math.sin(y/2), 0, (float)Math.cos(y/2));
        Quaternionf qPitch = new Quaternionf((float)Math.sin(x/2), 0, 0, (float)Math.cos(x/2));
        Quaternionf qRoll = new Quaternionf(0, 0, (float)Math.sin(z/2), (float)Math.cos(z/2));

        qYaw.mul(qRoll);
        qYaw.mul(qPitch);
        return qYaw;
    }

    public static Transformation rotateTo(Direction d) {
        return new Transformation(null).compose(ModelFactory.getRotation(d).getRotation());
    }

    /**
     * Internal class to store pointers to objects which need initialization
     */
    final class Objects {
        private Objects() {/**/}

        private static TextureAtlasSprite missingSprite;

        private static final BlockPos DEFAULT_POS = new BlockPos(0,0,0);

        private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

        public static boolean equals(Object a, Object b) {
            return java.util.Objects.equals(a, b);
        }
    }
}
