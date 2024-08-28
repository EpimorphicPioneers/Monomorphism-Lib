package com.epimorphismmc.monomorphism.integration.gtm.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class MOBlockProperties {

    public static final IntegerProperty STRUCTURE_TIER =
            IntegerProperty.create("structure_tier", 0, 15);
}
