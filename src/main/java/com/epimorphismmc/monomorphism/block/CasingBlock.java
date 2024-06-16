package com.epimorphismmc.monomorphism.block;

import com.gregtechceu.gtceu.api.block.RendererBlock;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CasingBlock extends RendererBlock {
    private final List<Component> tooltips = new ArrayList<>();

    public CasingBlock(Properties properties, IRenderer renderer) {
        super(properties, renderer);
    }

    public void addTooltip(Component... components) {
        this.tooltips.addAll(List.of(components));
    }

    public void addTooltip(List<MutableComponent> components) {
        this.tooltips.addAll(components);
    }

    public void appendHoverText(
            ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (!this.tooltips.isEmpty()) {
            tooltip.addAll(this.tooltips);
        }
    }
}
