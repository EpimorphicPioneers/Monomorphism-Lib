package com.epimorphismmc.monomorphism.integration.gtm.item;

import com.epimorphismmc.monomorphism.integration.gtm.block.IMaterialBlock;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.DustProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;

import com.lowdragmc.lowdraglib.client.renderer.IBlockRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MOMaterialBlockItem extends BlockItem implements IItemRendererProvider {

    protected MOMaterialBlockItem(IMaterialBlock block, Properties properties) {
        super(block.getBlock(), properties);
    }

    public static MOMaterialBlockItem create(IMaterialBlock block, Item.Properties properties) {
        return new MOMaterialBlockItem(block, properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return getItemBurnTime();
    }

    public void onRegister() {}

    @OnlyIn(Dist.CLIENT)
    public static ItemColor tintColor() {
        return (itemStack, index) -> {
            if (itemStack.getItem() instanceof MOMaterialBlockItem materialBlockItem
                    && materialBlockItem.getBlock() instanceof IMaterialBlock materialBlock) {
                return materialBlock.getMaterial().getLayerARGB(index);
            }
            return -1;
        };
    }

    @Nullable @Override
    @OnlyIn(Dist.CLIENT)
    public IRenderer getRenderer(ItemStack stack) {
        if (getBlock() instanceof IBlockRendererProvider provider) {
            return provider.getRenderer(getBlock().defaultBlockState());
        }
        return null;
    }

    @Override
    public String getDescriptionId() {
        return getBlock().getDescriptionId();
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return getDescriptionId();
    }

    @Override
    public Component getDescription() {
        return getBlock().getName();
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

    public int getItemBurnTime() {
        if (getBlock() instanceof IMaterialBlock block) {
            var material = block.getMaterial();
            DustProperty property = material == null ? null : material.getProperty(PropertyKey.DUST);
            if (property != null)
                return (int) (property.getBurnTime()
                        * block.getTagPrefix().getMaterialAmount(material)
                        / GTValues.M);
        }
        return -1;
    }
}
