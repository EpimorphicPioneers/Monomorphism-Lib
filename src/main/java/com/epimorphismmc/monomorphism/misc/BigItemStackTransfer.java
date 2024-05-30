package com.epimorphismmc.monomorphism.misc;

import com.epimorphismmc.monomorphism.utility.MONBTUtils;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BigItemStackTransfer extends ItemStackTransfer {

    @Setter @Getter
    private boolean acceptTag;
    @Setter @Getter
    private int slotLimit = 64;

    public BigItemStackTransfer() {
    }

    public BigItemStackTransfer(int size) {
        super(size);
    }

    public BigItemStackTransfer(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public BigItemStackTransfer(ItemStack stack) {
        super(stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        if (!acceptTag && stack.hasTag()) return 0;
        return stack.isStackable() ? getSlotLimit(slot) : 1;
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag nbtTagList = new ListTag();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                MONBTUtils.writeItemStack(stacks.get(i), itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        nbt.putBoolean("AcceptTag", acceptTag);
        nbt.putInt("SlotLimit", slotLimit);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.acceptTag = nbt.getBoolean("AcceptTag");
        this.slotLimit = nbt.getInt("SlotLimit");
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(slot, MONBTUtils.readItemStack(itemTags));
            }
        }
        onLoad();
    }
}
