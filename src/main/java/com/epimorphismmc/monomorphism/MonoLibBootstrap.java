package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.client.MonoLibClient;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * This class is just responsible for initializing the client or server-side mod class.
 *
 * @author GateGuardian
 * @date : 2024/8/3
 */
@Mod(MonoLib.MODID)
public class MonoLibBootstrap {

    public MonoLibBootstrap() {
        DistExecutor.unsafeRunForDist(() -> MonoLibClient::new, () -> MonoLibServer::new);
    }
}
