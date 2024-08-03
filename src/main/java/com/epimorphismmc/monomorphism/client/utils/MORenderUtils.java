package com.epimorphismmc.monomorphism.client.utils;

import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

import static com.epimorphismmc.monomorphism.client.model.ModelAssistant.getModelForState;
import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.*;
import static com.epimorphismmc.monomorphism.client.utils.MORenderBufferUtils.*;

@OnlyIn(Dist.CLIENT)
public class MORenderUtils {

    private static final BlockPos DEFAULT_POS = new BlockPos(0, 0, 0);

    /**
     * Renders an item
     */
    public static void renderItem(
            ItemStack stack,
            ItemDisplayContext transformType,
            int light,
            PoseStack transforms,
            MultiBufferSource buffer) {
        renderItem(stack, transformType, light, OverlayTexture.NO_OVERLAY, transforms, buffer);
    }

    /**
     * Renders an item
     */
    public static void renderItem(
            ItemStack stack,
            ItemDisplayContext transformType,
            int light,
            int overlay,
            PoseStack transforms,
            MultiBufferSource buffer) {
        itemRenderer().renderStatic(stack, transformType, light, overlay, transforms, buffer, null, 0);
    }

    /**
     * Renders a block state
     */
    public static void renderBlockState(
            BlockState state, PoseStack transforms, VertexConsumer buffer, RenderType renderType) {
        renderBlockState(state, DEFAULT_POS, transforms, buffer, OverlayTexture.NO_OVERLAY, renderType);
    }

    /**
     * Renders a block state
     */
    public static void renderBlockState(
            BlockState state,
            BlockPos pos,
            PoseStack transforms,
            VertexConsumer buffer,
            int overlay,
            RenderType renderType) {
        Level level = mc().level;
        renderBlockModel(
                level,
                getModelForState(state),
                state,
                pos,
                transforms,
                buffer,
                false,
                level.getRandom(),
                state.getSeed(pos),
                overlay,
                ModelData.EMPTY,
                renderType);
    }

    /**
     * Renders a block model
     */
    public static void renderBlockModel(
            BlockAndTintGetter world,
            BakedModel model,
            BlockState state,
            BlockPos pos,
            PoseStack transforms,
            VertexConsumer buffer,
            boolean checkSides,
            RandomSource random,
            long rand,
            int overlay,
            ModelData modelData,
            RenderType renderType) {
        blockRenderer()
                .tesselateBlock(
                        world,
                        model,
                        state,
                        pos,
                        transforms,
                        buffer,
                        checkSides,
                        random,
                        rand,
                        overlay,
                        modelData,
                        renderType);
    }

    public static void renderStillFluidInWorld(
            FluidStack fluid,
            Cuboid cuboid,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Camera camera,
            int combinedLight,
            int combinedOverlay,
            CuboidRenderer.FaceDisplay faceDisplay) {
        CuboidRenderer.renderCuboid(
                cuboid.prepStill(fluid).setColor(FluidHelper.getColor(fluid) | 0xff000000),
                poseStack,
                bufferSource.getBuffer(Sheets.translucentCullBlockSheet()),
                combinedLight,
                combinedOverlay,
                faceDisplay,
                camera,
                null);
    }

    public static void renderFlowingFluidInWorld(
            FluidStack fluid,
            Cuboid cuboid,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            Camera camera,
            int combinedLight,
            int combinedOverlay,
            CuboidRenderer.FaceDisplay faceDisplay) {
        CuboidRenderer.renderCuboid(
                cuboid.prepFlowing(fluid).setColor(FluidHelper.getColor(fluid) | 0xff000000),
                poseStack,
                bufferSource.getBuffer(Sheets.translucentCullBlockSheet()),
                combinedLight,
                combinedOverlay,
                faceDisplay,
                camera,
                null);
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
    public static void bindBlockAtlas() {
        bindTexture(InventoryMenu.BLOCK_ATLAS);
    }
}
