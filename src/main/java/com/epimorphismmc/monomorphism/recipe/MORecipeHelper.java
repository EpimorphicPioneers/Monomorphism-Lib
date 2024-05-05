package com.epimorphismmc.monomorphism.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MORecipeHelper {

    public static Content itemContent(ItemStack itemStack, float chance, float tierChanceBoost) {
        return new Content(ItemRecipeCapability.CAP.of(itemStack), chance, tierChanceBoost, null, null);
    }

    public static Content itemContent(Item item, int amount, float chance, float tierChanceBoost) {
        return new Content(ItemRecipeCapability.CAP.of(new ItemStack(item, amount)), chance, tierChanceBoost, null, null);
    }

    public static Content fluidContent(FluidStack fluidStack, float chance, float tierChanceBoost) {
        return new Content(FluidRecipeCapability.CAP.of(fluidStack), chance, tierChanceBoost, null, null);
    }

    public static Content fluidContent(Fluid fluid, long amount, float chance, float tierChanceBoost) {
        return new Content(FluidRecipeCapability.CAP.of(FluidStack.create(fluid, amount)), chance, tierChanceBoost, null, null);
    }

    /**
     * Those who use these methods should note that these methods do not guarantee that the returned values are valid,
     * because the relevant data, such as tag information, may not be loaded at the time these methods are called.
     * <p>
     * Recipe Builder methods for getting input items or fluids are not provided, as these data are not yet loaded when they are needed.
     */

    public static <T> List<T> getInputs(GTRecipeBuilder builder, RecipeCapability<T> capability) {
        return builder.input.getOrDefault(capability, Collections.emptyList()).stream()
                .map(content -> capability.of(content.getContent()))
                .collect(Collectors.toList());
    }

    public static <T> List<T> getInputs(GTRecipe recipe, RecipeCapability<T> capability) {
        return recipe.getInputContents(capability).stream()
                .map(content -> capability.of(content.getContent()))
                .collect(Collectors.toList());
    }

    public static <T> List<T> getOutputs(GTRecipeBuilder builder, RecipeCapability<T> capability) {
        return builder.output.getOrDefault(capability, Collections.emptyList()).stream()
                .map(content -> capability.of(content.getContent()))
                .collect(Collectors.toList());
    }

    public static <T> List<T> getOutputs(GTRecipe recipe, RecipeCapability<T> capability) {
        return recipe.getOutputContents(capability).stream()
                .map(content -> capability.of(content.getContent()))
                .collect(Collectors.toList());
    }

    public static List<ItemStack> getInputItem(GTRecipe recipe) {
        return recipe.getInputContents(ItemRecipeCapability.CAP).stream()
                .map(content -> ItemRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
                .collect(Collectors.toList());
    }

    public static List<FluidStack> getInputFluid(GTRecipe recipe) {
        return recipe.getInputContents(FluidRecipeCapability.CAP).stream()
                .map(content -> FluidRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getStacks()))
                .collect(Collectors.toList());
    }

    public static List<ItemStack> getOutputItem(GTRecipe recipe) {
        return recipe.getInputContents(ItemRecipeCapability.CAP).stream()
                .map(content -> ItemRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
                .collect(Collectors.toList());
    }

    public static List<ItemStack> getOutputItem(GTRecipeBuilder builder) {
        return builder.output.getOrDefault(ItemRecipeCapability.CAP, Collections.emptyList()).stream()
                .map(content -> ItemRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
                .collect(Collectors.toList());
    }

    public static List<FluidStack> getOutputFluid(GTRecipe recipe) {
        return recipe.getInputContents(FluidRecipeCapability.CAP).stream()
                .map(content -> FluidRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getStacks()))
                .collect(Collectors.toList());
    }

    public static List<FluidStack> getOutputFluid(GTRecipeBuilder builder) {
        return builder.output.getOrDefault(FluidRecipeCapability.CAP, Collections.emptyList()).stream()
                .map(content -> FluidRecipeCapability.CAP.of(content.getContent()))
                .flatMap(ingredient -> Arrays.stream(ingredient.getStacks()))
                .collect(Collectors.toList());
    }
}
