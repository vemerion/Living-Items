package mod.vemerion.livingitems.datagen;

import java.util.function.Consumer;

import mod.vemerion.livingitems.init.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ModItems.ITEM_AWAKENER.get()).define('#', Items.SCULK)
				.define('X', Items.SCULK_SHRIEKER).pattern("###").pattern("#X#").pattern("###")
				.unlockedBy("has_sculk_or_shrieker",
						inventoryTrigger(ItemPredicate.Builder.item().of(Items.SCULK, Items.SCULK_SHRIEKER).build()))
				.save(consumer);
	}

}
