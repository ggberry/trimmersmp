package me.gege.trimmersmp;

import me.gege.trimmersmp.block.ModBlocks;
import me.gege.trimmersmp.block.entity.ModBlockEntities;
import me.gege.trimmersmp.item.ModItems;
import me.gege.trimmersmp.recipe.ModRecipies;
import me.gege.trimmersmp.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrimmerSmp implements ModInitializer {
	public static final String MOD_ID = "trimmersmp";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItems.registerModGroup();

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerAllScreenHandlers();
		ModRecipies.registerRecipes();

		ItemGroupEvents.modifyEntriesEvent(ModItems.TRIMMERSMP_GROUP).register(itemGroup -> {
			itemGroup.add(ModItems.HARNESS_CRYSTAL);
			itemGroup.add(ModItems.ROOTED_SHARD);
			itemGroup.add(ModItems.REFINED_SHARD);

			itemGroup.add(ModItems.HARNESSED_WILD_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_VEX_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_RIB_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_SNOUT_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_EYE_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_SILENCE_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_RAISER_ARMOR_TRIM);
			itemGroup.add(ModItems.HARNESSED_SHAPER_ARMOR_TRIM);

			itemGroup.add(ModBlocks.CRYSTAL_COMPRESSOR);
		});

		LOGGER.info("Initialized Trimmer SMP plugin (1.20.1)");
	}
}