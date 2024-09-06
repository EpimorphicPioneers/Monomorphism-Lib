package com.epimorphismmc.monomorphism.integration.gtm.block;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MOMetaMachineBlock extends MetaMachineBlock {
    public MOMetaMachineBlock(Properties properties, MachineDefinition definition) {
        super(properties, definition);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(MOBlockProperties.STRUCTURE_TIER, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(MOBlockProperties.STRUCTURE_TIER);
    }
}
