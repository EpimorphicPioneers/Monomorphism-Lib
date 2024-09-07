package com.epimorphismmc.monomorphism.integration.gtm.pattern.predicates;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;

import java.util.Arrays;
import java.util.List;

public class PredicateDirections extends SimplePredicate {

    protected final Block block;

    protected final RelativeDirection[] directions;

    public PredicateDirections(Block block, RelativeDirection... directions) {
        super("directions");
        if (!block.getStateDefinition().getProperties().contains(DirectionalBlock.FACING)) {
            throw new IllegalArgumentException("The block is missing the 'facing' property.");
        }
        this.block = block;
        this.directions = directions;
        buildPredicate();
    }

    @Override
    public List<Component> getToolTips(TraceabilityPredicate predicates) {
        var components = super.getToolTips(predicates);
        components.add(Component.translatable("monomorphism.multiblock.pattern.direction"));
        return components;
    }

    @Override
    public SimplePredicate buildPredicate() {
        predicate = state -> state.getBlockState().is(block);
        candidates = () -> new BlockInfo[] {BlockInfo.fromBlock(block)};
        return this;
    }

    @Override
    public boolean test(MultiblockState blockWorldState) {
        if (super.test(blockWorldState)) {
            var controller = blockWorldState.getController().self();
            var state = blockWorldState.getBlockState();
            if (state.hasProperty(DirectionalBlock.FACING)) {
                return Arrays.stream(directions)
                        .map(d -> d.getRelativeFacing(
                                controller.getFrontFacing(), controller.getUpwardsFacing(), controller.isFlipped()))
                        .anyMatch(d -> d == state.getValue(DirectionalBlock.FACING));
            }
        }
        return false;
    }
}
