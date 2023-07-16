package me.gege.trimmersmp.item;

import me.gege.trimmersmp.TrimmerSmp;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.item.trim.ArmorTrimPatterns;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item HARNESS_CRYSTAL = registerItem("harness_crystal", new Item(
            new FabricItemSettings()
                    .maxCount(4)
                    .fireproof()
    ));

    public static final Item ROUGH_HARNESS_CRYSTAL = registerItem("rough_harness_crystal", new Item(
            new FabricItemSettings()
                    .maxCount(4)
                    .fireproof()
    ));

    // Trims

    public static final Item HARNESSED_WILD_ARMOR_TRIM = (Item) registerItem("harnessed_wild_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.WILD));
    public static final Item HARNESSED_VEX_ARMOR_TRIM = (Item) registerItem("harnessed_vex_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.VEX));
    public static final Item HARNESSED_RIB_ARMOR_TRIM = (Item) registerItem("harnessed_rib_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.RIB));
    public static final Item HARNESSED_SNOUT_ARMOR_TRIM = (Item) registerItem("harnessed_snout_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.SNOUT));
    public static final Item HARNESSED_EYE_ARMOR_TRIM = (Item) registerItem("harnessed_eye_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.EYE));
    public static final Item HARNESSED_SILENCE_ARMOR_TRIM = (Item) registerItem("harnessed_silence_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.SILENCE));
    public static final Item HARNESSED_RAISER_ARMOR_TRIM = (Item) registerItem("harnessed_raiser_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.RAISER));
    public static final Item HARNESSED_SHAPER_ARMOR_TRIM = (Item) registerItem("harnessed_shaper_armor_trim",  SmithingTemplateItem.of(ArmorTrimPatterns.SHAPER));

    // Group
    public static final RegistryKey<ItemGroup> TRIMMERSMP_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(TrimmerSmp.MOD_ID, "gegemod"));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TrimmerSmp.MOD_ID, name), item);
    }

    public static void registerModGroup() {
        Registry.register(Registries.ITEM_GROUP, TRIMMERSMP_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE))
                .displayName(Text.translatable("trimmersmp.group.trimmersmpitems"))
                .build());
    }



    public static void registerModItems() {
        TrimmerSmp.LOGGER.info("Registering Mod Items for " + TrimmerSmp.MOD_ID);
    }
}
