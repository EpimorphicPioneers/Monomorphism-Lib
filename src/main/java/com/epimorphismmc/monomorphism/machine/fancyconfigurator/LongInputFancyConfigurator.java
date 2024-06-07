package com.epimorphismmc.monomorphism.machine.fancyconfigurator;

import com.gregtechceu.gtceu.api.gui.fancy.IFancyConfigurator;
import com.gregtechceu.gtceu.api.gui.widget.LongInputWidget;
import com.gregtechceu.gtceu.data.lang.LangHandler;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Accessors(chain = true)
public class LongInputFancyConfigurator implements IFancyConfigurator {
    @Getter
    private final IGuiTexture icon;
    private final String key;
    @Setter
    private boolean hasTooltips;
    private final Supplier<Long> valueSupplier;
    private final Consumer<Long> onChanged;
    @Setter
    private long min = 0;
    @Setter
    private long max = Long.MAX_VALUE;

    public LongInputFancyConfigurator(String key, IGuiTexture icon, Supplier<Long> valueSupplier, Consumer<Long> onChanged) {
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
        return hasTooltips ? List.copyOf(LangHandler.getSingleOrMultiLang(key + ".desc")) : Collections.emptyList();
    }

    @Override
    public Widget createConfigurator() {
        return new LongInputWidget(valueSupplier, onChanged)
            .setMax(max)
            .setMin(min);
    }
}
