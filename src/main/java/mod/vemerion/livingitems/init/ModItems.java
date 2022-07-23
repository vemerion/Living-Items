package mod.vemerion.livingitems.init;

import mod.vemerion.livingitems.Main;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

	public static final RegistryObject<Item> ITEM_AWAKENER = ITEMS.register("item_awakener",
			() -> new BlockItem(ModBlocks.ITEM_AWAKENER.get(), new Item.Properties()));

}
