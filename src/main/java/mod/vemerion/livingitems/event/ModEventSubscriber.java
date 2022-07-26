package mod.vemerion.livingitems.event;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.network.ItemAwakenerMessage;
import mod.vemerion.livingitems.network.Network;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		Network.INSTANCE.registerMessage(0, ItemAwakenerMessage.class, ItemAwakenerMessage::encode,
				ItemAwakenerMessage::decode, ItemAwakenerMessage::handle);

	}
}
