package mod.vemerion.livingitems.event;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.init.ModEntities;
import mod.vemerion.livingitems.init.ModMenus;
import mod.vemerion.livingitems.renderer.LivingItemRenderer;
import mod.vemerion.livingitems.screen.ItemAwakenerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID, value = Dist.CLIENT)
public class ClientModEventSubscriber {
	@SubscribeEvent
	public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.LIVING_ITEM.get(), LivingItemRenderer::new);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(ModMenus.ITEM_AWAKENER_MENU.get(), ItemAwakenerScreen::new);
		});
	}
}
