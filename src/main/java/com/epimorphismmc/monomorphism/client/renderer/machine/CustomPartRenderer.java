package com.epimorphismmc.monomorphism.client.renderer.machine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.client.renderer.machine.IControllerRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;

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
            List<BakedQuad> list,
            IMultiController iMultiController,
            IMultiPart iMultiPart,
            Direction direction,
            @Nullable Direction direction1,
            RandomSource randomSource,
            Direction direction2,
            ModelState modelState) {
        if (direction1 != null && direction2 != null) {
            list.add(FaceQuad.bakeFace(
                    direction2, ModelFactory.getBlockSprite(locationGetter.apply(iMultiPart)), modelState));
        }
    }
}
