package mod.vemerion.livingitems.init;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.block.ItemAwakenerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);

	public static final RegistryObject<Block> ITEM_AWAKENER = BLOCKS.register("item_awakener",
			() -> new ItemAwakenerBlock(BlockBehaviour.Properties.of(Material.SCULK, MaterialColor.COLOR_BLACK)
					.isViewBlocking((s, g, p) -> false).strength(1, 3).sound(SoundType.SCULK_SHRIEKER))); // TODO: Crafting recipe
}
