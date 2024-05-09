package com.epimorphismmc.monomorphism.pattern.utils;

import com.epimorphismmc.monomorphism.block.MOBlockProperties;
import com.epimorphismmc.monomorphism.pattern.MOBlockPattern;
import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StructureUtil {
    public static final BlockPattern EMPTY_PATTERN = new BlockPattern(new TraceabilityPredicate[0][0][0], new RelativeDirection[0], new int[0][0], new int[0]) {
        @Override
        public boolean checkPatternAt(MultiblockState worldState, BlockPos centerPos, Direction facing, boolean savePredicate) {
            return false;
        }

        @Override
        public void autoBuild(Player player, MultiblockState worldState) {/**/}
    };
    public static BlockState getTierController(IMachineBlock controller, int tier, Direction direction) {
        return controller.getRotationState() == RotationState.NONE ?
                controller.self().defaultBlockState().setValue(MOBlockProperties.STRUCTURE_TIER, tier) :
                controller.self().defaultBlockState().setValue(controller.getRotationState().property, direction).setValue(MOBlockProperties.STRUCTURE_TIER, tier);
    }

    public static BlockPattern emptyPattern(MultiblockMachineDefinition definition) {
        return EMPTY_PATTERN;
    }

    public static List<MultiblockShapeInfo> getMatchingShapes(BlockPattern blockPattern) {
        int[][] aisleRepetitions = blockPattern.aisleRepetitions;
        return repetitionDFS(blockPattern, new ArrayList<>(), aisleRepetitions, new Stack<>());
    }

    public static List<MultiblockShapeInfo> getMatchingShapes(MOBlockPattern blockPattern, int maxIndex) {
        int[][] aisleRepetitions = blockPattern.aisleRepetitions;
        var pages = repetitionMO(blockPattern, new ArrayList<>(), aisleRepetitions, new Stack<>());
        if (pages.size() < maxIndex) {
            int[] repetition = new int[aisleRepetitions.length];
            for (int i = 0; i < repetition.length; i++) {
                repetition[i] = aisleRepetitions[i][1];
            }
            for (int i = pages.size(); i < maxIndex; i++) {
                pages.add(new MultiblockShapeInfo(blockPattern.getPreview(repetition, i)));
            }
        }
        return pages;
    }

    public static List<MultiblockShapeInfo> getTierMatchingShapes(MOBlockPattern blockPattern, int index) {
        int[][] aisleRepetitions = blockPattern.aisleRepetitions;
        return repetitionMO(blockPattern, new ArrayList<>(), aisleRepetitions, new Stack<>(), index);
    }

    private static List<MultiblockShapeInfo> repetitionDFS(BlockPattern pattern, List<MultiblockShapeInfo> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(new MultiblockShapeInfo(pattern.getPreview(repetition)));
        } else {
            for (int i = aisleRepetitions[repetitionStack.size()][0]; i <= aisleRepetitions[repetitionStack.size()][1]; i++) {
                repetitionStack.push(i);
                repetitionDFS(pattern, pages, aisleRepetitions, repetitionStack);
                repetitionStack.pop();
            }
        }
        return pages;
    }

    private static List<MultiblockShapeInfo> repetitionMO(MOBlockPattern pattern, List<MultiblockShapeInfo> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack, int index) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(new MultiblockShapeInfo(pattern.getPreview(repetition, index)));
        } else {
            for (int i = aisleRepetitions[repetitionStack.size()][0]; i <= aisleRepetitions[repetitionStack.size()][1]; i++) {
                repetitionStack.push(i);
                repetitionMO(pattern, pages, aisleRepetitions, repetitionStack, index);
                repetitionStack.pop();
            }
        }
        return pages;
    }

    private static List<MultiblockShapeInfo> repetitionCandidates(MOBlockPattern pattern, List<MultiblockShapeInfo> pages, int[][] aisleRepetitions, Stack<Integer> repetitionStack) {
        if (repetitionStack.size() == aisleRepetitions.length) {
            int[] repetition = new int[repetitionStack.size()];
            for (int i = 0; i < repetitionStack.size(); i++) {
                repetition[i] = repetitionStack.get(i);
            }
            pages.add(new MultiblockShapeInfo(pattern.getPreview(repetition, pages.size())));
        } else {
            for (int i = aisleRepetitions[repetitionStack.size()][0]; i <= aisleRepetitions[repetitionStack.size()][1]; i++) {
                repetitionStack.push(i);
                repetitionMO(pattern, pages, aisleRepetitions, repetitionStack);
                repetitionStack.pop();
            }
        }
        return pages;
    }
}
