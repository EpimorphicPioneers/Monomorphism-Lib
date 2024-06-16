package com.epimorphismmc.monomorphism.crafting;

import com.epimorphismmc.monomorphism.mixins.accessors.ShapedRecipeAccessor;
import com.epimorphismmc.monomorphism.mixins.accessors.ShapelessRecipeAccessor;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemRepairRecipe extends ShapelessRecipe {
    public static final RecipeSerializer<ItemRepairRecipe> SERIALIZER = new Serializer();

    @Getter
    private final int repairDamage;

    public ItemRepairRecipe(
            ResourceLocation id,
            String group,
            NonNullList<Ingredient> recipeItems,
            ItemStack result,
            int repairDamage) {
        super(id, group, CraftingBookCategory.MISC, result, recipeItems);
        this.repairDamage = repairDamage;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack resultStack = super.assemble(container, registryAccess);
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (ItemStack.isSameItem(stack, resultStack)) {
                return repair(stack);
            }
        }
        return resultStack;
    }

    private ItemStack repair(ItemStack stack) {
        if (stack.isDamaged()) {
            stack.setDamageValue(stack.getDamageValue() - repairDamage);
            return stack;
        }
        return stack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    protected static class Serializer implements RecipeSerializer<ItemRepairRecipe> {

        @Override
        public ItemRepairRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            String s = GsonHelper.getAsString(serializedRecipe, "group", "");
            NonNullList<Ingredient> nonnulllist =
                    itemsFromJson(GsonHelper.getAsJsonArray(serializedRecipe, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size()
                    > ShapedRecipeAccessor.getMaxWidth() * ShapedRecipeAccessor.getMaxHeight()) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is "
                        + ShapedRecipeAccessor.getMaxWidth() * ShapedRecipeAccessor.getMaxHeight());
            } else {
                ItemStack itemstack =
                        ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(serializedRecipe, "result"));
                int repair = GsonHelper.getAsInt(serializedRecipe, "repairDamage");
                return new ItemRepairRecipe(recipeId, s, nonnulllist, itemstack, repair);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i), false);
                nonnulllist.add(ingredient);
            }

            return nonnulllist;
        }

        @Override
        public @Nullable ItemRepairRecipe fromNetwork(
                ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String s = buffer.readUtf();
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);

            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack itemstack = buffer.readItem();
            int repair = buffer.readVarInt();
            return new ItemRepairRecipe(recipeId, s, nonnulllist, itemstack, repair);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ItemRepairRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            buffer.writeVarInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(((ShapelessRecipeAccessor) recipe).getResult());
            buffer.writeVarInt(recipe.repairDamage);
        }
    }
}
