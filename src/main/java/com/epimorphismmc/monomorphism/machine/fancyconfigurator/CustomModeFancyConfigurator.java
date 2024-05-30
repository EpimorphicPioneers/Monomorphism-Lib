package com.epimorphismmc.monomorphism.machine.fancyconfigurator;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.gregtechceu.gtceu.utils.SupplierMemoizer;
import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CustomModeFancyConfigurator implements IFancyConfigurator {
    @Getter
    private final IGuiTexture icon;
    @Getter
    private final Component title;
    private final int modes;

    private final Function<Integer, String> nameGetter;
    private final Supplier<Integer> modeGetter;
    private final Consumer<Integer> modeSetter;

    private Supplier<List<Component>> tooltipSupplier = Collections::emptyList;
    private Supplier<List<Component>> modeTooltipSupplier = Collections::emptyList;

    public CustomModeFancyConfigurator(Component title, IGuiTexture icon, int modes,
                                       Function<Integer, String> nameGetter,
                                       Supplier<Integer> modeGetter,
                                       Consumer<Integer> modeSetter) {
        this.title = title;
        this.icon = icon;
        this.modes = modes;
        this.nameGetter = nameGetter;
        this.modeGetter = modeGetter;
        this.modeSetter = modeSetter;
    }

    @Override
    public Widget createConfigurator() {
        WidgetGroup group = new WidgetGroup(0, 0, 140, 20 * modes + 4);
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);

        for (int i = 0; i < modes; ++i) {
            int tMode = i;
            group.addWidget(new ButtonWidget(2, 2 + i * 20, 136, 20,
                    IGuiTexture.EMPTY, cd -> modeSetter.accept(tMode))
                    .setHoverTooltips(modeTooltipSupplier.get()));
            group.addWidget(new ImageWidget(2, 2 + i * 20, 136, 20,
                    () -> new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON.copy()
                            .setColor(modeGetter.get() == tMode ? ColorPattern.CYAN.color : -1),
                            new TextTexture(nameGetter.apply(tMode))
                                    .setWidth(136)
                                    .setType(TextTexture.TextType.ROLL))));
        }
        return group;
    }

    public void setTooltips(String key) {
        this.tooltipSupplier = SupplierMemoizer.memoize(() -> List.copyOf(LangHandler.getSingleOrMultiLang(key)));
    }

    public void setModeTooltips(String key) {
        this.modeTooltipSupplier = SupplierMemoizer.memoize(() -> List.copyOf(LangHandler.getSingleOrMultiLang(key)));
    }

    @Override
    public List<Component> getTooltips() {
        return tooltipSupplier.get();
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        buffer.writeVarInt(modeGetter.get());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readInitialData(FriendlyByteBuf buffer) {
        modeSetter.accept(buffer.readVarInt());
    }

    @Override
    public void detectAndSendChange(BiConsumer<Integer, Consumer<FriendlyByteBuf>> sender) {
        sender.accept(0, buf -> buf.writeVarInt(modeGetter.get()));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            modeSetter.accept(buffer.readVarInt());
        }
    }
}
