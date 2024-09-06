package com.epimorphismmc.monomorphism.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

public interface INetworkHandler {

    ResourceLocation getName();

    String getVersion();

    <MSG extends IPacket> void register(Class<MSG> clazz, NetworkDirection direction);

    void send(PacketDistributor.PacketTarget target, IPacket packet);
}
