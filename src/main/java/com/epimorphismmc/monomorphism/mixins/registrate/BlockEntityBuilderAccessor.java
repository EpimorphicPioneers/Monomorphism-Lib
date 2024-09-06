package com.epimorphismmc.monomorphism.mixins.registrate;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = BlockEntityBuilder.class, remap = false)
public interface BlockEntityBuilderAccessor<T extends BlockEntity> {

    @Accessor
    BlockEntityBuilder.BlockEntityFactory<T> getFactory();

    @Accessor
    Set<NonNullSupplier<? extends Block>> getValidBlocks();
}
