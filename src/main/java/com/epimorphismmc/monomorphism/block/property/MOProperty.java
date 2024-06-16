package com.epimorphismmc.monomorphism.block.property;

import com.epimorphismmc.monomorphism.utility.DirectionalConnectivity;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class MOProperty<T extends Comparable<T>> {
    private final Property<T> property;
    private final T defaultValue;
    private final MirrorHandler<T> mirrorHandler;
    private final RotationHandler<T> rotationHandler;

    private MOProperty(
            Property<T> property,
            T defaultValue,
            MirrorHandler<T> mirrorHandler,
            RotationHandler<T> rotationHandler) {
        this.property = property;
        this.defaultValue = defaultValue;
        this.mirrorHandler = mirrorHandler;
        this.rotationHandler = rotationHandler;
    }

    public final String getName() {
        return this.getProperty().getName();
    }

    public final T getDefaultValue() {
        return this.defaultValue;
    }

    public final Property<T> getProperty() {
        return this.property;
    }

    public final Collection<T> getPossibleValues() {
        return this.getProperty().getPossibleValues();
    }

    protected StateDefinition.Builder<Block, BlockState> apply(
            StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.getProperty());
        return builder;
    }

    public final BlockState apply(BlockState state) {
        return this.apply(state, this.getDefaultValue());
    }

    public final BlockState apply(BlockState state, T value) {
        return state.setValue(this.getProperty(), value);
    }

    public final T fetch(BlockState state) {
        return state.getValue(this.getProperty());
    }

    public final BlockState mimic(BlockState from, BlockState to) {
        return this.apply(to, this.fetch(from));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return this.apply(state, this.mirrorHandler.handle(mirror, this.fetch(state)));
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return this.apply(state, this.rotationHandler.handle(rotation, this.fetch(state)));
    }

    public static class Defaults {
        private Defaults() {}

        private static final MOProperty<Boolean> WATERLOGGED =
                Creators.create(BlockStateProperties.WATERLOGGED, false);
        private static final MOProperty<Boolean> LAVALOGGED =
                Creators.create(BooleanProperty.create("lavalogged"), false);
        private static final MOProperty<FluidLogged> FLUIDLOGGED =
                Creators.create(EnumProperty.create("fluidlogged", FluidLogged.class), FluidLogged.NONE);
        private static final MOProperty<DirectionalConnectivity> CONNECTIVITY =
                Creators.create("connectivity", DirectionalConnectivity.NONE);

        public static MOProperty<Boolean> waterlogged() {
            return WATERLOGGED;
        }

        public static MOProperty<Boolean> lavalogged() {
            return LAVALOGGED;
        }

        public static MOProperty<FluidLogged> fluidlogged() {
            return FLUIDLOGGED;
        }

        public static MOProperty<DirectionalConnectivity> connectivity() {
            return CONNECTIVITY;
        }
    }

    public static final class Creators {
        private Creators() {}

        public static MOProperty<Boolean> create(String name, boolean defaultValue) {
            return create(BooleanProperty.create(name), defaultValue);
        }

        public static MOProperty<Integer> create(String name, int def, int min, int max) {
            return create(IntegerProperty.create(name, min, max), def);
        }

        public static MOProperty<Direction> create(String name, Direction defaultValue) {
            return create(DirectionProperty.create(name, Direction.values()), defaultValue);
        }

        public static MOProperty<Direction> createHorizontals(String name, Direction defaultValue) {
            return create(DirectionProperty.create(name, Direction.Plane.HORIZONTAL), defaultValue);
        }

        public static MOProperty<Direction.Axis> create(String name, Direction.Axis defaultValue) {
            return create(EnumProperty.create(name, Direction.Axis.class), defaultValue);
        }

        public static MOProperty<Direction.Axis> createHorizontals(
                String name, Direction.Axis defaultValue) {
            return create(
                    EnumProperty.create(name, Direction.Axis.class, Direction.Axis.X, Direction.Axis.Z),
                    defaultValue);
        }

        public static MOProperty<DirectionalConnectivity> create(
                String name, DirectionalConnectivity defaultValue) {
            return create(Properties.Connectivity.create(name), defaultValue);
        }

        public static <T extends Enum<T> & StringRepresentable> MOProperty<T> create(
                String name, Class<T> valueClass, T defaultValue) {
            return create(EnumProperty.create(name, valueClass), defaultValue);
        }

        public static <T extends Enum<T> & StringRepresentable> MOProperty<T> create(
                String name, Class<T> valueClass, T defaultValue, Collection<T> allowedValues) {
            return create(EnumProperty.create(name, valueClass, allowedValues), defaultValue);
        }

        public static <T extends Enum<T> & StringRepresentable> MOProperty<T> create(
                String name, Class<T> valueClass, T defaultValue, T... allowedValues) {
            return create(EnumProperty.create(name, valueClass, allowedValues), defaultValue);
        }

        public static <T extends Enum<T> & StringRepresentable> MOProperty<T> create(
                String name, Class<T> valueClass, T defaultValue, Predicate<T> allowedValues) {
            return create(EnumProperty.create(name, valueClass, allowedValues), defaultValue);
        }

        public static <T extends Comparable<T>> MOProperty<T> create(
                Property<T> property, T defaultValue) {
            return create(
                    property,
                    defaultValue,
                    MirrorHandler.Handlers.defaultHandler(),
                    RotationHandler.Handlers.defaultHandler());
        }

        public static <T extends Comparable<T>> MOProperty<T> create(
                Property<T> property, T defaultValue, MirrorHandler<T> mirrorHandler) {
            return create(
                    property, defaultValue, mirrorHandler, RotationHandler.Handlers.defaultHandler());
        }

        public static MOProperty<Direction> create(DirectionProperty property, Direction defaultValue) {
            return create(
                    property,
                    defaultValue,
                    MirrorHandler.Handlers.direction(),
                    RotationHandler.Handlers.direction());
        }

        public static MOProperty<Direction.Axis> create(
                EnumProperty<Direction.Axis> property, Direction.Axis defaultValue) {
            return create(property, defaultValue, RotationHandler.Handlers.axis());
        }

        public static <T extends Comparable<T>> MOProperty<T> create(
                Property<T> property, T defaultValue, RotationHandler<T> rotationHandler) {
            return create(
                    property, defaultValue, MirrorHandler.Handlers.defaultHandler(), rotationHandler);
        }

        public static <T extends Comparable<T>> MOProperty<T> create(
                Property<T> property,
                T defaultValue,
                MirrorHandler<T> mirrorHandler,
                RotationHandler<T> rotationHandler) {
            return new MOProperty<>(property, defaultValue, mirrorHandler, rotationHandler);
        }
    }

    public static class Properties {
        public static final class Connectivity extends Property<DirectionalConnectivity> {
            public static Property<DirectionalConnectivity> create(String name) {
                return new Connectivity(name);
            }

            private Connectivity(String name) {
                super(name, DirectionalConnectivity.class);
            }

            @Override
            public Collection<DirectionalConnectivity> getPossibleValues() {
                return DirectionalConnectivity.ALL;
            }

            @Override
            public String getName(DirectionalConnectivity value) {
                return value.toString();
            }

            @Override
            public Optional<DirectionalConnectivity> getValue(String value) {
                return Optional.empty();
            }
        }
    }

    public enum FluidLogged implements StringRepresentable, IExtensibleEnum {
        NONE(Fluids.EMPTY),
        WATER(Fluids.WATER),
        LAVA(Fluids.LAVA);

        private final Fluid fluid;

        FluidLogged(Fluid fluid) {
            this.fluid = fluid;
        }

        public Fluid getFluid() {
            return this.fluid;
        }

        public boolean isEmpty() {
            return this == NONE;
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        public static boolean accepts(Fluid fluid) {
            return Arrays.stream(values()).anyMatch(val -> val.getFluid().equals(fluid));
        }

        public static FluidLogged get(FluidState fluid) {
            return get(fluid.getType());
        }

        public static FluidLogged get(FluidStack fluid) {
            return get(fluid.getFluid());
        }

        public static FluidLogged get(Fluid fluid) {
            return Arrays.stream(values())
                    .filter(val -> val.getFluid() == fluid)
                    .findAny()
                    .orElse(NONE);
        }

        // this could be problematic, enum should be extended before state containers are filled.
        @SuppressWarnings("unused")
        public static FluidLogged create(String name, Fluid fluid) {
            throw new IllegalStateException("Enum not extended");
        }
    }
}
