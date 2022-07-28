package mod.vemerion.livingitems.datagen;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Main.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(BlockTags.MINEABLE_WITH_HOE).add(ModBlocks.ITEM_AWAKENER.get());
	}
}
