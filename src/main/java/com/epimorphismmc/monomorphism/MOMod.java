package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.proxy.base.ICommonProxyBase;
import com.epimorphismmc.monomorphism.registry.registrate.MORegistrate;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.lowdragmc.lowdraglib.networking.INetworking;
import com.lowdragmc.lowdraglib.networking.LDLNetworking;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MOMod<P extends ICommonProxyBase> {
    private final Logger logger;
    private final INetworking network;
    private final P proxy;
    private final MORegistrate registrate;

    @SuppressWarnings("Unchecked")
    public MOMod() {
        //Populate static mod instance
        this.onModConstructed();
        //Create logger
        this.logger = LoggerFactory.getLogger(getModName());
        // Create network
        this.network = LDLNetworking.createNetworking(new ResourceLocation(getModId(), "networking"), "0.0.1");
        // Create proxy
        this.proxy = this.createProxy();
        this.registrate = MORegistrate.create(getModId());
        // Register FML mod loading cycle listeners
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus bus = context.getModEventBus();
        bus.addListener(this::onCommonSetupEvent);
        bus.addListener(this::onClientSetupEvent);
        bus.addListener(this::onDedicatedServerSetupEvent);
        bus.addListener(this::onInterModEnqueueEvent);
        bus.addListener(this::onInterModProcessEvent);
        bus.addListener(this::onModLoadCompleteEvent);
        this.proxy.registerModBusEventHandlers(bus);
        MinecraftForge.EVENT_BUS.register(this);
        registrate.registerEventListeners(bus);
        addDataGenerator(registrate);
        // Register capabilities
        this.proxy.registerCapabilities();
    }

    private P createProxy() {
        P proxy = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> this::createClientProxy);
        if (proxy == null) {
            proxy = DistExecutor.unsafeCallWhenOn(Dist.DEDICATED_SERVER, () -> this::createServerProxy);
        }
        if (proxy == null) {
            // Can only happen if the mod fails to correctly implement the createClientProxy and/or the createServerProxy methods
            throw new RuntimeException("Failed to create SidedProxy for mod " + this.getModId() + " on side: " + FMLEnvironment.dist.name());
        }
        return proxy;
    }

    private void init() {
        // Register event handlers
        this.proxy.registerEventHandlers();
        // Register messages
        registerMessages(network);
    }

    private void initClient() {

    }

    public final Logger getLogger() {
        return this.logger;
    }

    public final INetworking getNetwork() {
        return this.network;
    }

    public final P getProxy() {
        return this.proxy;
    }

    public final MORegistrate getRegistrate() {
        return this.registrate;
    }

    /**
     * @return The mod ID of the mod
     */
    public abstract String getModId();

    /**
     * @return The name of the mod
     */
    public abstract String getModName();

    /**
     * Provides access to the instantiated mod object, for instance to store it in a static field
     */
    protected abstract void onModConstructed();

    /**
     * @return Creates the client proxy object for this mod
     */
    @OnlyIn(Dist.CLIENT)
    protected abstract P createClientProxy();

    /**
     * @return Creates the server proxy object for this mod
     */
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected abstract P createServerProxy();

    /**
     * Register all messages added by this mod
     * @param network NetworkWrapper instance to register messages to
     */
    public void registerMessages(INetworking network) {}

    public void addDataGenerator(MORegistrate registrate) {}

    /**
     * --------------------------
     * FML Mod Loading Listeners
     * --------------------------
     */

    public final void onCommonSetupEvent(final FMLCommonSetupEvent event) {
        //self init
        this.init();
        //forward to proxy
        this.proxy.onCommonSetupEvent(event);
    }

    public final void onClientSetupEvent(final FMLClientSetupEvent event) {
        //self init
        this.initClient();
        //forward to proxy
        this.proxy.onClientSetupEvent(event);}

    public final void onDedicatedServerSetupEvent(final FMLDedicatedServerSetupEvent event) {
        //forward to proxy
        this.proxy.onDedicatedServerSetupEvent(event);
    }

    public final void onInterModEnqueueEvent(final InterModEnqueueEvent event) {
        //forward to proxy
        this.proxy.onInterModEnqueueEvent(event);
    }

    public final void onInterModProcessEvent(final InterModProcessEvent event) {
        //forward to proxy
        this.proxy.onInterModProcessEvent(event);
    }

    public final void onModLoadCompleteEvent(final FMLLoadCompleteEvent event) {
        //forward to proxy
        this.proxy.onModLoadCompleteEvent(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public final void onServerStartingEvent(final ServerStartingEvent event) {
        //forward to proxy
        this.proxy.onServerStartingEvent(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public final void onServerAboutToStartEvent(final ServerAboutToStartEvent event) {
        //forward to proxy
        this.proxy.onServerAboutToStartEvent(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public final void onServerStoppingEvent(final ServerStoppingEvent event) {
        //forward to proxy
        this.proxy.onServerStoppingEvent(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public final void onServerStoppedEvent(final ServerStoppedEvent event) {
        //forward to proxy
        this.proxy.onServerStoppedEvent(event);
    }

    @SubscribeEvent
    public void registerMaterialRegistry(MaterialRegistryEvent event) {
    }

    /**
     * --------------------------
     * Proxy utility method calls
     * --------------------------
     */

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    public final Dist getPhysicalSide() {
        return this.proxy.getPhysicalSide();
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    public final LogicalSide getEffectiveSide() {
        return this.proxy.getLogicalSide();
    }

    /**
     * @return The minecraft server instance
     */
    public final MinecraftServer getMinecraftServer() {
        return this.proxy.getMinecraftServer();
    }

    /**
     * @return a registry access object based on the logical side
     */
    public final RegistryAccess getRegistryAccess() {
        return this.proxy.getRegistryAccess();
    }

    /**
     * @return the instance of the EntityPlayer on the client, null on the server
     */
    public final Player getClientPlayer() {
        return this.proxy.getClientPlayer();
    }

    /**
     * @return the client World object on the client, null on the server
     */
    public final Level getClientWorld() {
        return this.proxy.getClientWorld();
    }

    /**
     * @return the client World object on the client, null on the server
     */
    public final Level getWorldFromDimension(ResourceKey<Level> dimension) {
        return this.proxy.getWorldFromDimension(dimension);
    }

    /**
     *  @return  the entity in that World object with that id
     */
    public final Entity getEntityById(Level world, int id) {
        return this.proxy.getEntityById(world, id);
    }

    /**
     *  @return  the entity in that World object with that id
     */
    public final Entity getEntityById(ResourceKey<Level> dimension, int id) {
        return this.proxy.getEntityById(dimension, id);
    }

    /** Queues a task to be executed on this side */
    public final void queueTask(Runnable task) {
        this.proxy.queueTask(task);
    }

    /** Registers an event handler */
    public final void registerEventHandler(Object handler) {
        this.proxy.registerEventHandler(handler);
    }
}
