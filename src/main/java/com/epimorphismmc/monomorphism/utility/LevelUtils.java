package com.epimorphismmc.monomorphism.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class LevelUtils {
    public static <T> Optional<T> getBlock(LevelAccessor level, BlockPos pos, Class<T> type) {
        return Optional.ofNullable(level)
                .map(l -> l.getBlockState(pos))
                .map(BlockBehaviour.BlockStateBase::getBlock)
                .filter(b -> type.isAssignableFrom(b.getClass()))
                .map(type::cast);
    }

    public static <T> Optional<T> getBlockEntity(LevelAccessor level, BlockPos pos, Class<T> type) {
        return Optional.ofNullable(level)
                .map(l -> l.getBlockEntity(pos))
                .filter(te -> type.isAssignableFrom(te.getClass()))
                .map(type::cast);
    }

    public static <T> Optional<T> getCapability(
            LevelAccessor level, BlockPos pos, Capability<T> capability, Class<T> type) {
        return Optional.ofNullable(level).map(l -> l.getBlockEntity(pos)).flatMap(tile -> {
            if (type.isAssignableFrom(tile.getClass())) {
                return Optional.of(type.cast(tile));
            } else {
                return tile.getCapability(capability).map(obj -> obj);
            }
        });
    }

    public static <T> List<T> collectBlocks(
            LevelAccessor level, BlockPos min, BlockPos max, Class<T> type) {
        return streamBlocks(level, min, max, type).toList();
    }

    public static <T> List<T> collectBlockEntities(
            LevelAccessor level, BlockPos min, BlockPos max, Class<T> type) {
        return streamBlockEntities(level, min, max, type).toList();
    }

    public static <T> List<T> collectCapabilities(
            LevelAccessor level, BlockPos min, BlockPos max, Capability<T> capability, Class<T> type) {
        return streamCapabilities(level, min, max, capability, type).toList();
    }

    public static <T> Stream<T> streamBlocks(
            LevelAccessor level, BlockPos min, BlockPos max, Class<T> type) {
        return streamRange(level, min, max, (l, pos) -> getBlock(l, pos, type));
    }

    public static <T> Stream<T> streamBlockEntities(
            LevelAccessor level, BlockPos min, BlockPos max, Class<T> type) {
        return streamRange(level, min, max, (l, pos) -> getBlockEntity(l, pos, type));
    }

    public static <T> Stream<T> streamCapabilities(
            LevelAccessor level, BlockPos min, BlockPos max, Capability<T> capability, Class<T> type) {
        return streamRange(level, min, max, (l, pos) -> getCapability(l, pos, capability, type));
    }

    public static <T> Stream<T> streamRange(
            LevelAccessor level,
            BlockPos min,
            BlockPos max,
            BiFunction<LevelAccessor, BlockPos, Optional<T>> getter) {
        return streamPositions(min, max)
                .map(pos -> getter.apply(level, pos))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public static Stream<BlockPos> streamPositions(BlockPos start, BlockPos end) {
        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());
        int minZ = Math.min(start.getZ(), end.getZ());
        int maxX = Math.max(start.getX(), end.getX());
        int maxY = Math.max(start.getY(), end.getY());
        int maxZ = Math.max(start.getZ(), end.getZ());
        var min = new BlockPos(minX, minY, minZ);
        var max = new BlockPos(maxX, maxY, maxZ);
        return Stream.iterate(min, pos -> pos.equals(max), pos -> pos.offset(1, 1, 1));
    }

    public static <T> List<T> getBlockEntityNeighbors(
            LevelAccessor level, BlockPos pos, Class<T> type) {
        return getBlockEntityNeighbors(
                level, pos, type, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }

    public static <T> List<T> getBlockEntityNeighbors(
            LevelAccessor level, BlockPos pos, Class<T> type, Direction... dirs) {
        List<T> neighbours = new ArrayList<>();
        for (Direction dir : dirs) {
            BlockEntity be = level.getBlockEntity(pos.relative(dir));
            if (be != null && type.isAssignableFrom(be.getClass())) {
                neighbours.add(type.cast(be));
            }
        }
        return neighbours;
    }
}
