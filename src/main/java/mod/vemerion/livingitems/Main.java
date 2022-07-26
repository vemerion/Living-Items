package mod.vemerion.livingitems;

import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModBlocksEntities;
import mod.vemerion.livingitems.init.ModEntities;
import mod.vemerion.livingitems.init.ModItems;
import mod.vemerion.livingitems.init.ModMenus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "livingitems";

	public Main() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModEntities.ENTITIES.register(bus);
		ModBlocks.BLOCKS.register(bus);
		ModItems.ITEMS.register(bus);
		ModMenus.MENUS.register(bus);
		ModBlocksEntities.BLOCKS_ENTITIES.register(bus);
	}

	public static String guiTranslationKey(String suffix) {
		return "gui." + MODID + "." + suffix;
	}
}
