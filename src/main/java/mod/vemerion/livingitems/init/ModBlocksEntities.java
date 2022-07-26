package mod.vemerion.livingitems.init;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.blockentity.ItemAwakenerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocksEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCKS_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITIES, Main.MODID);

	public static final RegistryObject<BlockEntityType<ItemAwakenerBlockEntity>> ITEM_AWAKENER = BLOCKS_ENTITIES
			.register("item_awakener", () -> BlockEntityType.Builder
					.of(ItemAwakenerBlockEntity::new, ModBlocks.ITEM_AWAKENER.get()).build(null));
}
