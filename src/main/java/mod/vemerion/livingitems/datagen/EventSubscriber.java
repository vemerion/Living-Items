package mod.vemerion.livingitems.datagen;

import mod.vemerion.livingitems.Main;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class EventSubscriber {

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(event.includeClient(), new ModLanguageProvider(generator));
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModBlockTagsProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(generator));
	}
}