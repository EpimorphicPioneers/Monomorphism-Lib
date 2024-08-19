package com.epimorphismmc.monomorphism.utility;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.common.data.GTRecipeCapabilities;

import com.lowdragmc.lowdraglib.misc.ItemTransferList;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;

import net.minecraft.world.item.ItemStack;

import com.google.common.collect.Table;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNullElseGet;

public class MOTransferUtils {

    public static FluidStack drainFluid(
            Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy,
            FluidStack stack,
            boolean simulate) {
        List<IRecipeHandler<?>> handlers = requireNonNullElseGet(
                capabilitiesProxy.get(IO.IN, GTRecipeCapabilities.FLUID), Collections::emptyList);
        List<?> list = List.of(FluidIngredient.of(stack));
        for (var handler : handlers) {
            list = requireNonNullElseGet(
                    handler.handleRecipe(IO.IN, null, list, null, simulate), Collections::emptyList);
        }

        if (!list.isEmpty()) {
            stack.shrink(((FluidIngredient) list.get(0)).getAmount());
        }

        return stack;
    }
}
