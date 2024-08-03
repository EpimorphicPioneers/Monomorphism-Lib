package com.epimorphismmc.monomorphism;

import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

/**
 * Contains mod functionality specific to a dedicated server.
 *
 * @author GateGuardian
 * @date : 2024/8/3
 */
public class MonoLibServer extends MonoLibCommon {

    public MonoLibServer() {
        super();
    }

    @Override
    public @Nullable Level getClientLevel() {
        return null;
    }
}
