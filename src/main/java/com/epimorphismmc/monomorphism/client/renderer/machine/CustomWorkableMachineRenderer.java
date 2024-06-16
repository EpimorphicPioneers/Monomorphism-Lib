package com.epimorphismmc.monomorphism.client.renderer.machine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.client.model.WorkableOverlayModel;
import com.gregtechceu.gtceu.client.renderer.machine.TieredHullMachineRenderer;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CustomWorkableMachineRenderer extends TieredHullMachineRenderer {

    protected final WorkableOverlayModel overlayModel;
    protected final Predicate<MetaMachine> isWorkingEnabled;
    protected final Predicate<MetaMachine> isActive;

    public CustomWorkableMachineRenderer(
            int tier, ResourceLocation workableModel, Predicate<MetaMachine> isActive) {
        this(tier, workableModel, isActive, isActive);
    }

    public CustomWorkableMachineRenderer(
            int tier,
            ResourceLocation workableModel,
            Predicate<MetaMachine> isWorkingEnabled,
            Predicate<MetaMachine> isActive) {
        super(tier, GTCEu.id("block/machine/hull_machine"));
        this.overlayModel = new WorkableOverlayModel(workableModel);
        this.isWorkingEnabled = isWorkingEnabled;
        this.isActive = isActive;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderMachine(
            List<BakedQuad> quads,
            MachineDefinition definition,
            @Nullable MetaMachine machine,
            Direction frontFacing,
            @Nullable Direction side,
            RandomSource rand,
            Direction modelFacing,
            ModelState modelState) {
        super.renderMachine(
                quads, definition, machine, frontFacing, side, rand, modelFacing, modelState);
        Direction upwardsFacing = Direction.NORTH;
        if (machine instanceof IMultiController multi) {
            upwardsFacing = multi.self().getUpwardsFacing();
        }
        if (machine != null) {
            quads.addAll(overlayModel.bakeQuads(
                    side,
                    frontFacing,
                    upwardsFacing,
                    isActive.test(machine),
                    isWorkingEnabled.test(machine)));
        } else {
            quads.addAll(overlayModel.bakeQuads(side, frontFacing, upwardsFacing, false, false));
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onPrepareTextureAtlas(
            ResourceLocation atlasName, Consumer<ResourceLocation> register) {
        super.onPrepareTextureAtlas(atlasName, register);
        if (atlasName.equals(TextureAtlas.LOCATION_BLOCKS)) {
            overlayModel.registerTextureAtlas(register);
        }
    }
}
