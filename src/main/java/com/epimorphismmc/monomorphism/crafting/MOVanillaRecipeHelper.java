package com.epimorphismmc.monomorphism.crafting;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.item.tool.ToolHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MOVanillaRecipeHelper {

    public static void addRepairRecipe(
            Consumer<FinishedRecipe> provider,
            @NotNull ResourceLocation regName,
            @NotNull ItemStack requireRepair,
            int repairDamage,
            @NotNull Object... recipe) {
        var builder = new ItemRepairRecipeBuilder(regName);
        builder.repairDamage(repairDamage);
        builder.requireRepair(requireRepair);
        for (Object content : recipe) {
            if (content instanceof Ingredient ingredient) {
                builder.requires(ingredient);
            } else if (content instanceof ItemStack itemStack) {
                builder.requires(itemStack);
            } else if (content instanceof TagKey<?> key) {
                builder.requires((TagKey<Item>) key);
            } else if (content instanceof ItemLike itemLike) {
                builder.requires(itemLike);
            } else if (content instanceof UnificationEntry entry) {
                TagKey<Item> tag = ChemicalHelper.getTag(entry.tagPrefix, entry.material);
                if (tag != null) {
                    builder.requires(tag);
                } else builder.requires(ChemicalHelper.get(entry.tagPrefix, entry.material));
            } else {
                if (content instanceof Character c) {
                    builder.requires(ToolHelper.getToolFromSymbol(c).itemTags.get(0));
                }
            }
        }
        builder.save(provider);
    }
}
