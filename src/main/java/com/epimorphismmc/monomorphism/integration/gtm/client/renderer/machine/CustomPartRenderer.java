package com.epimorphismmc.monomorphism.integration.gtm.client.renderer.machine;

import com.epimorphismmc.monomorphism.client.model.FaceQuadBakery;
import com.epimorphismmc.monomorphism.client.model.ModelFactory;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.client.renderer.machine.IControllerRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import com.lowdragmc.lowdraglib.LDLib;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class CustomPartRenderer extends WorkableCasingMachineRenderer
        implements IControllerRenderer {

    @OnlyIn(Dist.CLIENT)
    protected Function<IMultiPart, ResourceLocation> locationGetter;

    public CustomPartRenderer(
            ResourceLocation baseCasing,
            ResourceLocation workableModel,
            Function<IMultiPart, ResourceLocation> locationGetter) {
        super(baseCasing, workableModel);
        if (LDLib.isClient()) {
            this.locationGetter = locationGetter;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderPartModel(
            List<BakedQuad> quads,
            IMultiController machine,
            IMultiPart part,
            Direction frontFacing,
            @Nullable Direction side,
            RandomSource rand,
            Direction modelFacing,
            ModelState modelState) {
        if (frontFacing != null && side != null) {
            quads.add(FaceQuadBakery.bakeFace(
                    side, ModelFactory.getBlockSprite(locationGetter.apply(part)), modelState));
        }
    }
}
