package com.epimorphismmc.monomorphism.integration.gtm.pattern;

import com.epimorphismmc.monomorphism.block.MOBlockProperties;
import com.epimorphismmc.monomorphism.integration.gtm.pattern.predicates.MOPredicate;
import com.epimorphismmc.monomorphism.utility.MOUtils;

import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockState;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.predicates.SimplePredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;

import com.lowdragmc.lowdraglib.utils.BlockInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MOBlockPattern extends BlockPattern {

    static Direction[] FACINGS = {
        Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN
    };
    static Direction[] FACINGS_H = {Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST};

    protected final Map<String, TraceabilityPredicate[][][]> blockMatchesMap; // [z][y][x]

    protected int structureTier;

    public MOBlockPattern(
            int structureTier,
            TraceabilityPredicate[][][] predicatesIn,
            RelativeDirection[] structureDir,
            int[][] aisleRepetitions,
            int[] centerOffset) {
        super(predicatesIn, structureDir, aisleRepetitions, centerOffset);
        this.blockMatchesMap = new HashMap<>();
        this.structureTier = structureTier;
    }

    public MOBlockPattern(
            int structureTier,
            Map<String, TraceabilityPredicate[][][]> predicatesIn,
            RelativeDirection[] structureDir,
            int[][] aisleRepetitions,
            int[] centerOffset) {
        super(new TraceabilityPredicate[0][0][0], structureDir, aisleRepetitions, centerOffset);
        this.blockMatchesMap = predicatesIn;
        this.structureTier = structureTier;
    }

    @Override
    public boolean checkPatternAt(
            MultiblockState worldState,
            BlockPos centerPos,
            Direction facing,
            Direction upwardsFacing,
            boolean isFlipped,
            boolean savePredicate) {
        return super.checkPatternAt(
                worldState, centerPos, facing, upwardsFacing, isFlipped, savePredicate);
    }

    @Override
    public BlockInfo[][][] getPreview(int[] repetition) {
        return getPreview(repetition, -1);
    }

    public BlockInfo[][][] getPreview(int[] repetition, int index) {
        Map<SimplePredicate, Integer> cacheGlobal = new HashMap<>();
        Map<BlockPos, BlockInfo> blocks = new HashMap<>();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (int l = 0, x = 0; l < this.fingerLength; l++) {
            for (int r = 0; r < repetition[l]; r++) {
                // Checking single slice
                Map<SimplePredicate, Integer> cacheLayer = new HashMap<>();
                for (int y = 0; y < this.thumbLength; y++) {
                    for (int z = 0; z < this.palmLength; z++) {
                        TraceabilityPredicate predicate = this.blockMatches[l][y][z];
                        boolean find = false;
                        boolean preview = false;
                        BlockInfo[] infos = null;
                        for (SimplePredicate limit : predicate.limited) { // check layer and previewCount
                            if (limit.minLayerCount > 0) {
                                if (!cacheLayer.containsKey(limit)) {
                                    cacheLayer.put(limit, 1);
                                } else if (cacheLayer.get(limit) < limit.minLayerCount) {
                                    cacheLayer.put(limit, cacheLayer.get(limit) + 1);
                                } else {
                                    continue;
                                }
                                if (cacheGlobal.getOrDefault(limit, 0) < limit.previewCount) {
                                    if (!cacheGlobal.containsKey(limit)) {
                                        cacheGlobal.put(limit, 1);
                                    } else if (cacheGlobal.get(limit) < limit.previewCount) {
                                        cacheGlobal.put(limit, cacheGlobal.get(limit) + 1);
                                    } else {
                                        continue;
                                    }
                                }
                            } else {
                                continue;
                            }
                            infos = limit.candidates == null ? null : limit.candidates.get();
                            find = true;
                            preview = shouldPreviewCandidates(limit);
                            break;
                        }
                        if (!find) { // check global and previewCount
                            for (SimplePredicate limit : predicate.limited) {
                                if (limit.minCount == -1 && limit.previewCount == -1) continue;
                                if (cacheGlobal.getOrDefault(limit, 0) < limit.previewCount) {
                                    if (!cacheGlobal.containsKey(limit)) {
                                        cacheGlobal.put(limit, 1);
                                    } else if (cacheGlobal.get(limit) < limit.previewCount) {
                                        cacheGlobal.put(limit, cacheGlobal.get(limit) + 1);
                                    } else {
                                        continue;
                                    }
                                } else if (limit.minCount > 0) {
                                    if (!cacheGlobal.containsKey(limit)) {
                                        cacheGlobal.put(limit, 1);
                                    } else if (cacheGlobal.get(limit) < limit.minCount) {
                                        cacheGlobal.put(limit, cacheGlobal.get(limit) + 1);
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                                infos = limit.candidates == null ? null : limit.candidates.get();
                                find = true;
                                preview = shouldPreviewCandidates(limit);
                                break;
                            }
                        }
                        if (!find) { // check common with previewCount
                            for (SimplePredicate common : predicate.common) {
                                if (common.previewCount > 0) {
                                    if (!cacheGlobal.containsKey(common)) {
                                        cacheGlobal.put(common, 1);
                                    } else if (cacheGlobal.get(common) < common.previewCount) {
                                        cacheGlobal.put(common, cacheGlobal.get(common) + 1);
                                    } else {
                                        continue;
                                    }
                                } else {
                                    continue;
                                }
                                infos = common.candidates == null ? null : common.candidates.get();
                                find = true;
                                preview = shouldPreviewCandidates(common);
                                break;
                            }
                        }
                        if (!find) { // check without previewCount
                            for (SimplePredicate common : predicate.common) {
                                if (common.previewCount == -1) {
                                    infos = common.candidates == null ? null : common.candidates.get();
                                    find = true;
                                    preview = shouldPreviewCandidates(common);
                                    break;
                                }
                            }
                        }
                        if (!find) { // check max
                            for (SimplePredicate limit : predicate.limited) {
                                if (limit.previewCount != -1) {
                                    continue;
                                } else if (limit.maxCount != -1 || limit.maxLayerCount != -1) {
                                    if (cacheGlobal.getOrDefault(limit, 0) < limit.maxCount) {
                                        if (!cacheGlobal.containsKey(limit)) {
                                            cacheGlobal.put(limit, 1);
                                        } else {
                                            cacheGlobal.put(limit, cacheGlobal.get(limit) + 1);
                                        }
                                    } else if (cacheLayer.getOrDefault(limit, 0) < limit.maxLayerCount) {
                                        if (!cacheLayer.containsKey(limit)) {
                                            cacheLayer.put(limit, 1);
                                        } else {
                                            cacheLayer.put(limit, cacheLayer.get(limit) + 1);
                                        }
                                    } else {
                                        continue;
                                    }
                                }

                                infos = limit.candidates == null ? null : limit.candidates.get();
                                preview = shouldPreviewCandidates(limit);
                                break;
                            }
                        }

                        BlockInfo info;
                        if (preview && index > -1) {
                            info = infos == null || infos.length == 0
                                    ? BlockInfo.EMPTY
                                    : MOUtils.getOrLast(infos, index);
                        } else {
                            info = infos == null || infos.length == 0 ? BlockInfo.EMPTY : infos[0];
                        }
                        BlockPos pos = setActualRelativeOffset(z, y, x, Direction.NORTH);

                        blocks.put(pos, info);
                        minX = Math.min(pos.getX(), minX);
                        minY = Math.min(pos.getY(), minY);
                        minZ = Math.min(pos.getZ(), minZ);
                        maxX = Math.max(pos.getX(), maxX);
                        maxY = Math.max(pos.getY(), maxY);
                        maxZ = Math.max(pos.getZ(), maxZ);
                    }
                }
                x++;
            }
        }
        BlockInfo[][][] result = (BlockInfo[][][])
                Array.newInstance(BlockInfo.class, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
        int finalMinX = minX;
        int finalMinY = minY;
        int finalMinZ = minZ;
        blocks.forEach((pos, info) -> {
            resetFacing(
                    pos,
                    info.getBlockState(),
                    null,
                    (p, f) -> {
                        BlockInfo blockInfo = blocks.get(p.relative(f));
                        if (blockInfo == null || blockInfo.getBlockState().getBlock() == Blocks.AIR) {
                            if (blocks.get(pos).getBlockState().getBlock()
                                    instanceof MetaMachineBlock machineBlock) {
                                if (machineBlock.newBlockEntity(BlockPos.ZERO, machineBlock.defaultBlockState())
                                        instanceof IMachineBlockEntity machineBlockEntity) {
                                    var machine = machineBlockEntity.getMetaMachine();
                                    if (machine instanceof IMultiController) {
                                        return false;
                                    } else {
                                        return machine.isFacingValid(f);
                                    }
                                }
                            }
                            return true;
                        }
                        return false;
                    },
                    state -> info.setBlockState(
                            state.hasProperty(MOBlockProperties.STRUCTURE_TIER)
                                    ? state.setValue(MOBlockProperties.STRUCTURE_TIER, structureTier)
                                    : state));
            result[pos.getX() - finalMinX][pos.getY() - finalMinY][pos.getZ() - finalMinZ] = info;
        });
        return result;
    }

    private boolean shouldPreviewCandidates(SimplePredicate predicate) {
        if (predicate instanceof MOPredicate moPredicate) {
            return moPredicate.previewCandidates();
        }
        return false;
    }

    private void resetFacing(
            BlockPos pos,
            BlockState blockState,
            Direction facing,
            BiFunction<BlockPos, Direction, Boolean> checker,
            Consumer<BlockState> consumer) {
        if (blockState.hasProperty(BlockStateProperties.FACING)) {
            tryFacings(
                    blockState,
                    pos,
                    checker,
                    consumer,
                    BlockStateProperties.FACING,
                    facing == null ? FACINGS : ArrayUtils.addAll(new Direction[] {facing}, FACINGS));
        } else if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            tryFacings(
                    blockState,
                    pos,
                    checker,
                    consumer,
                    BlockStateProperties.HORIZONTAL_FACING,
                    facing == null || facing.getAxis() == Direction.Axis.Y
                            ? FACINGS_H
                            : ArrayUtils.addAll(new Direction[] {facing}, FACINGS_H));
        }
    }

    private void tryFacings(
            BlockState blockState,
            BlockPos pos,
            BiFunction<BlockPos, Direction, Boolean> checker,
            Consumer<BlockState> consumer,
            Property<Direction> property,
            Direction[] facings) {
        Direction found = null;
        for (Direction facing : facings) {
            if (checker.apply(pos, facing)) {
                found = facing;
                break;
            }
        }
        if (found == null) {
            found = Direction.NORTH;
        }
        consumer.accept(blockState.setValue(property, found));
    }

    private BlockPos setActualRelativeOffset(int x, int y, int z, Direction facing) {
        int[] c0 = new int[] {x, y, z}, c1 = new int[3];
        for (int i = 0; i < 3; i++) {
            switch (structureDir[i].getActualFacing(facing)) {
                case UP -> c1[1] = c0[i];
                case DOWN -> c1[1] = -c0[i];
                case WEST -> c1[0] = -c0[i];
                case EAST -> c1[0] = c0[i];
                case NORTH -> c1[2] = -c0[i];
                case SOUTH -> c1[2] = c0[i];
            }
        }
        return new BlockPos(c1[0], c1[1], c1[2]);
    }
}
