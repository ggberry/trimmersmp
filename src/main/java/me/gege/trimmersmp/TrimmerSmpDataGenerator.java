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
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.COMPRESSOR)
					.pattern("   ")
					.pattern("# #")
					.pattern("___")
					.input('#', Ingredient.ofItems(Items.PISTON))
					.input('_', Ingredient.ofItems(Items.NETHERITE_BLOCK))
					.criterion(hasItem(Items.PISTON), conditionsFromItem(Items.PISTON))
					.criterion(hasItem(Items.NETHERITE_BLOCK), conditionsFromItem(Items.NETHERITE_BLOCK))
					.offerTo(exporter);

			CrystalCompressorRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.HARNESS_CRYSTAL)
					.input(Ingredient.ofItems(Items.END_CRYSTAL))
					.input(Ingredient.ofItems(ModItems.REFINED_SHARD))
					.time(12000)
					.criterion(hasItem(Items.END_CRYSTAL), conditionsFromItem(Items.END_CRYSTAL))
					.criterion(hasItem(ModItems.REFINED_SHARD), conditionsFromItem(ModItems.REFINED_SHARD))
					.offerTo(exporter);

		}
	}

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(MyRecipeGenerator::new);
	}
}


