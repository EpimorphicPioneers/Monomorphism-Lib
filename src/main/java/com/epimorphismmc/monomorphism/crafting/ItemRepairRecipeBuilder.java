package com.epimorphismmc.monomorphism.crafting;

import com.gregtechceu.gtceu.data.recipe.builder.ShapelessRecipeBuilder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.PartialNBTIngredient;

import com.google.gson.JsonObject;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@Accessors(chain = true, fluent = true)
public class ItemRepairRecipeBuilder extends ShapelessRecipeBuilder {

    @Setter
    private int repairDamage;

    public ItemRepairRecipeBuilder(@Nullable ResourceLocation id) {
        super(id);
    }

    public ItemRepairRecipeBuilder requireRepair(ItemStack stack) {
        output(stack);
        if (!stack.hasTag()) {
            this.requires(Ingredient.of(stack));
        } else {
            var tag = stack.getTag().copy();
            tag.remove("Damage");
            this.requires(PartialNBTIngredient.of(stack.getItem(), tag));
        }
        return this;
    }

    @Override
    public void toJson(JsonObject json) {
        super.toJson(json);
        json.addProperty("repairDamage", repairDamage);
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject pJson) {
                toJson(pJson);
            }

            @Override
            public ResourceLocation getId() {
                var ID = id == null ? defaultId() : id;
                return new ResourceLocation(ID.getNamespace(), "repair" + "/" + ID.getPath());
            }

            @Override
            public RecipeSerializer<?> getType() {
                return ItemRepairRecipe.SERIALIZER;
            }

            @Nullable @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
