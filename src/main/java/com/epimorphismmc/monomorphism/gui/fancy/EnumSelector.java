package com.epimorphismmc.monomorphism.gui.fancy;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfiguratorButton;
import com.gregtechceu.gtceu.api.gui.widget.EnumSelectorWidget;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;
import lombok.Setter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumSelector<T extends Enum<T> & EnumSelectorWidget.SelectableEnum> implements IFancyConfiguratorButton {

    @Setter
    protected Int2ObjectFunction<IGuiTexture> texture;

    @Setter
    protected int range;
    private final List<T> values;
    private final Supplier<T> supplier;
    private final Consumer<T> onChanged;

    private int index = 0;

    private BiFunction<T, IGuiTexture, IGuiTexture> textureSupplier = (value, texture) ->
            new GuiTextureGroup(GuiTextures.VANILLA_BUTTON, texture);

    private BiFunction<T, String, List<Component>> tooltipSupplier = (value, key) ->
            List.copyOf(LangHandler.getSingleOrMultiLang(key));

    public EnumSelector(T[] values, Supplier<T> supplier, Consumer<T> onChanged) {
        this(Arrays.asList(values), supplier, onChanged);
    }

    public EnumSelector(List<T> values, Supplier<T> supplier, Consumer<T> onChanged) {
        this.values = values;
        this.supplier = supplier;
        this.onChanged = onChanged;
    }

    public void setSelected(@NotNull T value) {
        var selectedIndex = values.indexOf(value);

        if (selectedIndex == -1)
            throw new NoSuchElementException(value + " is not a possible value for this selector.");

        this.index = selectedIndex;

        onChanged.accept(value);
    }

    private void onSelected(int selected) {
        T selectedValue = values.get(selected);
        setSelected(selectedValue);
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        var selectedIndex = values.indexOf(supplier.get());
        if (selectedIndex != index) {
            this.index = selectedIndex;
            buffer.writeVarInt(index);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readInitialData(FriendlyByteBuf buffer) {
        onSelected(buffer.readVarInt());
    }

    @Override
    public void detectAndSendChange(BiConsumer<Integer, Consumer<FriendlyByteBuf>> sender) {
        var selectedIndex = values.indexOf(supplier.get());
        if (selectedIndex != index) {
            this.index = selectedIndex;
            sender.accept(0, buf -> buf.writeVarInt(index));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        if (id == 0) {
            this.index = buffer.readVarInt();
        }
    }

    public T getCurrentValue() {
        return values.get(index);
    }

    private IGuiTexture getTexture(int selected) {
        var selectedValue = values.get(selected);
        return textureSupplier.apply(selectedValue, selectedValue.getIcon());
    }

    public EnumSelector<T> setTextureSupplier(BiFunction<T, IGuiTexture, IGuiTexture> textureSupplier) {
        this.textureSupplier = textureSupplier;
        return this;
    }

    public EnumSelector<T> setTooltipSupplier(BiFunction<T, String, List<Component>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    @Override
    public List<Component> getTooltips() {
        T selectedValue = getCurrentValue();
        return this.tooltipSupplier.apply(selectedValue, selectedValue.getTooltip());
    }

    @Override
    public void onClick(ClickData clickData) {
        index++;
        if (index >= range) {
            index = 0;
        }
        if (onChanged != null) {
            onChanged.accept(getCurrentValue());
        }
    }

    @Override
    public IGuiTexture getIcon() {
        return texture.get(index);
    }
}
