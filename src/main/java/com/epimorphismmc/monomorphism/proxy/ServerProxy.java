package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.proxy.base.IServerProxyBase;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy, IServerProxyBase {
    public ServerProxy() {}

}
