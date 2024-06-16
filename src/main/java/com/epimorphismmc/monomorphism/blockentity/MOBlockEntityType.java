package com.epimorphismmc.monomorphism.blockentity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class MOBlockEntityType<T extends BlockEntity> extends BlockEntityType<T>
        implements IMOBlockEntityType {
    @Getter
    private final boolean ticking;

    private MOBlockEntityType(
            BlockEntityType.BlockEntitySupplier<? extends T> factory,
            Set<Block> validBlocks,
            boolean ticking) {
        super(factory, validBlocks, null);
        this.ticking = ticking;
    }

    public static <T extends BlockEntity> Builder<T> builder(
            BlockEntityType.BlockEntitySupplier<? extends T> factory) {
        return new Builder<>(factory);
    }

    public static final class Builder<T extends BlockEntity> {
        private final BlockEntityType.BlockEntitySupplier<? extends T> factory;
        private final Set<Block> blocks;

        private boolean ticking;

        private Builder(BlockEntityType.BlockEntitySupplier<? extends T> factory) {
            this.factory = factory;
            this.blocks = Sets.newIdentityHashSet();
            this.ticking = false;
        }

        public Builder<T> addBlock(Block block) {
            this.blocks.add(block);
            return this;
        }

        public Builder<T> addBlocks(Collection<Block> block) {
            this.blocks.addAll(block);
            return this;
        }

        public Builder<T> addBlocks(Block... blocks) {
            Arrays.stream(blocks).forEach(this::addBlock);
            return this;
        }

        public Builder<T> setTicking(boolean ticking) {
            this.ticking = ticking;
            return this;
        }

        public MOBlockEntityType<T> build() {
            return new MOBlockEntityType<>(this.factory, this.blocks, this.ticking);
        }
    }
}
