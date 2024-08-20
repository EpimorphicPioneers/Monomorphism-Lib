package com.epimorphismmc.monomorphism.integration.gtm.client.renderer.item;

import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;

import static com.epimorphismmc.monomorphism.client.utils.ClientUtils.itemRenderer;

public abstract class WrappedItemRenderer implements IRenderer {

    @OnlyIn(Dist.CLIENT)
    protected BakedModel getVanillaModel(
            ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        var shaper = itemRenderer().getItemModelShaper();
        var model = shaper.getItemModel(stack.getItem());
        if (model != null) {
            var bakedmodel = model.getOverrides().resolve(model, stack, level, entity, 0);
            if (bakedmodel != null) return bakedmodel;
        }
        return shaper.getModelManager().getMissingModel();
    }

    @OnlyIn(Dist.CLIENT)
    protected void vanillaRender(
            ItemStack stack,
            ItemDisplayContext transformType,
            boolean leftHand,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            int combinedOverlay,
            BakedModel model) {
        IItemRendererProvider.disabled.set(true);
        Minecraft.getInstance()
                .getItemRenderer()
                .render(
                        stack,
                        transformType,
                        leftHand,
                        poseStack,
                        buffer,
                        combinedLight,
                        combinedOverlay,
                        model);
        IItemRendererProvider.disabled.set(false);
    }
}
