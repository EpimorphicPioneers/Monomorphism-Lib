package com.epimorphismmc.monomorphism.blockentity;

import com.gregtechceu.gtceu.api.GTValues;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public abstract class MOBlockEntityBase extends BlockEntity implements IMOBlockEntity {
    private final long offset = GTValues.RNG.nextInt(20);

    public MOBlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public static void onBlockEntityRegister(BlockEntityType<BlockEntity> blockEntityType) {}

    @Override
    public boolean triggerEvent(int id, int para) {
        if (id == 1) { // chunk re render
            if (level != null && level.isClientSide) {
                scheduleRenderUpdate();
            }
            return true;
        }
        return false;
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
