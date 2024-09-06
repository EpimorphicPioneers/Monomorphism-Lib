package com.epimorphismmc.monomorphism.integration.gtm.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class MORecipeHelper {

    public static Content itemContent(
            ItemStack itemStack, int chance, int maxChance, int tierChanceBoost) {
        return new Content(
                ItemRecipeCapability.CAP.of(itemStack), chance, maxChance, tierChanceBoost, null, null);
    }

    public static Content itemContent(
            Item item, int amount, int chance, int maxChance, int tierChanceBoost) {
        return new Content(
                ItemRecipeCapability.CAP.of(new ItemStack(item, amount)),
                chance,
                maxChance,
                tierChanceBoost,
                null,
                null);
    }

    public static Content fluidContent(
            FluidStack fluidStack, int chance, int maxChance, int tierChanceBoost) {
        return new Content(
                FluidRecipeCapability.CAP.of(fluidStack), chance, maxChance, tierChanceBoost, null, null);
    }

    public static Content fluidContent(
            Fluid fluid, long amount, int chance, int maxChance, int tierChanceBoost) {
        return new Content(
                FluidRecipeCapability.CAP.of(FluidStack.create(fluid, amount)),
                chance,
                maxChance,
                tierChanceBoost,
                null,
                null);
    }
}
