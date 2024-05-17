package com.epimorphismmc.monomorphism;

import com.epimorphismmc.monomorphism.datagen.lang.MOLangHandler;
import com.epimorphismmc.monomorphism.proxy.ClientProxy;
import com.epimorphismmc.monomorphism.proxy.CommonProxy;
import com.epimorphismmc.monomorphism.registry.registrate.MORegistrate;
import com.gregtechceu.gtceu.api.addon.AddonFinder;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.lowdragmc.lowdraglib.networking.INetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import static com.epimorphismmc.monomorphism.datagen.MOProviderTypes.*;

@Mod(Monomorphism.MODID)
public class Monomorphism extends MOMod<CommonProxy> {
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
        AddonFinder.getAddons();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected CommonProxy createClientProxy() {
        return new ClientProxy();
    }

    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    protected CommonProxy createServerProxy() {
        return new CommonProxy();
    }

    @Override
    public void addDataGenerator(MORegistrate registrate) {
        registrate.addDataGenerator(MO_LANG, MOLangHandler::init);
    }

    public static Logger logger() {
        return instance.getLogger();
    }

    public static CommonProxy proxy() {
        return instance.getProxy();
    }

    public static MORegistrate registrate() {
        return instance.getRegistrate();
    }

    public static INetworking network() {
        return instance.getNetwork();
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path));
    }
}
