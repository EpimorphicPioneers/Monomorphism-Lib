package com.epimorphismmc.monomorphism.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class InputUtils {
    public static boolean isShiftDown() {
        var id = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_SHIFT) ||
                InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_SHIFT);
    }

    public static boolean isCtrlDown() {
        var id = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_CONTROL) ||
                InputConstants.isKeyDown(id, GLFW.GLFW_KEY_RIGHT_CONTROL);
    }

    public static boolean isAltDown() {
        var id = Minecraft.getInstance().getWindow().getWindow();
        return InputConstants.isKeyDown(id, GLFW.GLFW_KEY_LEFT_ALT) ||
                InputConstants.isKeyDown(id, GLFW.GLFW_KEY_RIGHT_ALT);
    }
}
