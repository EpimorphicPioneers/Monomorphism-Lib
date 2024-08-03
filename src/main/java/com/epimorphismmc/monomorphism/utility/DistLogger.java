package com.epimorphismmc.monomorphism.utility;

import net.minecraftforge.fml.util.thread.EffectiveSide;

import lombok.experimental.Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistLogger implements Logger {

    private static final String SERVER_SUFFIX = ":S";
    private static final String CLIENT_SUFFIX = ":C";

    private final Logger server;
    private final Logger client;

    public DistLogger(String name) {
        this.server = LoggerFactory.getLogger(name + SERVER_SUFFIX);
        this.client = LoggerFactory.getLogger(name + CLIENT_SUFFIX);
    }

    @Delegate
    private Logger logger() {
        return EffectiveSide.get().isServer() ? server : client;
    }
}
