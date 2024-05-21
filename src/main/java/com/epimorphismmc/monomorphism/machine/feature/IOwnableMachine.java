package com.epimorphismmc.monomorphism.machine.feature;

import com.epimorphismmc.monomorphism.Monomorphism;
import com.gregtechceu.gtceu.api.machine.feature.IDataInfoProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.common.item.PortableScannerBehavior;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOwnableMachine extends IMachineLife, IDataInfoProvider {
    UUID getOwnerUUID();

    void setOwnerUUID(UUID uuid);

    @Override
    default void onMachinePlaced(@Nullable LivingEntity player, ItemStack stack) {
        if (player != null) {
            setOwnerUUID(player.getUUID());
        }
    }

    @Override
    default void onMachineRemoved() {
        setOwnerUUID(null);
    }

    @NotNull
    @Override
    default List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        var name = Optional.ofNullable(Monomorphism.instance.getMinecraftServer())
                .map(MinecraftServer::getProfileCache)
                .flatMap(cache -> cache.get(getOwnerUUID()))
                .map(GameProfile::getName)
                .orElse("Null");

        return List.of(Component.translatable("monomorphism.machine.owner", name));
    }
}
