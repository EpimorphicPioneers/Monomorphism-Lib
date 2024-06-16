package com.epimorphismmc.monomorphism.registry.registrate;

import com.epimorphismmc.monomorphism.blockentity.MOBlockEntityType;
import com.epimorphismmc.monomorphism.mixins.accessors.BlockEntityBuilderAccessor;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MOBlockEntityBuilder<T extends BlockEntity, P> extends BlockEntityBuilder<T, P> {

    private boolean ticking = false;

    protected MOBlockEntityBuilder(
            AbstractRegistrate<?> owner,
            P parent,
            String name,
            BuilderCallback callback,
            BlockEntityFactory<T> factory) {
        super(owner, parent, name, callback, factory);
    }

    public static <T extends BlockEntity, P> BlockEntityBuilder<T, P> create(
            AbstractRegistrate<?> owner,
            P parent,
            String name,
            BuilderCallback callback,
            BlockEntityFactory<T> factory) {
        return new MOBlockEntityBuilder<>(owner, parent, name, callback, factory);
    }

    public BlockEntityBuilder<T, P> ticking() {
        this.ticking = true;
        return this;
    }

    @Override
    protected BlockEntityType<T> createEntry() {
        BlockEntityBuilderAccessor<T> accessor = (BlockEntityBuilderAccessor<T>) this;
        BlockEntityFactory<T> factory = accessor.getFactory();
        Supplier<BlockEntityType<T>> supplier = asSupplier();
        return MOBlockEntityType.builder((pos, state) -> factory.create(supplier.get(), pos, state))
                .addBlocks(
                        accessor.getValidBlocks().stream().map(NonNullSupplier::get).toArray(Block[]::new))
                .setTicking(ticking)
                .build();
    }
}
