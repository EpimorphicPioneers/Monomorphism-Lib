package com.epimorphismmc.monomorphism.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class MORecipeHelper {

    public static Content itemContent(ItemStack itemStack, float chance, float tierChanceBoost) {
        return new Content(ItemRecipeCapability.CAP.of(itemStack), chance, tierChanceBoost, null, null);
    }

    public static Content itemContent(Item item, int amount, float chance, float tierChanceBoost) {
        return new Content(
                ItemRecipeCapability.CAP.of(new ItemStack(item, amount)),
                chance,
                tierChanceBoost,
                null,
                null);
    }

    public static Content fluidContent(FluidStack fluidStack, float chance, float tierChanceBoost) {
        return new Content(
                FluidRecipeCapability.CAP.of(fluidStack), chance, tierChanceBoost, null, null);
    }

    public static Content fluidContent(
            Fluid fluid, long amount, float chance, float tierChanceBoost) {
        return new Content(
                FluidRecipeCapability.CAP.of(FluidStack.create(fluid, amount)),
                chance,
                tierChanceBoost,
                null,
                null);
    }
}
