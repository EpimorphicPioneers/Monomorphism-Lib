package com.epimorphismmc.monomorphism.data.tag;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.component.ICustomRenderer;

import lombok.Getter;

public class MOTagPrefix extends TagPrefix {
    @Getter
    private final ICustomRenderer customRenderer;

    public MOTagPrefix(String name, ICustomRenderer customRenderer) {
        super(name);
        this.customRenderer = customRenderer;
    }

    public MOTagPrefix(String name, boolean invertedName, ICustomRenderer customRenderer) {
        super(name, invertedName);
        this.customRenderer = customRenderer;
    }
}
