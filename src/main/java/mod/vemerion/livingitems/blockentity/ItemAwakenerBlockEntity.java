package mod.vemerion.livingitems.blockentity;

import mod.vemerion.livingitems.capability.ItemAwakenerReceivers;
import mod.vemerion.livingitems.entity.LivingItemEntity;
import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModBlocksEntities;
import mod.vemerion.livingitems.init.ModEntities;
import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemAwakenerBlockEntity extends BlockEntity implements MenuProvider {

	public static final int FILTER_SIZE = 25;
	private static final int COOLDOWN = 20;

	private ItemStackHandler filter = new ItemStackHandler(FILTER_SIZE) {
		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}

		@Override
		protected void onContentsChanged(int slot) {
			setChanged();
		}
	};

	private Data data = new Data();
	private int cooldown = COOLDOWN;
	private int slot;

	public ItemAwakenerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModBlocksEntities.ITEM_AWAKENER.get(), pWorldPosition, pBlockState);
	}

	public void tick() {
		if (!hasLevel() || level.isClientSide || data.linkId == -1 || cooldown-- != 0)
			return;

		cooldown = COOLDOWN;

		if (data.isSender) {
			getStorage().ifPresent(c -> {
				ItemAwakenerReceivers.getCap(level).ifPresent(receivers -> {
					var receiver = receivers.get(data.linkId);
					if (receiver == null)
						return;

					if (slot >= c.getSlots())
						slot = 0;

					var stack = c.extractItem(slot, 64, true);

					if (!stack.isEmpty() && match(stack)) {

						stack = c.extractItem(slot, 64, false);

						var livingItem = new LivingItemEntity(ModEntities.LIVING_ITEM.get(), level, stack, receiver);
						livingItem.finalizeSpawn((ServerLevelAccessor) level,
								level.getCurrentDifficultyAt(worldPosition), MobSpawnType.MOB_SUMMONED, null, null);
						livingItem.setPos(Vec3.atCenterOf(worldPosition));
						level.addFreshEntity(livingItem);
					}
					slot++;
				});
			});
		} else {
			getStorage().ifPresent(c -> {
				for (var e : level.getEntitiesOfClass(LivingItemEntity.class,
						new AABB(Vec3.atCenterOf(worldPosition).subtract(1, 1, 1),
								Vec3.atCenterOf(worldPosition).add(1, 1, 1)),
						e -> e.getReceiver().equals(worldPosition))) {
					ItemHandlerHelper.insertItem(c, e.getItemStack(), false);
					e.discard();
				}
			});
		}
	}

	private LazyOptional<IItemHandler> getStorage() {
		var storage = level.getBlockEntity(
				worldPosition.relative(getBlockState().getValue(BlockStateProperties.FACING).getOpposite()));
		if (storage != null)
			return storage.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		return LazyOptional.empty();
	}

	private boolean match(ItemStack stack) {
		var found = false;
		for (int i = 0; i < filter.getSlots(); i++) {
			if (filter.getStackInSlot(i).is(stack.getItem())) {
				found = true;
				break;
			}
		}
		return found ^ data.isDenylistEnabled;
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("filter", filter.serializeNBT());
		pTag.put("itemAwakenerData", data.serializeNBT());
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if (pTag.contains("filter"))
			filter.deserializeNBT(pTag.getCompound("filter"));

		if (pTag.contains("itemAwakenerData"))
			data.deserializeNBT(pTag.getCompound("itemAwakenerData"));
	}

	@Override
	public void setLevel(Level pLevel) {
		super.setLevel(pLevel);
		if (!data.isSender && data.linkId != -1 && hasLevel())
			ItemAwakenerReceivers.getCap(level).ifPresent(c -> c.add(data.linkId, worldPosition));
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new ItemAwakenerMenu(pContainerId, pInventory, filter, ContainerLevelAccess.create(level, getBlockPos()),
				data, worldPosition);
	}

	@Override
	public Component getDisplayName() {
		return ModBlocks.ITEM_AWAKENER.get().getName();
	}

	public void openGui(ServerPlayer player) {
		NetworkHooks.openGui(player, this, buffer -> {
			data.writeToBuffer(buffer);
			buffer.writeBlockPos(worldPosition);
		});
	}

	public void receiveMessage(Data newData) {
		ItemAwakenerReceivers.getCap(level).ifPresent(c -> {
			// Should not override other item awakeners
			if (!newData.isSender && c.exists(newData.linkId))
				return;

			if (!this.data.isSender)
				c.remove(this.data.linkId);
			this.data = newData;
			if (!this.data.isSender)
				c.add(this.data.linkId, worldPosition);
		});

		setChanged();
	}

	public static class Data implements INBTSerializable<CompoundTag> {

		public boolean isDenylistEnabled = true;
		public boolean isSender = true;
		public int linkId = -1;

		public Data() {

		}

		public Data(FriendlyByteBuf buffer) {
			readFromBuffer(buffer);
		}

		public Data(boolean isDenylistEnabled, boolean isSender, int linkId) {
			this.isDenylistEnabled = isDenylistEnabled;
			this.isSender = isSender;
			this.linkId = linkId;
		}

		@Override
		public CompoundTag serializeNBT() {
			var tag = new CompoundTag();
			tag.putBoolean("isDenylistEnabled", isDenylistEnabled);
			tag.putBoolean("isSender", isSender);
			tag.putInt("linkId", linkId);
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			if (nbt.contains("isDenylistEnabled"))
				isDenylistEnabled = nbt.getBoolean("isDenylistEnabled");
			if (nbt.contains("isSender"))
				isSender = nbt.getBoolean("isSender");
			if (nbt.contains("linkId"))
				linkId = nbt.getInt("linkId");
		}

		public void writeToBuffer(FriendlyByteBuf buffer) {
			buffer.writeBoolean(isDenylistEnabled);
			buffer.writeBoolean(isSender);
			buffer.writeInt(linkId);
		}

		public void readFromBuffer(FriendlyByteBuf buffer) {
			isDenylistEnabled = buffer.readBoolean();
			isSender = buffer.readBoolean();
			linkId = buffer.readInt();
		}

	}

	public void onRemove() {
		if (!data.isSender)
			ItemAwakenerReceivers.getCap(level).ifPresent(c -> c.remove(data.linkId));
	}

}
