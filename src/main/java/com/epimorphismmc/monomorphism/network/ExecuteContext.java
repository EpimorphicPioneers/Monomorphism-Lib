package com.epimorphismmc.monomorphism.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;

public class ExecuteContext {

    private final NetworkEvent.Context context;

    public ExecuteContext(NetworkEvent.Context ctx) {
        this.context = ctx;
    }

    public LogicalSide getSide() {
        return context.getDirection().getReceptionSide();
    }

    @Nullable public ServerPlayer getPlayer() {
        return context.getSender();
    }
}
