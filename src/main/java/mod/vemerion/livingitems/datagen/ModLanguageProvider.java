package mod.vemerion.livingitems.datagen;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModEntities;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(DataGenerator gen) {
		super(gen, Main.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add(ModEntities.LIVING_ITEM.get(), "Living Item");
		add(ModBlocks.ITEM_AWAKENER.get(), "Item Awakener");
	}
}
