package com.epimorphismmc.monomorphism.datagen;

import com.epimorphismmc.monomorphism.datagen.lang.MOLangProvider;
import com.tterrag.registrate.providers.ProviderType;

public class MOProviderTypes {

    public static final ProviderType<MOLangProvider> MO_LANG = ProviderType.register("mo_lang", (p, e) -> new MOLangProvider(p, e.getGenerator().getPackOutput()));


}
