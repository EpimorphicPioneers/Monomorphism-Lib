package com.epimorphismmc.monomorphism.datagen.tag;

import com.epimorphismmc.monomorphism.block.tier.CoilTier;
import com.epimorphismmc.monomorphism.block.tier.IBlockTier;
import com.epimorphismmc.monomorphism.block.tier.MOBlockTiers;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MOBlockTagsProvider extends BlockTagsProvider {
    public MOBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        GTCEuAPI.HEATING_COILS.forEach(
                (tier, block) -> tag(MOBlockTiers.COILS.getTag(CoilTier.toTier(tier))).add(block.get()));

        if (CraftingComponent.CASING == null) {
            CraftingComponent.initializeComponents();
        }

        var tiers = IBlockTier.TierBlockType.values();
        for (int i = 0; i < tiers.length; i++) {
            tag(MOBlockTiers.MACHINES.getTag(tiers[i])).add(toBlock(CraftingComponent.CASING.getIngredient(i)));
        }
    }

    private static Block toBlock(Object object) {
        if (object instanceof ItemStack stack) {
            if (stack.getItem() instanceof BlockItem item) {
                return item.getBlock();
            }
        }
        return null;
    }
}
