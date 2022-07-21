package mod.vemerion.livingitems;

import mod.vemerion.livingitems.init.ModEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "livingitems";

	public Main() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModEntities.ENTITIES.register(bus);
	}
}
