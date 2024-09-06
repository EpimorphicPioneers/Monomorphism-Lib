package com.epimorphismmc.monomorphism.utility;

import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SideUtils {

    /**
     * @return The physical side, is always Side.SERVER on the server and Side.CLIENT on the client
     */
    public static Dist getPhysicalSide() {
        return FMLEnvironment.dist;
    }

    /**
     * @return The effective side, on the server, this is always Side.SERVER, on the client it is dependent on the thread
     */
    public static LogicalSide getEffectiveSide() {
        return EffectiveSide.get();
    }

    /**
     * Can be used to get the current level the client is in.
     *
     * @return empty if no client level is available (i.e. on a dedicated server)
     */
    public static Optional<Level> getClientLevel() {
        return getClientLevel(EffectiveSide.get());
    }

    public static Optional<Level> getClientLevel(LogicalSide side) {
        return LogicalSidedProvider.CLIENTWORLD.get(side);
    }

    /** Queues a task to be executed on this side */
    public static CompletableFuture<Void> queueTask(Runnable task) {
        return queueTask(getEffectiveSide(), task);
    }

    public static CompletableFuture<Void> queueTask(LogicalSide side, Runnable task) {
        BlockableEventLoop<?> executor = LogicalSidedProvider.WORKQUEUE.get(side);
        // Must check ourselves as Minecraft will sometimes delay tasks even when they are received on
        // the client thread
        // Same logic as ThreadTaskExecutor#runImmediately without the join
        if (!executor.isSameThread()) {
            return executor.submitAsync(task); // Use the internal method so thread check isn't done twice
        } else {
            task.run();
            return CompletableFuture.completedFuture(null);
        }
    }
}
