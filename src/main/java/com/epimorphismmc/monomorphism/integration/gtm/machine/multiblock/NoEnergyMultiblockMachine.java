package com.epimorphismmc.monomorphism.integration.gtm.machine.multiblock;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.gui.fancy.TooltipsPanel;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NoEnergyMultiblockMachine extends WorkableMultiblockMachine
        implements IFancyUIMachine, IDisplayUIMachine {
    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NoEnergyMultiblockMachine.class, WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    public NoEnergyMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    //////////////////////////////////////
    // ***        Multiblock UI       ***//
    //////////////////////////////////////
    @Override
    public void addDisplayText(List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (isFormed()) {

            //            if (canBeDistinct() && inputInventory.getSlots() > 0) {
            //                var buttonText =
            // Component.translatable("gtceu.multiblock.universal.distinct");
            //                buttonText.appendText(" ");
            //                var button = AdvancedTextWidget.withButton(isDistinct() ?
            //
            // Component.translatable("gtceu.multiblock.universal.distinct.yes").setStyle(Style.EMPTY.setColor(TextFormatting.GREEN)) :
            //
            // Component.translatable("gtceu.multiblock.universal.distinct.no").setStyle(Style.EMPTY.setColor(TextFormatting.RED)), "distinct");
            //                AdvancedTextWidget.withHoverTextTranslate(button,
            // "gtceu.multiblock.universal.distinct.info");
            //                buttonText.appendSibling(button);
            //                textList.add(buttonText);
            //            }

            //
            // textList.add(Component.translatable("gtceu.multiblock.multiple_recipemaps.header")
            //                    .setStyle(Style.EMPTY.withHoverEvent(new
            // HoverEvent(HoverEvent.Action.SHOW_TEXT,
            //
            // Component.translatable("gtceu.multiblock.multiple_recipemaps.tooltip")))));

            textList.add(Component.translatable(getRecipeType().registryName.toLanguageKey())
                    .setStyle(Style.EMPTY
                            .withColor(ChatFormatting.AQUA)
                            .withHoverEvent(new HoverEvent(
                                    HoverEvent.Action.SHOW_TEXT,
                                    Component.translatable("gtceu.gui.machinemode.title")))));

            if (!isWorkingEnabled()) {
                textList.add(Component.translatable("gtceu.multiblock.work_paused"));

            } else if (isActive()) {
                textList.add(Component.translatable("gtceu.multiblock.running"));
                int currentProgress = (int) (recipeLogic.getProgressPercent() * 100);
                //                if (this.recipeMapWorkable.getParallelLimit() != 1) {
                //                    textList.add(Component.translatable("gtceu.multiblock.parallel",
                // this.recipeMapWorkable.getParallelLimit()));
                //                }
                textList.add(Component.translatable("gtceu.multiblock.progress", currentProgress));
            } else {
                textList.add(Component.translatable("gtceu.multiblock.idling"));
            }

            if (recipeLogic.isWaiting()) {
                textList.add(Component.translatable("gtceu.multiblock.waiting")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        }
        getDefinition().getAdditionalDisplay().accept(this, textList);
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117)
                .setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .setMaxWidthLimit(150)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer)
                .widget(new FancyMachineUIWidget(this, 198, 208));
    }

    @Override
    public List<IFancyUIProvider> getSubTabs() {
        return getParts().stream()
                .filter(IFancyUIProvider.class::isInstance)
                .map(IFancyUIProvider.class::cast)
                .toList();
    }

    @Override
    public void attachTooltips(TooltipsPanel tooltipsPanel) {
        for (IMultiPart part : getParts()) {
            part.attachFancyTooltipsToController(this, tooltipsPanel);
        }
    }

    //////////////////////////////////////
    // ***       Multiblock Data      ***//
    //////////////////////////////////////
    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
