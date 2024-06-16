package com.epimorphismmc.monomorphism.pattern.utils.containers;

import com.epimorphismmc.monomorphism.utility.MOUtils;

import com.gregtechceu.gtceu.api.pattern.MultiblockState;

import net.minecraft.world.level.block.Block;

public class TierOptionalContainer implements IValueContainer<Integer> {

    private int tier;
    private int record;

    @Override
    public void operate(Block block, Object data) {
        if (data instanceof Integer index) {
            if (!MOUtils.getStatusType(record, tier)) {
                tier++;
            }
            record |= 1 << (index - 1);
        }
    }

    @Override
    public Integer getValue() {
        return record;
    }

    public static int getTier(MultiblockState multiblockState, String name) {
        IValueContainer<?> container =
                multiblockState.getMatchContext().getOrCreate(name + "Value", IValueContainer::noop);

        if (container instanceof TierOptionalContainer optionalContainer) {
            int tier = optionalContainer.tier;
            if (getAllOnesInt(tier) == optionalContainer.record) return tier;
        }

        return 0;
    }

    private static int getAllOnesInt(int n) {
        if (n <= 0 || n > 32) { // Java int 类型最大为32位
            throw new IllegalArgumentException("Invalid number of bits, should be between 1 and 32");
        }
        int result = 0;
        for (int i = 0; i < n; i++) {
            result |= 1 << i;
        }
        return result;
    }
}
