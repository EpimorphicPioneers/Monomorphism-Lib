package com.epimorphismmc.monomorphism.integration.gtm.item.component;

import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;

import net.minecraft.world.item.ItemStack;

public interface IMODurabilityBar extends IDurabilityBar {

    @Override
    int getMaxDurability(ItemStack itemStack);

    int getDamage(ItemStack itemStack);

    void setDamage(ItemStack itemStack, int damage);

    default void applyDamage(ItemStack itemStack, int damageApplied) {
        if (getMaxDurability(itemStack) < 1) return;
        int maxDurability = getMaxDurability(itemStack);
        int resultDamage = getDamage(itemStack) + damageApplied;
        if (resultDamage >= maxDurability) {
            itemStack.shrink(1);
        } else {
            setDamage(itemStack, resultDamage);
        }
    }

    default int getDurability(ItemStack itemStack) {
        return Math.max(getMaxDurability(itemStack) - getDamage(itemStack), 0);
    }

    @Override
    default float getDurabilityForDisplay(ItemStack itemStack) {
        return (float) getDurability(itemStack) / getMaxDurability(itemStack);
    }

    @Override
    default boolean showEmptyBar(ItemStack itemStack) {
        return getMaxDurability(itemStack) > 0;
    }

    @Override
    default boolean isBarVisible(ItemStack stack) {
        return getMaxDurability(stack) > 0;
    }
}
