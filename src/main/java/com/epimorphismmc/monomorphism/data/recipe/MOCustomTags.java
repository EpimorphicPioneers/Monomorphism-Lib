package com.epimorphismmc.monomorphism.data.recipe;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.gregtechceu.gtceu.data.recipe.CustomTags.*;

@SuppressWarnings("unchecked")
public class MOCustomTags {

    public static final TagKey<Item>[] CIRCUITS = new TagKey[] {
        ULV_CIRCUITS,
        LV_CIRCUITS,
        MV_CIRCUITS,
        HV_CIRCUITS,
        EV_CIRCUITS,
        IV_CIRCUITS,
        LuV_CIRCUITS,
        ZPM_CIRCUITS,
        UV_CIRCUITS,
        UHV_CIRCUITS,
        UEV_CIRCUITS,
        UIV_CIRCUITS,
        UXV_CIRCUITS,
        OpV_CIRCUITS,
        MAX_CIRCUITS
    };

    public static final TagKey<Item>[] BATTERIES = new TagKey[] {
        ULV_BATTERIES,
        LV_BATTERIES,
        MV_BATTERIES,
        HV_BATTERIES,
        EV_BATTERIES,
        IV_BATTERIES,
        LuV_BATTERIES,
        ZPM_BATTERIES,
        UV_BATTERIES,
        UHV_BATTERIES
    };
}
