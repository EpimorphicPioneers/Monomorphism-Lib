package com.epimorphismmc.monomorphism.integration.ldlib;

import com.epimorphismmc.monomorphism.integration.ldlib.syncdata.SyncedFieldAccessors;

import com.lowdragmc.lowdraglib.plugin.ILDLibPlugin;
import com.lowdragmc.lowdraglib.plugin.LDLibPlugin;

@LDLibPlugin
public class MOLDLibPlugin implements ILDLibPlugin {
    @Override
    public void onLoad() {
        SyncedFieldAccessors.init();
    }
}
