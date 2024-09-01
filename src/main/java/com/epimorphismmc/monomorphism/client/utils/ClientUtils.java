package com.epimorphismmc.monomorphism.client.utils;

import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    /**
     * Fetches Minecraft's Font Renderer
     *
     * @return the FontRenderer object
     */
    public static Font fontRenderer() {
        return mc().font;
    }

    /**
     * Fetches Minecraft's Item Renderer
     *
     * @return the ItemRenderer object
     */
    public static ItemRenderer itemRenderer() {
        return mc().getItemRenderer();
    }

    /**
     * Fetches Minecraft's Entity Rendering Manager
     *
     * @return the EntityRendererManager object
     */
    public static EntityRenderDispatcher entityRendererManager() {
        return mc().getEntityRenderDispatcher();
    }

    /**
     * Fetches Minecraft's Block Renderer Dispatcher
     *
     * @return the BlockRendererDispatcher object
     */
    public static BlockRenderDispatcher blockRendererDispatcher() {
        return mc().getBlockRenderer();
    }

    /**
     * Fetches Minecraft's Block Model Renderer
     *
     * @return the BlockModelRenderer object
     */
    public static ModelBlockRenderer blockRenderer() {
        return blockRendererDispatcher().getModelRenderer();
    }

    public static GameRenderer gameRenderer() {
        return mc().gameRenderer;
    }

    /**
     * Fetches Minecraft's Texture Manager
     *
     * @return the TextureManager object
     */
    public static TextureManager textureManager() {
        return mc().getTextureManager();
    }

    public static float getPartialTick() {
        return mc().getFrameTime();
    }

    public static Camera mainCamera() {
        return gameRenderer().getMainCamera();
    }

    /**
     * Fetches the Player's current Camera Orientation
     *
     * @return a Quaternion object representing the orientation of the camera
     */
    public static Quaternionf getCameraOrientation() {
        return entityRendererManager().cameraOrientation();
    }

    /**
     * Fetches the Player's current Point of View (First Person, Third Person over shoulder, Third Person front)
     *
     * @return the PointOfView object
     */
    public static CameraType getPointOfView() {
        return entityRendererManager().options.getCameraType();
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

    public static @Nullable ClientPacketListener connection() {
        return mc().getConnection();
    }
}
