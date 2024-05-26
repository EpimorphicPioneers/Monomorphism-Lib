package com.epimorphismmc.monomorphism.item.behaviors;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.IAddInformation;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.client.renderer.item.TagPrefixItemRenderer;
import com.lowdragmc.lowdraglib.Platform;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TagPrefixBehavior implements IAddInformation, ICustomDescriptionId {

    public final TagPrefix tagPrefix;
    public final Material material;

    public TagPrefixBehavior(TagPrefix tagPrefix, Material material) {
        this.tagPrefix = tagPrefix;
        this.material = material;
    }

    @Override
    public void onAttached(Item item) {
        if (Platform.isClient()) {
            TagPrefixItemRenderer.create(item, tagPrefix.materialIconType(), material.getMaterialIconSet());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (this.tagPrefix.tooltip() != null) {
            this.tagPrefix.tooltip().accept(material, tooltipComponents);
        }
    }

    @Override
    public @Nullable Component getItemName(ItemStack stack) {
        return tagPrefix.getLocalizedName(material);
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemColor tintColor() {
        return (itemStack, index) -> {
            var behavior = TagPrefixBehavior.getBehaviour(itemStack);
            if (behavior != null) {
                return behavior.material.getLayerARGB(index);
            }
            return -1;
        };
    }

    @Nullable
    public static TagPrefixBehavior getBehaviour(@NotNull ItemStack itemStack) {
        if (itemStack.getItem() instanceof ComponentItem componentItem) {
            for (var component : componentItem.getComponents()) {
                if (component instanceof TagPrefixBehavior behaviour) {
                    return behaviour;
                }
            }
        }
        return null;
    }
}
