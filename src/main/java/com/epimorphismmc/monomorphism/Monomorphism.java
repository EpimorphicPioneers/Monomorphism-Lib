package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.datagen.lang.MOLangHandler;
import com.epimorphismmc.monomorphism.proxy.ClientProxy;
import com.epimorphismmc.monomorphism.proxy.IProxy;
import com.epimorphismmc.monomorphism.proxy.ServerProxy;
import com.epimorphismmc.monomorphism.registry.registrate.MORegistrate;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import static com.epimorphismmc.monomorphism.datagen.MOProviderTypes.*;

@Mod(Monomorphism.MODID)
public class Monomorphism extends MOMod<IProxy> {
    public static final String MODID = "monomorphism";
    public static final String NAME = "Monomorphism";

    public static Monomorphism instance;

    public Monomorphism() {
        super();
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public String getModName() {
        return NAME;
    }

    @Override
    protected void onModConstructed() {
        instance = this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected IProxy createClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected IProxy createServerProxy() {
        return new ServerProxy();
    }

    @Override
    public void addDataGenerator(MORegistrate registrate) {
        registrate.addDataGenerator(MO_LANG, MOLangHandler::init);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path));
    }
}
