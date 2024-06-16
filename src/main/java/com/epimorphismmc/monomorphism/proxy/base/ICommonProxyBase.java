package com.epimorphismmc.monomorphism.proxy.base;

import com.epimorphismmc.monomorphism.Monomorphism;
import com.epimorphismmc.monomorphism.capability.CapabilityHandler;
import com.epimorphismmc.monomorphism.capability.ICapabilityImplementation;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.addon.events.KJSRecipeKeyEvent;
import com.gregtechceu.gtceu.api.addon.events.MaterialCasingCollectionEvent;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.cover.CoverDefinition;
import com.gregtechceu.gtceu.api.data.chemical.Element;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.data.worldgen.GTOreDefinition;
import com.gregtechceu.gtceu.api.data.worldgen.bedrockfluid.BedrockFluidDefinition;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.server.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

public interface ICommonProxyBase {

    /**
     * Called to register the event handlers
     */
    void registerEventHandlers();

    /**
     * Called to register the capabilities for this mod
     */
    void registerCapabilities();

    /**
     * Called to register event handlers for FML IModBusEvent events
     * @param bus the bus for the mod
     */
    default void registerModBusEventHandlers(IEventBus bus) {
        bus.addListener(this::registerMaterialRegistry);
        bus.addListener(this::registerMaterials);
        bus.addListener(this::postRegisterMaterials);

        bus.addGenericListener(Element.class, this::registerElements);
        bus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        bus.addGenericListener(
                (Class<Class<? extends RecipeCondition>>) RecipeCondition.class.getClass(),
                this::registerRecipeConditions);
        bus.addGenericListener(RecipeCapability.class, this::registerRecipeCapabilities);
        bus.addGenericListener(MachineDefinition.class, this::registerMachineDefinitions);
        bus.addGenericListener(CoverDefinition.class, this::registerCoverDefinitions);
        bus.addGenericListener(SoundEntry.class, this::registerSoundEntries);
        bus.addGenericListener(BedrockFluidDefinition.class, this::registerBedrockFluidDefinitions);
        bus.addGenericListener(GTOreDefinition.class, this::registerOreDefinitions);
    }

    /** Registers an event handler */
    default void registerEventHandler(Object handler) {
        Monomorphism.instance
                .getLogger()
                .debug("Registering event handler: " + handler.getClass().getName());
        MinecraftForge.EVENT_BUS.register(handler);
    }

    /** Registers a capability */
    @SuppressWarnings("unchecked")
    default void registerCapability(
            ICapabilityImplementation<? extends ICapabilityProvider, ?> capability) {
        CapabilityHandler.getInstance().registerCapability(capability);
    }

    default void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {}

    default void registerRecipeCapabilities(
            GTCEuAPI.RegisterEvent<String, RecipeCapability<?>> event) {}

    default void registerRecipeConditions(
            GTCEuAPI.RegisterEvent<String, Class<? extends RecipeCondition>> event) {}

    default void registerRecipeKeys(KJSRecipeKeyEvent event) {}

    default void registerTagPrefixes() {}

    default void registerElements(GTCEuAPI.RegisterEvent<String, Element> event) {}

    default void registerMaterialRegistry(MaterialRegistryEvent event) {}

    default void registerMaterials(MaterialEvent event) {}

    default void postRegisterMaterials(PostMaterialEvent event) {}

    default void collectMaterialCasings(MaterialCasingCollectionEvent event) {}

    default void registerMachineDefinitions(
            GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {}

    default void registerCoverDefinitions(
            GTCEuAPI.RegisterEvent<ResourceLocation, CoverDefinition> event) {}

    default void registerSoundEntries(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {}

    default void registerWorldgenLayers() {}

    default void registerVeinGenerators() {}

    default void registerIndicatorGenerators() {}

    default void registerOreDefinitions(
            GTCEuAPI.RegisterEvent<ResourceLocation, GTOreDefinition> event) {}

    default void registerBedrockFluidDefinitions(
            GTCEuAPI.RegisterEvent<ResourceLocation, BedrockFluidDefinition> event) {}

    /**
     * -----------------------------
     * FML MOD LOADING CYCLE METHODS
     * -----------------------------
     */
    default void onCommonSetupEvent(final FMLCommonSetupEvent event) {}

    default void onClientSetupEvent(final FMLClientSetupEvent event) {}

    default void onDedicatedServerSetupEvent(final FMLDedicatedServerSetupEvent event) {}

    default void onInterModEnqueueEvent(final InterModEnqueueEvent event) {}

    default void onInterModProcessEvent(final InterModProcessEvent event) {}

    default void onModLoadCompleteEvent(final FMLLoadCompleteEvent event) {}

    default void onServerStartingEvent(final ServerStartingEvent event) {}
    ;

    default void onServerAboutToStartEvent(final ServerAboutToStartEvent event) {}
    ;

    default void onServerStoppingEvent(final ServerStoppingEvent event) {}
    ;

    default void onServerStoppedEvent(final ServerStoppedEvent event) {}
    ;

    /**
     * ---------------
     * UTILITY METHODS
     * ---------------
     */

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    default Dist getPhysicalSide() {
        return FMLEnvironment.dist;
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    default LogicalSide getLogicalSide() {
        return EffectiveSide.get();
    }

    /**
     * @return The minecraft server instance
     */
    default MinecraftServer getMinecraftServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    /**
     * @return registry access instance
     */
    default RegistryAccess getRegistryAccess() {
        return this.getMinecraftServer().registryAccess();
    }

    /**
     * @return the instance of the EntityPlayer on the client, null on the server
     */
    default Player getClientPlayer() {
        return null;
    }

    /**
     * @return the client World object on the client, null on the server
     */
    default Level getClientWorld() {
        return null;
    }

    /**
     *  @return  the entity in that World object with that id
     */
    default Entity getEntityById(Level world, int id) {
        return world == null ? null : world.getEntity(id);
    }

    /**
     *  @return  the entity in that World object with that id
     */
    default Entity getEntityById(ResourceKey<Level> dimension, int id) {
        return this.getEntityById(this.getWorldFromDimension(dimension), id);
    }

    /**
     *  @return the render view entity on the client, null on the server
     */
    @Nullable default Entity getRenderViewEntity() {
        return null;
    }

    /**
     *  Sets the render view entity on the client
     */
    default void setRenderViewEntity(Entity entity) {}

    /**
     *  @return the World object ofr a given dimension key
     */
    default Level getWorldFromDimension(ResourceKey<Level> dimension) {
        return this.getMinecraftServer().getLevel(dimension);
    }

    /**
     * @return the fov setting on the client, 70 on the server
     */
    default double getFieldOfView() {
        return 70;
    }

    /** Queues a task to be executed on this side */
    default void queueTask(Runnable task) {
        this.getMinecraftServer()
                .submit(new TickTask(this.getMinecraftServer().getTickCount() + 1, task));
    }
}
