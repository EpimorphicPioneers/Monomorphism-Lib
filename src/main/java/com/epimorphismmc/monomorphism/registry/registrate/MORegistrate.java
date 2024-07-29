package com.epimorphismmc.monomorphism.registry.registrate;

import com.epimorphismmc.monomorphism.block.MOMetaMachineBlock;
import com.epimorphismmc.monomorphism.registry.registrate.builders.MOBlockEntityBuilder;
import com.epimorphismmc.monomorphism.registry.registrate.providers.MOProviderTypes;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.gregtechceu.gtceu.common.data.GTItems.attach;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MORegistrate extends GTRegistrate {

    protected MORegistrate(String modId) {
        super(modId);
    }

    @NotNull public static MORegistrate create(String modId) {
        return new MORegistrate(modId);
    }

    @Override
    public <T extends RegistrateProvider> void genData(ProviderType<? extends T> type, T gen) {
        if (type == ProviderType.LANG) return;

        if (type == MOProviderTypes.MO_LANG) {
            super.genData(ProviderType.LANG, gen);
            super.genData(type, gen);
        } else {
            super.genData(type, gen);
        }
    }

    // Block Entities
    @Override
    public <T extends BlockEntity> MOBlockEntityBuilder<T, Registrate> blockEntity(
            BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), factory);
    }

    @Override
    public <T extends BlockEntity> MOBlockEntityBuilder<T, Registrate> blockEntity(
            String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(self(), name, factory);
    }

    @Override
    public <T extends BlockEntity, P> MOBlockEntityBuilder<T, P> blockEntity(
            P parent, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return blockEntity(parent, currentName(), factory);
    }

    @Override
    public <T extends BlockEntity, P> MOBlockEntityBuilder<T, P> blockEntity(
            P parent, String name, BlockEntityBuilder.BlockEntityFactory<T> factory) {
        return (MOBlockEntityBuilder<T, P>)
                entry(name, callback -> MOBlockEntityBuilder.create(this, parent, name, callback, factory));
    }

    @Override
    public MultiblockMachineBuilder multiblock(
            String name,
            Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        return MultiblockMachineBuilder.createMulti(
                this,
                name,
                metaMachine,
                MOMetaMachineBlock::new,
                MetaMachineItem::new,
                MetaMachineBlockEntity::createBlockEntity);
    }

    @Override
    public <T extends Item, P> ItemBuilder<T, P> item(
            P parent, String name, NonNullFunction<Item.Properties, T> factory) {
        return super.item(parent, name, factory)
                .setData(
                        ProviderType.LANG, NonNullBiConsumer.noop()); // We don't need an auto-generated name
    }

    @Override
    public <T extends Item> ItemBuilder<T, Registrate> item(
            String name, NonNullFunction<Item.Properties, T> factory) {
        return item(self(), name, factory); // We don't need an auto-generated name
    }

    public ItemBuilder<Item, Registrate> item(String name) {
        return item(name, Item::new);
    }

    public ItemBuilder<ComponentItem, Registrate> componentItem(String name) {
        return item(name, ComponentItem::create);
    }

    public ItemBuilder<ComponentItem, Registrate> itemWithTooltip(String name, int num) {
        return itemWithTooltip(name, ComponentItem::create, num);
    }

    public ItemBuilder<ComponentItem, Registrate> itemWithTooltip(
            String name, int num, Component... other) {
        return itemWithTooltip(name, ComponentItem::create, num, other);
    }

    public <T extends ComponentItem> ItemBuilder<T, Registrate> itemWithTooltip(
            String name, NonNullFunction<Item.Properties, T> factory, int num) {
        return item(name, factory).onRegister(attach(new TooltipBehavior(lines -> {
            if (num <= 0) return;

            if (num == 1) {
                lines.add(Component.translatable("item.%s.%s.desc".formatted(getModid(), name)));
            } else {
                for (int i = 0; i < num; i++) {
                    lines.add(Component.translatable("item.%s.%s.desc.%s".formatted(getModid(), name, i)));
                }
            }
        })));
    }

    public <T extends ComponentItem> ItemBuilder<T, Registrate> itemWithTooltip(
            String name, NonNullFunction<Item.Properties, T> factory, int num, Component... other) {
        return item(name, factory).onRegister(attach(new TooltipBehavior(lines -> {
            if (num <= 0) return;

            if (num == 1) {
                lines.add(Component.translatable("item.%s.%s.desc".formatted(getModid(), name)));
                lines.addAll(Arrays.asList(other));
            } else {
                for (int i = 0; i < num; i++) {
                    lines.add(Component.translatable("item.%s.%s.desc.%s".formatted(getModid(), name, i)));
                }
                lines.addAll(Arrays.asList(other));
            }
        })));
    }

    @Override
    public <T extends Block, P> BlockBuilder<T, P> block(
            P parent, String name, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return super.block(parent, name, factory)
                .setData(
                        ProviderType.LANG, NonNullBiConsumer.noop()); // We don't need an auto-generated name
    }
}
