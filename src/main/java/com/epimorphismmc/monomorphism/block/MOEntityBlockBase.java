package com.epimorphismmc.monomorphism.block;

import com.epimorphismmc.monomorphism.block.blockentity.MOBlockEntityBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class MOEntityBlockBase<T extends MOBlockEntityBase> extends MOBlockBase implements IMOEntityBlock<T> {

    public MOEntityBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int id, int data) {
        super.triggerEvent(state, world, pos, id, data);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, data);
    }
}
