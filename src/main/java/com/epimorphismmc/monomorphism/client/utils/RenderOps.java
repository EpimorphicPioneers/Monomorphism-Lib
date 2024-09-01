package com.epimorphismmc.monomorphism.client.utils;

import com.epimorphismmc.monomorphism.utility.ColorUtils;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Common rendering operations.
 *
 * @author GateGuardian
 * @date : 2024/8/30
 */
@OnlyIn(Dist.CLIENT)
public class RenderOps {
    /**
     * Binds a texture for rendering
     *
     * @param location the ResourceLocation for the texture
     */
    public static void bindTexture(ResourceLocation location) {
        RenderSystem.setShaderTexture(0, location);
    }

    /**
     * Binds the block texture atlas for rendering
     */
    public static void bindBlockAtlas() {
        bindTexture(InventoryMenu.BLOCK_ATLAS);
    }

    public static void setPositionShader() {
        RenderSystem.setShader(GameRenderer::getPositionShader);
    }

    public static void setPositionColorShader() {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
    }

    public static void getPositionColorTexShader() {
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
    }

    public static void setPositionTexShader() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
    }

    public static void setPositionTexColorShader() {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
    }

    public static void setColor(int argb) {
        float r = ColorUtils.ARGB32.red(argb);
        float g = ColorUtils.ARGB32.green(argb);
        float b = ColorUtils.ARGB32.blue(argb);
        float a = ColorUtils.ARGB32.alpha(argb);
        RenderSystem.setShaderColor(r, g, b, a);
    }
}
