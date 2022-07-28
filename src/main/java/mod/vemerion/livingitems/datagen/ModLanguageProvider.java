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

		addGui("denylist", "Denylist");
		addGui("allowlist", "Allowlist");
		addGui("sender", "Sender");
		addGui("receiver", "Receiver");
		addGui("id_box_default", "id...");
		
		addGui("help.id_box", "The id is used to link together receiver and sender Awakeners. One receiver can be linked to many senders.");
		addGui("help.sender_toggle", "Toggle if this Awakener should be receiver or sender. Sender extracts items from a container and receiver inserts into a container.");
		addGui("help.allowlist_toggle", "Toggle if this Awakener should have allowlist (only extract items in filter) or denylist (extract all items NOT in filter). Only used for Senders.");
		addGui("help.filter", "Item slots for the allowlist/denylist.");
	}

	void addGui(String suffix, String translation) {
		add(Main.guiTranslationKey(suffix), translation);
	}
}
