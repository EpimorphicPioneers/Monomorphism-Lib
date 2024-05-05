package com.epimorphismmc.monomorphism.mixins.accessors;

import net.minecraft.world.item.crafting.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ShapedRecipe.class, remap = false)
public interface ShapedRecipeAccessor {

    @Accessor(value = "MAX_WIDTH")
    static int getMaxWidth() {
        throw new UnsupportedOperationException();
    }

    @Accessor(value = "MAX_HEIGHT")
    static int getMaxHeight() {
        throw new UnsupportedOperationException();
    }
}
