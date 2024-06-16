package com.epimorphismmc.monomorphism.blockentity;

import com.gregtechceu.gtceu.api.GTValues;

import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAsyncAutoSyncBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IAutoPersistBlockEntity;
import com.lowdragmc.lowdraglib.syncdata.blockentity.IRPCBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;

import org.jetbrains.annotations.Nullable;

public interface IMOBlockEntity
        extends IAsyncAutoSyncBlockEntity, IRPCBlockEntity, IAutoPersistBlockEntity, IEnhancedManaged {
    default BlockEntity self() {
        return (BlockEntity) this;
    }

    default Level level() {
        return self().getLevel();
    }

    default BlockPos pos() {
        return self().getBlockPos();
    }

    default int xCoord() {
        return this.pos().getX();
    }

    default int yCoord() {
        return this.pos().getY();
    }

    default int zCoord() {
        return this.pos().getZ();
    }

    @Nullable default ChunkAccess getChunk() {
        if (this.level() == null) {
            return null;
        }
        return this.level().getChunk(this.pos());
    }

    default RandomSource getRandom() {
        return this.level() == null ? GTValues.RNG : this.level().getRandom();
    }

    default long getOffsetTimer() {
        return level() == null ? getOffset() : (level().getGameTime() + getOffset());
    }

    default boolean isRemote() {
        return this.level() != null && this.level().isClientSide();
    }

    default void notifyBlockUpdate() {
        if (level() != null) {
            level().updateNeighborsAt(pos(), level().getBlockState(pos()).getBlock());
        }
    }

    default void scheduleRenderUpdate() {
        var pos = pos();
        if (level() != null) {
            var state = level().getBlockState(pos);
            if (level().isClientSide) {
                level().sendBlockUpdated(pos, state, state, 1 << 3);
            } else {
                level().blockEvent(pos, state.getBlock(), 1, 0);
            }
        }
    }

    default void tick() {}

    long getOffset();

    @Override
    default void saveCustomPersistedData(CompoundTag tag, boolean forDrop) {
        IAutoPersistBlockEntity.super.saveCustomPersistedData(tag, forDrop);
    }

    @Override
    default void loadCustomPersistedData(CompoundTag tag) {
        IAutoPersistBlockEntity.super.loadCustomPersistedData(tag);
    }
}
