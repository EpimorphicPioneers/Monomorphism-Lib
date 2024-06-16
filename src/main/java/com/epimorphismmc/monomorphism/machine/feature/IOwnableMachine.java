package com.epimorphismmc.monomorphism.machine.feature;

import com.epimorphismmc.monomorphism.utility.MOUtils;

import com.gregtechceu.gtceu.api.machine.feature.IDataInfoProvider;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.common.item.PortableScannerBehavior;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    @NotNull @Override
    default List<Component> getDataInfo(PortableScannerBehavior.DisplayMode mode) {
        return List.of(Component.translatable(
                "monomorphism.machine.owner", MOUtils.getPlayerName(getOwnerUUID(), null)));
    }
}
