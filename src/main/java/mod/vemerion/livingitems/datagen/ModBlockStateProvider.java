package mod.vemerion.livingitems.datagen;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Main.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		var itemAwakenerId = ModBlocks.ITEM_AWAKENER.getId().getPath();

		var itemAwakenerModel = models().getBuilder(itemAwakenerId)
				.parent(models().getExistingFile(mcLoc(ModelProvider.BLOCK_FOLDER + "/block")))
				.texture("top", blockTexture(itemAwakenerId, "top"))
				.texture("spike", blockTexture(itemAwakenerId, "spike"))
				.texture("bottom", blockTexture(itemAwakenerId, "bottom"))
				.texture("side", blockTexture(itemAwakenerId, "side"))
				.texture("particle", blockTexture(itemAwakenerId, "top")).renderType("cutout").element()
				.from(1, 0, 1).to(15, 4, 15).face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#side").end()
				.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#side").end().face(Direction.SOUTH).uvs(0, 0, 16, 16)
				.texture("#side").end().face(Direction.WEST).uvs(0, 0, 16, 16).texture("#side").end().face(Direction.UP)
				.uvs(0, 0, 16, 16).texture("#top").end().face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").end()
				.end()
				// Outer spikes
				.element().from(2, 4, 2).to(14, 8, 14).face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#spike").end()
				.face(Direction.EAST).uvs(0, 0, 16, 16).texture("#spike").end().face(Direction.WEST).uvs(0, 0, 16, 16)
				.texture("#spike").end().face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#spike").end().end()
				// Inner spikes
				.element().from(2, 4, 13.98f).to(14, 8, 13.98f).face(Direction.NORTH).uvs(0, 0, 16, 16)
				.texture("#spike").end().end().element().from(2, 4, 2.02f).to(14, 8, 2.02f).face(Direction.SOUTH)
				.uvs(0, 0, 16, 16).texture("#spike").end().end().element().from(13.98f, 4, 2).to(13.98f, 8, 14)
				.face(Direction.WEST).uvs(0, 0, 16, 16).texture("#spike").end().end().element().from(2.02f, 4, 2)
				.to(2.02f, 8, 16).face(Direction.EAST).uvs(0, 0, 16, 16).texture("#spike").end().end();

		directionalBlock(ModBlocks.ITEM_AWAKENER.get(), itemAwakenerModel);
		simpleBlockItem(ModBlocks.ITEM_AWAKENER.get(), itemAwakenerModel);
	}

	private ResourceLocation blockTexture(String blockId, String suffix) {
		return modLoc(ModelProvider.BLOCK_FOLDER + "/" + blockId + "_" + suffix);
	}
}
