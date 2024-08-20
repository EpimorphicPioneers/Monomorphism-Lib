package com.epimorphismmc.monomorphism.mixins;

import com.epimorphismmc.monomorphism.MOValues;
import com.epimorphismmc.monomorphism.utility.Platform;
import com.epimorphismmc.monomorphism.utility.ReflectionUtils;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ConfigPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("com.epimorphismmc.monomorphism.mixins.shimmer")) {
            return Platform.isModLoaded(MOValues.MODID_SHIMMER);
        } else if (mixinClassName.contains("com.epimorphismmc.monomorphism.mixins.gtm")) {
            return Platform.isModLoaded(MOValues.MODID_GTM);
        } else if (mixinClassName.contains("com.epimorphismmc.monomorphism.mixins.ldlib")) {
            return Platform.isModLoaded(MOValues.MODID_LDLIB);
        } else if (mixinClassName.contains("com.epimorphismmc.monomorphism.mixins.registrate")) {
            return ReflectionUtils.isClassFound("com.tterrag.registrate.Registrate");
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(
            String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(
            String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
