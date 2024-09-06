package com.epimorphismmc.monomorphism.integration.gtm.machine;

import com.gregtechceu.gtceu.api.blockentity.ITickSubscription;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;

import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ScheduledSubscriptionHandler {
    private final ITickSubscription handler;
    private final Runnable runnable;
    private final Supplier<Boolean> condition;
    private TickableSubscription subscription;

    public ScheduledSubscriptionHandler(
            ITickSubscription handler, Runnable runnable, Supplier<Boolean> condition) {
        this.handler = handler;
        this.runnable = runnable;
        this.condition = condition;
    }

    public void initialize(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            this.initialize(serverLevel.getServer());
        }
    }

    protected void initialize(BlockableEventLoop<TickTask> server) {
        server.tell(new TickTask(0, this::updateSubscription));
    }

    public void updateSubscription() {
        if (this.condition.get()) {
            this.subscription = this.handler.subscribeServerTick(this.subscription, this.runnable);
        } else if (this.subscription != null) {
            this.subscription.unsubscribe();
        }
    }

    public void unsubscribe() {
        if (this.subscription != null) {
            this.subscription.unsubscribe();
        }
    }
}
