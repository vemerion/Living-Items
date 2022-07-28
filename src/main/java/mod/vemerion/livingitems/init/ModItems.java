package mod.vemerion.livingitems.init;

import mod.vemerion.livingitems.Main;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

	public static final CreativeModeTab ITEM_TAB = new CreativeModeTab(Main.MODID) {

		@Override
		public ItemStack makeIcon() {
			return ITEM_AWAKENER.get().getDefaultInstance();
		}

	};

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

	public static final RegistryObject<Item> ITEM_AWAKENER = ITEMS.register("item_awakener",
			() -> new BlockItem(ModBlocks.ITEM_AWAKENER.get(), new Item.Properties().tab(ITEM_TAB)));

}
