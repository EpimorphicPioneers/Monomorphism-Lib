package com.epimorphismmc.monomorphism.gui.utils;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.getFontRenderer;

public class MOWidgetUtil {
    public static IGuiTexture createTextOverlay(String translationKey) {
        return new IGuiTexture() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public void draw(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
                graphics.pose().pushPose();
                graphics.pose().translate(0, 0, 400);
                graphics.pose().scale(0.5f, 0.5f, 1);
                String s = LocalizationUtils.format(translationKey);
                Font fontRenderer = getFontRenderer();
                graphics.drawString(fontRenderer, s, (int) ((x + (width / 3f)) * 2 - fontRenderer.width(s) + 23), (int) ((y + (height / 3f) + 6) * 2 - height), 0xff0000, true);
                graphics.pose().popPose();
            }
        };
    }
}
