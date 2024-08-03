package com.epimorphismmc.monomorphism.data.chemical;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper.UNIFICATION_ENTRY_ITEM;
import static com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper.getTags;

@Deprecated(since = "gtm@1.3.2", forRemoval = true) // TODO
public class MOChemicalHelper {

    public static final Map<TagPrefix, ArrayList<Supplier<? extends ItemLike>>> TAG_PREFIX_ITEM =
            new ConcurrentHashMap<>();

    public static List<ItemLike> getItems(TagPrefix tagPrefix) {
        return TAG_PREFIX_ITEM
                .computeIfAbsent(tagPrefix, prefix -> {
                    var items = new ArrayList<Supplier<? extends ItemLike>>();
                    for (TagKey<Item> tag : prefix.getItemParentTags()) {
                        for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                            items.add(itemHolder::value);
                        }
                    }
                    if (items.isEmpty()) {
                        var condition = prefix.generationCondition();
                        for (MaterialRegistry registry : GTCEuAPI.materialManager.getRegistries()) {
                            for (Material material : registry.getAllMaterials()) {
                                if (condition == null || condition.test(material)) {
                                    items.addAll(getSuppliers(new UnificationEntry(prefix, material)));
                                }
                            }
                        }
                    }
                    return items;
                })
                .stream()
                .map(Supplier::get)
                .collect(Collectors.toList());
    }

    public static List<Supplier<? extends ItemLike>> getSuppliers(UnificationEntry unificationEntry) {
        return UNIFICATION_ENTRY_ITEM.computeIfAbsent(unificationEntry, entry -> {
            var items = new ArrayList<Supplier<? extends ItemLike>>();
            for (TagKey<Item> tag : getTags(entry.tagPrefix, entry.material)) {
                for (Holder<Item> itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
                    items.add(itemHolder::value);
                }
            }
            TagPrefix prefix = entry.tagPrefix;
            if (items.isEmpty() && prefix.hasItemTable() && prefix.doGenerateItem(entry.material)) {
                return new ArrayList<>(List.of(prefix.getItemFromTable(entry.material)));
            }
            return items;
        });
    }
}
