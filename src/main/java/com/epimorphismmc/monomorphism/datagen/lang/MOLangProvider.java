package com.epimorphismmc.monomorphism.datagen.lang;

import com.epimorphismmc.monomorphism.datagen.MOProviderTypes;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.epimorphismmc.monomorphism.datagen.lang.MOLangHelper.getBlockKey;
import static com.epimorphismmc.monomorphism.datagen.lang.MOLangHelper.getSubKey;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MOLangProvider extends RegistrateLangProvider {

    private static class CNLanguageProvider extends LanguageProvider {

        public CNLanguageProvider(PackOutput packOutput, String modid) {
            super(packOutput, modid, "zh_cn");
        }

        public void addBlockWithTooltip(
                NonNullSupplier<? extends Block> block, String name, String tooltip) {
            addBlock(block, name);
            addTooltip(block, tooltip);
        }

        public void addBlockWithTooltip(
                NonNullSupplier<? extends Block> block, String name, List<@NonnullType String> tooltip) {
            addBlock(block, name);
            addTooltip(block, tooltip);
        }

        public void addItemWithTooltip(
                NonNullSupplier<? extends Item> item, String name, List<@NonnullType String> tooltip) {
            addItem(item, name);
            addTooltip(item, tooltip);
        }

        public void addItemWithTooltip(
                NonNullSupplier<? extends Item> item, String name, String tooltip) {
            addItem(item, name);
            addTooltip(item, tooltip);
        }

        public void addTooltip(NonNullSupplier<? extends ItemLike> item, String tooltip) {
            add(item.get().asItem().getDescriptionId() + ".desc", tooltip);
        }

        public void addTooltip(
                NonNullSupplier<? extends ItemLike> item, List<@NonnullType String> tooltip) {
            for (int i = 0; i < tooltip.size(); i++) {
                add(item.get().asItem().getDescriptionId() + ".desc." + i, tooltip.get(i));
            }
        }

        public void add(CreativeModeTab tab, String name) {
            var contents = tab.getDisplayName().getContents();
            if (contents instanceof TranslatableContents lang) {
                add(lang.getKey(), name);
            } else {
                throw new IllegalArgumentException(
                        "Creative tab does not have a translatable name: " + tab.getDisplayName());
            }
        }

        @Override
        public void add(@Nullable String key, @Nullable String value) {
            super.add(key, value);
        }

        @Override
        protected void addTranslations() {}
    }

    protected final AbstractRegistrate<?> owner;
    protected final String modid;
    private final CNLanguageProvider simplifiedChinese;

    public MOLangProvider(AbstractRegistrate<?> owner, PackOutput packOutput) {
        super(owner, packOutput);
        this.owner = owner;
        this.modid = owner.getModid();
        this.simplifiedChinese = new CNLanguageProvider(packOutput, owner.getModid());
    }

    public void addItemWithTooltip(
            NonNullSupplier<? extends Item> item, String name, String tooltip) {
        addItem(item, name);
        addTooltip(item, tooltip);
    }

    public void addBlockWithTooltip(
            NonNullSupplier<? extends Block> block, String name, List<@NonnullType String> tooltip) {
        addBlock(block, name);
        addTooltip(block, tooltip);
    }

    @Override
    public String getName() {
        return "Lang (en_us/en_ud/zh_cn)";
    }

    @Override
    protected void addTranslations() {
        owner.genData(MOProviderTypes.MO_LANG, this);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return CompletableFuture.allOf(super.run(cache), simplifiedChinese.run(cache));
    }

    /* -------------------------------------------------- Utilities -------------------------------------------------- */

    public void add(String key, String enText, String cnText) {
        simplifiedChinese.add(key, cnText);
        add(key, enText);
    }

    public void addMultiLang(
            Function<Integer, String> keyGetter,
            Function<Integer, String> enTextGetter,
            Function<Integer, String> cnTextGetter,
            int... tiers) {
        for (int tier : tiers) {
            var name = keyGetter.apply(tier);
            simplifiedChinese.add(name, cnTextGetter.apply(tier));
            add(name, enTextGetter.apply(tier));
        }
    }

    public void addMultiLang(String key, List<String> enText, List<String> cnText) {
        for (var i = 0; i < cnText.size(); i++) {
            simplifiedChinese.add(getSubKey(key, i), cnText.get(i));
        }

        for (var i = 0; i < enText.size(); i++) {
            add(getSubKey(key, i), enText.get(i));
        }
    }

    public void addMultilineLang(String key, String enText, String cnText) {
        multilineLang(simplifiedChinese, key, cnText);

        multilineLang(this, key, enText);
    }

    protected void multiLang(LanguageProvider provider, String key, String... values) {
        for (var i = 0; i < values.length; i++) {
            provider.add(getSubKey(key, i), values[i]);
        }
    }

    protected void multilineLang(LanguageProvider provider, String key, String multiline) {
        var lines = multiline.split("\n");
        multiLang(provider, key, lines);
    }

    public void addCN(String key, String cnText) {
        simplifiedChinese.add(key, cnText);
    }

    public void addItemName(NonNullSupplier<? extends Item> item, String enName, String cnName) {
        simplifiedChinese.addItem(item, cnName);
        addItem(item, enName);
    }

    public void addItemName(NonNullSupplier<? extends Item> item, String cnName) {
        simplifiedChinese.addItem(item, cnName);
    }

    public void addItemWithTooltip(
            NonNullSupplier<? extends Item> item,
            String enName,
            String cnName,
            String enTooltip,
            String cnTooltip) {
        simplifiedChinese.addItemWithTooltip(item, cnName, cnTooltip);
        addItemWithTooltip(item, enName, enTooltip);
    }

    public void addItemWithTooltip(
            NonNullSupplier<? extends Item> item, String cnName, String enTooltip, String cnTooltip) {
        simplifiedChinese.addItemWithTooltip(item, cnName, cnTooltip);
        addTooltip(item, enTooltip);
    }

    public void addItemWithTooltip(
            NonNullSupplier<? extends Item> item,
            String enName,
            String cnName,
            List<String> enTooltip,
            List<String> cnTooltip) {
        simplifiedChinese.addItemWithTooltip(item, cnName, cnTooltip);
        addItemWithTooltip(item, enName, enTooltip);
    }

    public void addItemWithTooltip(
            NonNullSupplier<? extends Item> item,
            String cnName,
            List<String> enTooltip,
            List<String> cnTooltip) {
        simplifiedChinese.addItemWithTooltip(item, cnName, cnTooltip);
        addTooltip(item, enTooltip);
    }

    public void addBlockName(NonNullSupplier<? extends Block> block, String enName, String cnName) {
        simplifiedChinese.addBlock(block, cnName);
        addBlock(block, enName);
    }

    public void addBlockName(NonNullSupplier<? extends Block> block, String cnName) {
        simplifiedChinese.addBlock(block, cnName);
    }

    public void addBlockWithTooltip(
            NonNullSupplier<? extends Block> block,
            String enName,
            String cnName,
            String enTooltip,
            String cnTooltip) {
        simplifiedChinese.addBlockWithTooltip(block, cnName, cnTooltip);
        addBlockWithTooltip(block, enName, enTooltip);
    }

    public void addBlockWithTooltip(
            NonNullSupplier<? extends Block> block, String cnName, String enTooltip, String cnTooltip) {
        simplifiedChinese.addBlockWithTooltip(block, cnName, cnTooltip);
        addTooltip(block, enTooltip);
    }

    public void addBlockWithTooltip(
            NonNullSupplier<? extends Block> block,
            String enName,
            String cnName,
            List<String> enTooltip,
            List<String> cnTooltip) {
        simplifiedChinese.addBlockWithTooltip(block, cnName, cnTooltip);
        addBlockWithTooltip(block, enName, enTooltip);
    }

    public void addBlockWithTooltip(
            NonNullSupplier<? extends Block> block,
            String cnName,
            List<String> enTooltip,
            List<String> cnTooltip) {
        simplifiedChinese.addBlockWithTooltip(block, cnName, cnTooltip);
        addTooltip(block, enTooltip);
    }

    public void addBlockWithTooltip(String blockName, String enTooltip, String cnTooltip) {
        simplifiedChinese.add(getBlockKey(modid, blockName) + ".desc", cnTooltip);
        add(getBlockKey(modid, blockName) + ".desc", enTooltip);
    }

    public void addBlockWithTooltip(
            String blockName, List<String> enTooltip, List<String> cnTooltip) {
        for (int i = 0; i < cnTooltip.size(); i++) {
            simplifiedChinese.add(getBlockKey(modid, blockName) + ".desc." + i, cnTooltip.get(i));
            add(getBlockKey(modid, blockName) + ".desc." + i, enTooltip.get(i));
        }
    }

    public void addTieredBlockName(
            Function<Integer, String> keyGetter,
            Function<Integer, String> enNameGetter,
            Function<Integer, String> cnNameGetter,
            int... tiers) {
        for (int tier : tiers) {
            var name = getBlockKey(modid, keyGetter.apply(tier));
            simplifiedChinese.add(name, cnNameGetter.apply(tier));
            add(name, enNameGetter.apply(tier));
        }
    }

    public void addTieredBlockWithTooltip(
            Function<Integer, String> keyGetter,
            Function<Integer, String> enNameGetter,
            Function<Integer, String> cnNameGetter,
            Function<Integer, String> enTooltipGetter,
            Function<Integer, String> cnTooltipGetter,
            int... tiers) {
        for (int tier : tiers) {
            var name = getBlockKey(modid, keyGetter.apply(tier));
            simplifiedChinese.add(name, cnNameGetter.apply(tier));
            simplifiedChinese.add(name + ".desc", cnTooltipGetter.apply(tier));
            add(name, enNameGetter.apply(tier));
            add(name + ".desc", enTooltipGetter.apply(tier));
        }
    }

    public void addTieredMachineName(
            String key, Function<Integer, String> cnNameGetter, int... tiers) {
        addTieredMachineName(
                tier -> GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + key, cnNameGetter, tiers);
    }

    public void addTieredMachineName(
            Function<Integer, String> keyGetter, Function<Integer, String> cnNameGetter, int... tiers) {
        for (int tier : tiers) {
            simplifiedChinese.add(getBlockKey(modid, keyGetter.apply(tier)), cnNameGetter.apply(tier));
        }
    }

    public void addTieredMachineName(String key, String cnName, int... tiers) {
        for (int tier : tiers) {
            simplifiedChinese.add(
                    getBlockKey(modid, GTValues.VN[tier].toLowerCase(Locale.ROOT) + "_" + key),
                    "%s§r%s".formatted(GTValues.VNF[tier], cnName));
        }
    }

    public void addTooltip(
            NonNullSupplier<? extends ItemLike> item, List<String> enTooltip, List<String> cnTooltip) {
        simplifiedChinese.addTooltip(item, cnTooltip);
        addTooltip(item, enTooltip);
    }

    public void addShiftTooltip(
            NonNullSupplier<? extends ItemLike> item, List<String> enTooltip, List<String> cnTooltip) {
        for (int i = 0; i < cnTooltip.size(); i++) {
            simplifiedChinese.add(
                    item.get().asItem().getDescriptionId() + ".shift_desc." + i, cnTooltip.get(i));
        }

        for (int i = 0; i < enTooltip.size(); i++) {
            add(item.get().asItem().getDescriptionId() + ".shift_desc." + i, enTooltip.get(i));
        }
    }

    public void addCtrlTooltip(
            NonNullSupplier<? extends ItemLike> item, List<String> enTooltip, List<String> cnTooltip) {
        for (int i = 0; i < cnTooltip.size(); i++) {
            simplifiedChinese.add(
                    item.get().asItem().getDescriptionId() + ".ctrl_desc." + i, cnTooltip.get(i));
        }

        for (int i = 0; i < enTooltip.size(); i++) {
            add(item.get().asItem().getDescriptionId() + ".ctrl_desc." + i, enTooltip.get(i));
        }
    }

    public void addAltTooltip(
            NonNullSupplier<? extends ItemLike> item, List<String> enTooltip, List<String> cnTooltip) {
        for (int i = 0; i < cnTooltip.size(); i++) {
            simplifiedChinese.add(
                    item.get().asItem().getDescriptionId() + ".alt_desc." + i, cnTooltip.get(i));
        }

        for (int i = 0; i < enTooltip.size(); i++) {
            add(item.get().asItem().getDescriptionId() + ".alt_desc." + i, enTooltip.get(i));
        }
    }

    public void addRecipeType(GTRecipeType recipeType, String enName, String cnName) {
        simplifiedChinese.add(recipeType.registryName.toLanguageKey(), cnName);
        add(recipeType.registryName.toLanguageKey(), enName);
    }

    public void addMaterial(Material material, String enName, String cnName) {
        simplifiedChinese.add(material.getUnlocalizedName(), cnName);
        add(material.getUnlocalizedName(), enName);
    }

    public void addTagPrefix(TagPrefix tagPrefix, String enName, String cnName) {
        simplifiedChinese.add(tagPrefix.getUnlocalizedName(), cnName);
        add(tagPrefix.getUnlocalizedName(), enName);
    }
}
