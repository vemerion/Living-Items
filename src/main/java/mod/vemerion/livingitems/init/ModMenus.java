package mod.vemerion.livingitems.init;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
	public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES,
			Main.MODID);

	public static final RegistryObject<MenuType<ItemAwakenerMenu>> ITEM_AWAKENER_MENU = MENUS.register("item_awakener",
			() -> new MenuType<>(new IContainerFactory<ItemAwakenerMenu>() {
				@Override
				public ItemAwakenerMenu create(int windowId, Inventory inv, FriendlyByteBuf data) {
					return new ItemAwakenerMenu(windowId, inv, data);
				}
			}));
}
