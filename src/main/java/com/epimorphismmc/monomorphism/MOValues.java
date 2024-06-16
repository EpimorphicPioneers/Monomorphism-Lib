package com.epimorphismmc.monomorphism;

import static net.minecraft.ChatFormatting.*;

public class MOValues {

    /**
     * The number of units in a block.
     */
    public static final int WHOLE = 16;

    /**
     * The value of 1/16 as represented in float form. Pre-calculated as to cut
     * back on calculations.
     */
    public static final float UNIT = 1.0f / WHOLE;

    /**
     * The value of half a block. Pre-calculated as to cut
     * back on calculations.
     */
    public static final float HALF = UNIT * WHOLE / 2;

    /**
     * The value of quarter a block. Pre-calculated as to cut
     * back on calculations.
     */
    public static final float QUARTER = UNIT * WHOLE / 4;

    /**
     * The value of three quarter a block. Pre-calculated as to cut
     * back on calculations.
     */
    public static final float THREE_QUARTER = 3 * QUARTER;

    public static final int K = (int) Math.pow(10, 3),
            M = (int) Math.pow(10, 6),
            G = (int) Math.pow(10, 9),
            T = (int) Math.pow(10, 12),
            P = (int) Math.pow(10, 15);

    public static final String[] CVLVH = new String[] {
        "蒸汽", // not doing the gray color for these first two because it looks weird
        "基础",
        AQUA + "进阶",
        GOLD + "进阶",
        DARK_PURPLE + "进阶",
        BLUE + "精英",
        LIGHT_PURPLE + "精英",
        RED + "精英",
        DARK_AQUA + "终极",
        DARK_RED + "史诗",
        GREEN + "史诗",
        DARK_GREEN + "史诗",
        YELLOW + "史诗",
        BLUE.toString() + BOLD + "传奇",
        RED.toString() + BOLD + "MAX"
    };
}
