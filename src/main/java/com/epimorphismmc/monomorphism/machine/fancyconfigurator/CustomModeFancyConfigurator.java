package com.epimorphismmc.monomorphism.machine.fancyconfigurator;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.data.lang.LangHandler;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Accessors(chain = true)
public class CustomModeFancyConfigurator implements IFancyConfigurator {
    @Getter
    private final IGuiTexture icon;

    private final String key;
    private final int modes;

    @Setter
    private boolean hasTooltips;

    @Setter
    private boolean hasModeTooltips;

    private final Function<Integer, String> nameGetter;
    private final Supplier<Integer> modeGetter;
    private final Consumer<Integer> modeSetter;

    public CustomModeFancyConfigurator(
            String key,
            IGuiTexture icon,
            int modes,
            Function<Integer, String> nameGetter,
            Supplier<Integer> modeGetter,
            Consumer<Integer> modeSetter) {
        this.key = key;
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
            group.addWidget(new ButtonWidget(
                            2, 2 + i * 20, 136, 20, IGuiTexture.EMPTY, cd -> modeSetter.accept(tMode))
                    .setHoverTooltips(
                            hasModeTooltips
                                    ? List.copyOf(LangHandler.getSingleOrMultiLang(nameGetter.apply(tMode) + ".desc"))
                                    : Collections.emptyList()));
            group.addWidget(new ImageWidget(
                    2,
                    2 + i * 20,
                    136,
                    20,
                    () -> new GuiTextureGroup(
                            ResourceBorderTexture.BUTTON_COMMON
                                    .copy()
                                    .setColor(modeGetter.get() == tMode ? ColorPattern.CYAN.color : -1),
                            new TextTexture(nameGetter.apply(tMode))
                                    .setWidth(136)
                                    .setType(TextTexture.TextType.ROLL))));
        }
        return group;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(key + ".title");
    }

    @Override
    public List<Component> getTooltips() {
        return hasTooltips
                ? List.copyOf(LangHandler.getSingleOrMultiLang(key + ".desc"))
                : Collections.emptyList();
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
