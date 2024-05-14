package com.epimorphismmc.monomorphism.registry.registrate;

import com.epimorphismmc.monomorphism.block.MOMetaMachineBlock;
import com.epimorphismmc.monomorphism.datagen.MOProviderTypes;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MORegistrate extends GTRegistrate {

    protected MORegistrate(String modId) {
        super(modId);
    }

    @NotNull
    public static MORegistrate create(String modId) {
        return new MORegistrate(modId);
    }

    @Override
    public <T extends RegistrateProvider> void genData(ProviderType<? extends T> type, T gen) {
        if (type == ProviderType.LANG) return;

        if (type == MOProviderTypes.MO_LANG) {
            super.genData(type, gen);
            super.genData(ProviderType.LANG, gen);
        }
    }

    // Block Entities
    @Override
    public <T extends BlockEntity> MOBlockEntityBuilder<T, Registrate> blockEntity(BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), factory);
    }

    @Override
    public <T extends BlockEntity> MOBlockEntityBuilder<T, Registrate> blockEntity(String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), name, factory);
    }

    @Override
    public <T extends BlockEntity, P> MOBlockEntityBuilder<T, P> blockEntity(P parent, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(parent, currentName(), factory);
    }

    @Override
    public <T extends BlockEntity, P> MOBlockEntityBuilder<T, P> blockEntity(P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return (MOBlockEntityBuilder<T, P>) entry(name, callback -> MOBlockEntityBuilder.create(this, parent, name, callback, factory));
    }

    @Override
    public MultiblockMachineBuilder multiblock(String name, Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return MultiblockMachineBuilder.createMulti(this, name, metaMachine, MOMetaMachineBlock::new, MetaMachineItem::new, MetaMachineBlockEntity::createBlockEntity);
    }

}
