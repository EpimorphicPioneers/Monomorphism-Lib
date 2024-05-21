package com.epimorphismmc.monomorphism.client.renderer.machine;

import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.model.SpriteOverrider;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.lowdragmc.lowdraglib.LDLib;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
public class TierCasingMachineRenderer extends WorkableCasingMachineRenderer {

    @OnlyIn(Dist.CLIENT)
    protected Map<ResourceLocation, Map<Direction, BakedModel>> tierBlockModels;

    @OnlyIn(Dist.CLIENT)
    protected Function<MetaMachine, ResourceLocation> locationGetter;

    public TierCasingMachineRenderer(ResourceLocation baseCasing, ResourceLocation workableModel, Function<MetaMachine, ResourceLocation> locationGetter) {
        super(baseCasing, workableModel, false);
        if (LDLib.isClient()) {
            this.locationGetter = locationGetter;
            this.tierBlockModels = new ConcurrentHashMap<>();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderBaseModel(List<BakedQuad> quads, MachineDefinition definition, @Nullable MetaMachine machine, Direction frontFacing, @Nullable Direction side, RandomSource rand) {
        var location = locationGetter.apply(machine);
        if (location != null) {
            quads.addAll(getRotatedModel(frontFacing, location).getQuads(definition.defaultBlockState(), side, rand));
        } else {
            super.renderBaseModel(quads, definition, machine, frontFacing, side, rand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected BakedModel getRotatedModel(Direction frontFacing, ResourceLocation location) {
        return tierBlockModels.computeIfAbsent(location, location1 -> {
            var map = new ConcurrentHashMap<Direction, BakedModel>();
            map.put(frontFacing, renderModel(frontFacing, location1));
            return map;
        }).computeIfAbsent(frontFacing, direction -> renderModel(direction, location));
    }

    @OnlyIn(Dist.CLIENT)
    protected BakedModel renderModel(Direction frontFacing, ResourceLocation location) {
        return this.getModel().bake(ModelFactory.getModeBaker(), new SpriteOverrider(Map.of("all", location)), ModelFactory.getRotation(frontFacing), this.modelLocation);
    }
}
