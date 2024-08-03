package com.epimorphismmc.monomorphism.gui.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.fontRenderer;

@OnlyIn(Dist.CLIENT)
public class MODrawerHelper {
    public static void renderStackCount(GuiGraphics guiGraphics, String count, int x, int y) {
        var font = fontRenderer();
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 200.0F);
        float scale = Math.min(1f, (float) 16 / font.width(count));
        if (scale < 1f) {
            poseStack.scale(scale, scale, 1.0F);
        }
        font.drawInBatch(
                count,
                (x + 19 - 2 - (font.width(count) * scale)) / scale,
                (y + 6 + 3 + (1 / (scale * scale) - 1)) / scale,
                16777215,
                true,
                poseStack.last().pose(),
                guiGraphics.bufferSource(),
                Font.DisplayMode.NORMAL,
                0,
                LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }
}
