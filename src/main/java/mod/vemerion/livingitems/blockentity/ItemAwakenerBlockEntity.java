package mod.vemerion.livingitems.blockentity;

import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModBlocksEntities;
import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemAwakenerBlockEntity extends BlockEntity implements MenuProvider {

	public static final int FILTER_SIZE = 25;

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

	public ItemAwakenerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModBlocksEntities.ITEM_AWAKENER.get(), pWorldPosition, pBlockState);
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

	public void receiveMessage(Data data) {
		this.data = data;
		this.setChanged();
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

}
