package com.epimorphismmc.monomorphism;

import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Monomorphism.MODID)
public class Monomorphism {
    public static final String MODID = "monomorphism";
    public static final String NAME = "Monomorphism";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static Monomorphism instance;

    public Monomorphism() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        instance = this;
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, FormattingUtil.toLowerCaseUnder(path));
    }
}
