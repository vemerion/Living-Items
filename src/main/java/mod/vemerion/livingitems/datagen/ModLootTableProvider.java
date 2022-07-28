package mod.vemerion.livingitems.datagen;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import mod.vemerion.livingitems.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class ModLootTableProvider extends LootTableProvider {

	public ModLootTableProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
		return ImmutableList.of(Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
	}

	private static class ModBlockLoot extends BlockLoot {
		@Override
		protected Iterable<Block> getKnownBlocks() {
			return ModBlocks.BLOCKS.getEntries().parallelStream().map(r -> r.get()).toList();
		}

		@Override
		protected void addTables() {
			dropSelf(ModBlocks.ITEM_AWAKENER.get());
		}
	}
}
