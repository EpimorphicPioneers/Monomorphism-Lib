package com.epimorphismmc.monomorphism.data.chemical.material.info;

import com.gregtechceu.gtceu.api.data.chemical.material.info.MaterialIconSet;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MOMaterialIconSet extends MaterialIconSet {

    @Getter
    private final ICustomRenderer customRenderer;
    public MOMaterialIconSet(@NotNull String name, ICustomRenderer customRenderer) {
        super(name);
        this.customRenderer = customRenderer;
    }

    public MOMaterialIconSet(@NotNull String name, @NotNull MaterialIconSet parentIconset, ICustomRenderer customRenderer) {
        super(name, parentIconset);
        this.customRenderer = customRenderer;
    }

    public MOMaterialIconSet(@NotNull String name, @Nullable MaterialIconSet parentIconset, boolean isRootIconset, ICustomRenderer customRenderer) {
        super(name, parentIconset, isRootIconset);
        this.customRenderer = customRenderer;
    }
}
