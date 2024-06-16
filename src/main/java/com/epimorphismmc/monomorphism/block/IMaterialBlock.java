package com.epimorphismmc.monomorphism.block;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.world.level.block.Block;

public interface IMaterialBlock {
    TagPrefix getTagPrefix();

    Material getMaterial();

    default Block getBlock() {
        return (Block) this;
    }
}
