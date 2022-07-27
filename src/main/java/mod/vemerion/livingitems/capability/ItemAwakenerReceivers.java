package mod.vemerion.livingitems.capability;

import java.util.HashMap;
import java.util.Map;

import mod.vemerion.livingitems.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class ItemAwakenerReceivers {
	public static final Capability<ItemAwakenerReceivers> CAPABILITY = CapabilityManager
			.get(new CapabilityToken<ItemAwakenerReceivers>() {
			});

	private Map<Integer, BlockPos> receiverPositions = new HashMap<>();

	public ItemAwakenerReceivers() {

	}

	public void add(int id, BlockPos pos) {
		receiverPositions.put(id, pos);
	}

	public void remove(int id) {
		receiverPositions.remove(id);
	}

	public BlockPos get(int id) {
		return receiverPositions.get(id);
	}

	public boolean exists(int id) {
		return receiverPositions.containsKey(id);
	}

	public static LazyOptional<ItemAwakenerReceivers> getCap(Level level) {
		return level.getCapability(CAPABILITY);
	}

	@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
	public static class Provider implements ICapabilityProvider {

		private LazyOptional<ItemAwakenerReceivers> instance = LazyOptional.of(ItemAwakenerReceivers::new);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return CAPABILITY.orEmpty(cap, instance);
		}

		public static final ResourceLocation LOCATION = new ResourceLocation(Main.MODID, "itemawakenerreceivers");

		@SubscribeEvent
		public static void attachCapability(AttachCapabilitiesEvent<Level> event) {
			event.addCapability(LOCATION, new Provider());
		}
	}

}
