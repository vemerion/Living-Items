package mod.vemerion.livingitems.blockentity;

import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModBlocksEntities;
import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

public class ItemAwakenerBlockEntity extends BlockEntity implements MenuProvider {

	public static final int FILTER_SIZE = 25;

	private ItemStackHandler filter = new StackHandler();

	public ItemAwakenerBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
		super(ModBlocksEntities.ITEM_AWAKENER.get(), pWorldPosition, pBlockState);
	}

	@Override
	protected void saveAdditional(CompoundTag pTag) {
		super.saveAdditional(pTag);
		pTag.put("filter", filter.serializeNBT());
	}

	@Override
	public void load(CompoundTag pTag) {
		super.load(pTag);
		if (pTag.contains("filter"))
			filter.deserializeNBT(pTag.getCompound("filter"));
	}

	@Override
	public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
		return new ItemAwakenerMenu(pContainerId, pInventory, filter,
				ContainerLevelAccess.create(level, getBlockPos()));
	}

	@Override
	public Component getDisplayName() {
		return ModBlocks.ITEM_AWAKENER.get().getName();
	}

	public static class StackHandler extends ItemStackHandler {

		public StackHandler() {
			super(FILTER_SIZE);
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	}

}
