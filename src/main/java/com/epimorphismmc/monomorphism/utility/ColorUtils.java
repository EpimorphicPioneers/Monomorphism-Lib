package com.epimorphismmc.monomorphism.utility;

import net.minecraft.util.FastColor;

public class ColorUtils {
    public static class ARGB32 {
        private ARGB32() {}

        public static float alpha(int packedColor) {
            return FastColor.ARGB32.alpha(packedColor) / 255.0F;
        }

        public static float red(int packedColor) {
            return FastColor.ARGB32.red(packedColor) / 255.0F;
        }

        public static float green(int packedColor) {
            return FastColor.ARGB32.green(packedColor) / 255.0F;
        }

        public static float blue(int packedColor) {
            return FastColor.ARGB32.blue(packedColor) / 255.0F;
        }

        public static int color(float alpha, float red, float green, float blue) {
            return FastColor.ARGB32.color(
                    (int) (alpha * 255), (int) (red * 255), (int) (green * 255), (int) (blue * 255));
        }
    }

    public static class ABGR32 {
        private ABGR32() {}

        public static float alpha(int packedColor) {
            return FastColor.ABGR32.alpha(packedColor) / 255.0F;
        }

        public static float blue(int packedColor) {
            return FastColor.ABGR32.blue(packedColor) / 255.0F;
        }

        public static float green(int packedColor) {
            return FastColor.ABGR32.green(packedColor) / 255.0F;
        }

        public static float red(int packedColor) {
            return FastColor.ABGR32.red(packedColor) / 255.0F;
        }

        public static int color(float alpha, float blue, float green, float red) {
            return FastColor.ABGR32.color(
                    (int) (alpha * 255), (int) (blue * 255), (int) (green * 255), (int) (red * 255));
        }
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 1 -> {
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                }
                case 2 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                }
                case 3 -> {
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                }
                case 5 -> {
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                }
            }
        }
        return 0xff000000 | (r << 16) | (g << 8) | (b);
    }

    /**
     * all components should in [0-1]
     */
    public static float[] RGBtoHSB(int color) {
        int r = ((color >> 16) & 0xff);
        int g = ((color >> 8) & 0xff);
        int b = ((color) & 0xff);

        float hue, saturation, brightness;

        int cmax = Math.max(r, g);
        if (b > cmax) cmax = b;
        int cmin = Math.min(r, g);
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0) saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else saturation = 0;
        if (saturation == 0) hue = 0;
        else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax) hue = bluec - greenc;
            else if (g == cmax) hue = 2.0f + redc - bluec;
            else hue = 4.0f + greenc - redc;
            hue /= 6.0f;
            if (hue < 0) hue += 1.0f;
        }
        return new float[] {hue, saturation, brightness};
    }
}
