package com.epimorphismmc.monomorphism.mixins.shimmer;

import com.lowdragmc.shimmer.forge.platform.ForgePlatformHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(value = ForgePlatformHelper.class, remap = false)
public abstract class ForgePlatformHelperMixin {

    @Inject(method = "isDevelopmentEnvironment()Z", at = @At("HEAD"), cancellable = true)
    private void isDevelopmentEnvironment(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

}
