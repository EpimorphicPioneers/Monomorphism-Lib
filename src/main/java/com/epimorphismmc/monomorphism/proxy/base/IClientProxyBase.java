package com.epimorphismmc.monomorphism.proxy.base;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;

public interface IClientProxyBase extends IProxyBase {
    @Override
    default Entity getRenderViewEntity() {
        return Minecraft.getInstance().getCameraEntity();
    }

    default void setRenderViewEntity(Entity entity) {
        Minecraft.getInstance().setCameraEntity(entity);
    }

    @Override
    default RegistryAccess getRegistryAccess() {
        if(this.getLogicalSide().isServer()) {
            return this.getMinecraftServer().registryAccess();
        } else {
            Level client = this.getClientWorld();
            return client == null ? RegistryAccess.EMPTY : client.registryAccess();
        }
    }

    @Override
    default Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    default Level getClientWorld() {
        return Minecraft.getInstance().level;
    }

    @Override
    default Level getWorldFromDimension(ResourceKey<Level> dimension) {
        LogicalSide effectiveSide = this.getLogicalSide();
        if(effectiveSide == LogicalSide.SERVER) {
            return getMinecraftServer().getLevel(dimension);
        } else {
            return getClientWorld();
        }
    }

    @Override
    default void queueTask(Runnable task) {
        if(getLogicalSide() == LogicalSide.CLIENT) {
            Minecraft.getInstance().submit(task);
        } else {
            IProxyBase.super.queueTask(task);
        }
    }

    /**
     * @return the fov setting on the client, 90 on the server
     */
    @Override
    default double getFieldOfView() {
        return Minecraft.getInstance().options.fov().get();
    }
}
