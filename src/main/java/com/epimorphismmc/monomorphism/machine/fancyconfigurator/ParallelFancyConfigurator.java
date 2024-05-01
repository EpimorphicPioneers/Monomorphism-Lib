package com.epimorphismmc.monomorphism.machine.fancyconfigurator;

import com.epimorphismmc.monomorphism.machine.feature.multiblock.stats.IParallelMachine;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class ParallelFancyConfigurator implements IFancyConfigurator {
    protected IParallelMachine machine;

    public ParallelFancyConfigurator(IParallelMachine machine) {
        this.machine = machine;
    }
    @Override
    public Component getTitle() {
        return Component.translatable("gui.epimorphism.machine_parallel.title");
    }

    @Override
    public IGuiTexture getIcon() {
        return OVERLAY_PARALLEL_CONFIGURATOR;
    }

    @Override
    public Widget createConfigurator() {
        WidgetGroup parallelAmountGroup = new WidgetGroup(0, 0, 100, 20);
        parallelAmountGroup.addWidget(new IntInputWidget(machine::getParallelNumber, machine::setParallelNumber) {
            @Override
            public void writeInitialData(FriendlyByteBuf buffer) {
                super.writeInitialData(buffer);
                buffer.writeVarInt(machine.getMaxParallel());
                setMax(machine.getMaxParallel());
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public void readInitialData(FriendlyByteBuf buffer) {
                super.readInitialData(buffer);
                setMax(buffer.readVarInt());
            }

            @Override
            public void detectAndSendChanges() {
                super.detectAndSendChanges();
                int newMaxParallel = machine.getMaxParallel();
                if (newMaxParallel != getMax()) {
                    setMax(newMaxParallel);
                    writeUpdateInfo(0, buf -> buf.writeVarInt(newMaxParallel));
                }
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
                super.readUpdateInfo(id, buffer);
                if (id == 0) {
                    setMax(buffer.readVarInt());
                }
            }

        }.setMin(1));
        return parallelAmountGroup;
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("gui.epimorphism.change_parallel.desc"));
        return tooltip;
    }
}
