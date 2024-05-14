package com.epimorphismmc.monomorphism.proxy;

import com.epimorphismmc.monomorphism.proxy.base.IClientProxyBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy, IClientProxyBase {
    public ClientProxy() {}

    @Override
    public void forceClientRenderUpdate(BlockPos pos) {
        Minecraft.getInstance().levelRenderer.setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }
}
