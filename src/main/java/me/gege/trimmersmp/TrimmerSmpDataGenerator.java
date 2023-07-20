package me.gege.trimmersmp;

import me.gege.trimmersmp.block.ModBlocks;
import me.gege.trimmersmp.item.ModItems;
import me.gege.trimmersmp.recipe.ModRecipies;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TrimmerSmpDataGenerator implements DataGeneratorEntrypoint {
	private static class MyRecipeGenerator extends FabricRecipeProvider {
		private MyRecipeGenerator(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CRYSTAL_COMPRESSOR)
					.pattern("## ")
					.pattern("#H ")
					.pattern("___")
					.input('#', Ingredient.ofItems(Items.STONE))
					.input('H', Ingredient.ofItems(Items.HEAVY_WEIGHTED_PRESSURE_PLATE))
					.input('_', Ingredient.ofItems(Items.OAK_SLAB))
					.criterion(hasItem(Items.STONE), conditionsFromItem(Items.STONE))
					.offerTo(exporter);

			CrystalCompressorRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HARNESS_CRYSTAL)
					.input(Ingredient.ofItems(Items.PITCHER_PLANT))
					.fuel(Ingredient.ofItems(Items.TURTLE_HELMET))
					.criterion(hasItem(Items.PITCHER_PLANT), conditionsFromItem(Items.PITCHER_PLANT))
					.criterion(hasItem(Items.TURTLE_HELMET), conditionsFromItem(Items.TURTLE_HELMET))
					.offerTo(exporter);

		}
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(MyRecipeGenerator::new);
	}
}


