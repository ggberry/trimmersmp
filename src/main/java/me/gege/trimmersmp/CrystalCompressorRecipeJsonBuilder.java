package me.gege.trimmersmp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class CrystalCompressorRecipeJsonBuilder extends RecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Item output;
    private final int count;
    private final List<Ingredient> inputs = Lists.newArrayList();
    private final List<Ingredient> fuel = Lists.newArrayList();
    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    @Nullable
    private String group;

    public CrystalCompressorRecipeJsonBuilder(RecipeCategory category, ItemConvertible output, int count) {
        this.category = category;
        this.output = output.asItem();
        this.count = count;
    }

    public static CrystalCompressorRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output) {
        return new CrystalCompressorRecipeJsonBuilder(category, output, 1);
    }

    public static CrystalCompressorRecipeJsonBuilder create(RecipeCategory category, ItemConvertible output, int count) {
        return new CrystalCompressorRecipeJsonBuilder(category, output, count);
    }

    public CrystalCompressorRecipeJsonBuilder input(TagKey<Item> tag) {
        return this.input(Ingredient.fromTag(tag));
    }

    public CrystalCompressorRecipeJsonBuilder input(ItemConvertible itemProvider) {
        return this.input((ItemConvertible)itemProvider, 1);
    }

    public CrystalCompressorRecipeJsonBuilder input(ItemConvertible itemProvider, int size) {
        for(int i = 0; i < size; ++i) {
            this.input(Ingredient.ofItems(new ItemConvertible[]{itemProvider}));
        }

        return this;
    }

    public CrystalCompressorRecipeJsonBuilder input(Ingredient ingredient) {
        return this.input((Ingredient)ingredient, 1);
    }

    public CrystalCompressorRecipeJsonBuilder input(Ingredient ingredient, int size) {
        for(int i = 0; i < size; ++i) {
            this.inputs.add(ingredient);
        }

        return this;
    }

    public CrystalCompressorRecipeJsonBuilder fuel(Ingredient ingredient) {
        return this.fuel((Ingredient)ingredient, 1);
    }

    public CrystalCompressorRecipeJsonBuilder fuel(Ingredient ingredient, int size) {
        for(int i = 0; i < size; ++i) {
            this.fuel.add(ingredient);
        }

        return this;
    }

    public CrystalCompressorRecipeJsonBuilder criterion(String string, CriterionConditions criterionConditions) {
        this.advancementBuilder.criterion(string, criterionConditions);
        return this;
    }

    public CrystalCompressorRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public Item getOutputItem() {
        return this.output;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.advancementBuilder.parent(ROOT).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(net.minecraft.advancement.AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
        exporter.accept(new CrystalCompressorRecipeJsonBuilder.CrystalCompressorRecipeJsonProvider(recipeId, this.output, this.count, this.group == null ? "" : this.group, getCraftingCategory(this.category), this.fuel, this.inputs, this.advancementBuilder, recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(Identifier recipeId) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }

    public static class CrystalCompressorRecipeJsonProvider extends RecipeJsonBuilder.CraftingRecipeJsonProvider {
        private final Identifier recipeId;
        private final Item output;
        private final int count;
        private final String group;
        private final List<Ingredient> inputs;
        private final Ingredient fuel;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;

        public CrystalCompressorRecipeJsonProvider(Identifier recipeId, Item output, int outputCount, String group, CraftingRecipeCategory craftingCategory, List<Ingredient> inputs, List<Ingredient> fuel, Advancement.Builder advancementBuilder, Identifier advancementId) {
            super(craftingCategory);
            this.recipeId = recipeId;
            this.output = output;
            this.count = outputCount;
            this.group = group;
            this.inputs = inputs;
            this.fuel = fuel.get(0);
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
        }

        public void serialize(JsonObject json) {
            json.addProperty("type", "trimmersmp:crystal_compressor");

            // Hey, tell my why in the world would a function called serialize just add the property 'category' huh? This is so nonsensical.
//            super.serialize(json);

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();
            Iterator var3 = this.inputs.iterator();

            while(var3.hasNext()) {
                Ingredient ingredient = (Ingredient)var3.next();
                jsonArray.add(ingredient.toJson());
            }

            json.add("ingredients", jsonArray);
            json.add("fuel", fuel.toJson());

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.count > 1) {
                jsonObject.addProperty("count", this.count);
            }

            json.add("output", jsonObject);
        }

        public RecipeSerializer<?> getSerializer() {
            return RecipeSerializer.SHAPELESS;
        }

        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Nullable
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Nullable
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
