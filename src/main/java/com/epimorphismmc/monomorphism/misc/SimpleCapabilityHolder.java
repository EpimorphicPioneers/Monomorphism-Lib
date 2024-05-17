package com.epimorphismmc.monomorphism.misc;

import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.misc.IgnoreEnergyRecipeHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SimpleCapabilityHolder implements IRecipeCapabilityHolder {

    @Getter
    protected final List<ItemStack> items;
    @Getter
    protected final List<FluidStack> fluids;

    @Getter
    protected final Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy;

    public SimpleCapabilityHolder() {
        this.capabilitiesProxy = Tables.newCustomTable(new EnumMap<>(IO.class), HashMap::new);
        this.capabilitiesProxy.put(IO.IN, FluidRecipeCapability.CAP, List.of(new IRecipeHandler<FluidIngredient>() {

            @Override
            public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe gtRecipe, List<FluidIngredient> left, @Nullable String s, boolean simulate) {
                if (io != IO.IN) return left;
                var capabilities = simulate ? fluids.stream().map(FluidStack::copy).toList() : fluids;
                for (FluidStack capability : capabilities) {
                    Iterator<FluidIngredient> iterator = left.iterator();
                    while (iterator.hasNext()) {
                        FluidIngredient fluidStack = iterator.next();
                        if (fluidStack.isEmpty()) {
                            iterator.remove();
                            continue;
                        }

                        if (!fluidStack.test(capability)) {
                            continue;
                        }

                        long drained = Math.min(fluidStack.getAmount(), capability.getAmount());
                        capability.shrink(drained);
                        fluidStack.setAmount(fluidStack.getAmount() - drained);
                        if (fluidStack.getAmount() <= 0) {
                            iterator.remove();
                        }
                    }
                    if (left.isEmpty()) break;
                }
                return left.isEmpty() ? null : left;
            }

            @Override
            public List<Object> getContents() {
                List<FluidStack> ingredients = new ArrayList<>();
                for (FluidStack stack : fluids) {
                    if (!stack.isEmpty()) {
                        ingredients.add(stack);
                    }
                }
                return Arrays.asList(ingredients.toArray());
            }

            @Override
            public double getTotalContentAmount() {
                long amount = 0;
                for (ItemStack item : items) {
                    if (!item.isEmpty()) {
                        amount += item.getCount();
                    }
                }
                return amount;
            }

            @Override
            public RecipeCapability<FluidIngredient> getCapability() {
                return FluidRecipeCapability.CAP;
            }

            @Override
            public int getSize() {
                return fluids.size();
            }
        }));

        this.capabilitiesProxy.put(IO.IN, ItemRecipeCapability.CAP, List.of(new IRecipeHandler<Ingredient>() {

            @Override
            public List<Ingredient> handleRecipeInner(IO io, GTRecipe gtRecipe, List<Ingredient> left, @Nullable String s, boolean simulate) {
                if (io != IO.IN) return left;
                var capability = simulate ? items.stream().map(ItemStack::copy).toList() : items;
                Iterator<Ingredient> iterator = left.iterator();
                while (iterator.hasNext()) {
                    Ingredient ingredient = iterator.next();
                    SLOT_LOOKUP:
                    for (ItemStack itemStack : capability) {
                        if (ingredient.test(itemStack)) {
                            ItemStack[] ingredientStacks = ingredient.getItems();
                            for (ItemStack ingredientStack : ingredientStacks) {
                                if (ingredientStack.is(itemStack.getItem())) {
                                    int extracted = Math.min(itemStack.getCount(), ingredientStack.getCount());
                                    itemStack.shrink(extracted);
                                    ingredientStack.shrink(extracted);
                                    if (ingredientStack.isEmpty()) {
                                        iterator.remove();
                                        break SLOT_LOOKUP;
                                    }
                                }
                            }
                        }
                    }
                }
                return left.isEmpty() ? null : left;
            }

            @Override
            public List<Object> getContents() {
                List<ItemStack> stacks = new ArrayList<>();
                for (ItemStack stack : items) {
                    if (!stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
                return Arrays.asList(stacks.toArray());
            }

            @Override
            public double getTotalContentAmount() {
                long amount = 0;
                for (FluidStack fluid : fluids) {
                    if (!fluid.isEmpty()) {
                        amount += fluid.getAmount();
                    }
                }
                return amount;
            }

            @Override
            public RecipeCapability<Ingredient> getCapability() {
                return ItemRecipeCapability.CAP;
            }

            @Override
            public int getSize() {
                return items.size();
            }
        }));
        this.capabilitiesProxy.put(IO.OUT, FluidRecipeCapability.CAP, List.of(new IRecipeHandler<FluidIngredient>() {

            @Override
            public List<FluidIngredient> handleRecipeInner(IO io, GTRecipe gtRecipe, List<FluidIngredient> list, @Nullable String s, boolean b) {
                return null;
            }

            @Override
            public List<Object> getContents() {
                return Collections.emptyList();
            }

            @Override
            public double getTotalContentAmount() {
                return 0;
            }

            @Override
            public RecipeCapability<FluidIngredient> getCapability() {
                return FluidRecipeCapability.CAP;
            }

            @Override
            public int getSize() {
                return Integer.MAX_VALUE;
            }
        }));

        this.capabilitiesProxy.put(IO.OUT, ItemRecipeCapability.CAP, List.of(new IRecipeHandler<Ingredient>() {

            @Override
            public List<Ingredient> handleRecipeInner(IO io, GTRecipe gtRecipe, List<Ingredient> list, @Nullable String s, boolean b) {
                return null;
            }

            @Override
            public List<Object> getContents() {
                return Collections.emptyList();
            }

            @Override
            public double getTotalContentAmount() {
                return 0;
            }

            @Override
            public RecipeCapability<Ingredient> getCapability() {
                return ItemRecipeCapability.CAP;
            }

            @Override
            public int getSize() {
                return Integer.MAX_VALUE;
            }
        }));

        this.capabilitiesProxy.put(IO.IN, EURecipeCapability.CAP, List.of(new IgnoreEnergyRecipeHandler()));
        this.items = new ArrayList<>();
        this.fluids = new ArrayList<>();
    }

    public void addItems(ItemStack... itemStacks) {
        items.addAll(List.of(itemStacks));
    }

    public void addFluids(FluidStack... fluidStacks) {
        fluids.addAll(List.of(fluidStacks));
    }

    public void addItems(Collection<ItemStack> itemStacks) {
        items.addAll(itemStacks);
    }

    public void addFluids(Collection<FluidStack> fluidStacks) {
        fluids.addAll(fluidStacks);
    }

    public void clear() {
        fluids.clear();
        items.clear();
    }

}
