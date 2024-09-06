package com.epimorphismmc.monomorphism.network;

import com.epimorphismmc.monomorphism.MonoLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import lombok.Getter;

import java.util.Optional;

public class NetworkHandler implements INetworkHandler {

    @Getter
    private final ResourceLocation name;

    @Getter
    private final String version;

    private final SimpleChannel channel;

    private int id = 0;

    public NetworkHandler(ResourceLocation name, String version) {
        this.name = name;
        this.version = version;
        this.channel =
                NetworkRegistry.newSimpleChannel(name, () -> version, version::equals, version::equals);
    }

    @Override
    public <MSG extends IPacket> void register(Class<MSG> clazz, NetworkDirection direction) {
        channel.registerMessage(
                id++,
                clazz,
                IPacket::encode,
                buffer -> {
                    try {
                        MSG packet = clazz.getDeclaredConstructor().newInstance();
                        packet.decode(buffer);
                        return packet;
                    } catch (ReflectiveOperationException e) {
                        MonoLib.LOGGER.error("Unable to create an IPacket instance!", e);
                        return null;
                    }
                },
                (msg, ctx) -> {
                    NetworkEvent.Context context = ctx.get();
                    context.enqueueWork(() -> msg.execute(new ExecuteContext(context)));
                    context.setPacketHandled(true);
                },
                Optional.ofNullable(direction));
    }

    @Override
    public void send(PacketDistributor.PacketTarget target, IPacket packet) {
        channel.send(target, packet);
    }
}
