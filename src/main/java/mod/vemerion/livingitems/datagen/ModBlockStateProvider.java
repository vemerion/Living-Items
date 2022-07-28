package mod.vemerion.livingitems.datagen;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Main.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		var itemAwakenerModel = models()
				.cubeAll(ModBlocks.ITEM_AWAKENER.getId().getPath(),
						modLoc(ModelProvider.BLOCK_FOLDER + "/" + ModBlocks.ITEM_AWAKENER.getId().getPath()))
				.renderType("cutout");
		directionalBlock(ModBlocks.ITEM_AWAKENER.get(), itemAwakenerModel);
		simpleBlockItem(ModBlocks.ITEM_AWAKENER.get(), itemAwakenerModel);
	}

}
