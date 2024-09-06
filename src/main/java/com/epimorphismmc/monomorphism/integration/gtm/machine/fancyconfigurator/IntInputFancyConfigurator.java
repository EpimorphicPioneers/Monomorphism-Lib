package com.epimorphismmc.monomorphism.integration.gtm.machine.fancyconfigurator;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.IntInputWidget;
import com.gregtechceu.gtceu.data.lang.LangHandler;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;

import net.minecraft.network.chat.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Accessors(chain = true)
public class IntInputFancyConfigurator implements IFancyConfigurator {
    @Getter
    private final IGuiTexture icon;

    private final String key;

    @Setter
    private boolean hasTooltips;

    private final Supplier<Integer> valueSupplier;
    private final Consumer<Integer> onChanged;

    @Setter
    private int min = 0;

    @Setter
    private int max = Integer.MAX_VALUE;

    public IntInputFancyConfigurator(
            String key, IGuiTexture icon, Supplier<Integer> valueSupplier, Consumer<Integer> onChanged) {
        this.key = key;
        this.icon = icon;
        this.valueSupplier = valueSupplier;
        this.onChanged = onChanged;
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
    public Widget createConfigurator() {
        return new IntInputWidget(valueSupplier, onChanged).setMax(max).setMin(min);
    }
}
