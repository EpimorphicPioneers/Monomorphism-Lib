package com.epimorphismmc.monomorphism.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IPacket {

    void encode(FriendlyByteBuf buf);

    void decode(FriendlyByteBuf buf);

    default void execute(ExecuteContext ctx) {
        if (ctx.getSide().isClient()) {
            clientExecute(ctx);
        } else {
            serverExecute(ctx);
        }
    }

    @OnlyIn(Dist.CLIENT)
    default void clientExecute(ExecuteContext ctx) {}

    default void serverExecute(ExecuteContext ctx) {}
}
