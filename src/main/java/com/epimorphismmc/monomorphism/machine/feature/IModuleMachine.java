package com.epimorphismmc.monomorphism.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import org.jetbrains.annotations.Nullable;

public interface IModuleMachine<T extends IMultiController> extends IMachineFeature {
    @Nullable
    T getHost();

    void setHost(T provider);

}
