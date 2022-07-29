package mod.vemerion.livingitems.menu;

import org.lwjgl.glfw.GLFW;

import mod.vemerion.livingitems.blockentity.ItemAwakenerBlockEntity;
import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModMenus;
import mod.vemerion.livingitems.network.ItemAwakenerMessage;
import mod.vemerion.livingitems.network.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ItemAwakenerMenu extends AbstractContainerMenu {

	public static final int SLOT_SIZE = 18;
	private static final int INVENTORY_LEFT_OFFSET = 8;
	private static final int INVENTORY_TOP_OFFSET = 140;
	private static final int HOTBAR_TOP_OFFSET = 198;
	public static final int FILTER_LENGTH = 5;
	public static final int FILTER_LEFT_OFFSET = 80;
	public static final int FILTER_TOP_OFFSET = 36;

	private ItemStackHandler filter;
	private final ContainerLevelAccess access;
	private ItemAwakenerBlockEntity.Data data;
	private BlockPos pos;

	public ItemAwakenerMenu(int id, Inventory playerInv, FriendlyByteBuf buffer) {
		this(id, playerInv, new ItemStackHandler(ItemAwakenerBlockEntity.FILTER_SIZE), ContainerLevelAccess.NULL,
				new ItemAwakenerBlockEntity.Data(buffer), buffer.readBlockPos());
	}

	public ItemAwakenerMenu(int containerId, Inventory playerInv, ItemStackHandler filter, ContainerLevelAccess access,
			ItemAwakenerBlockEntity.Data data, BlockPos pos) {
		super(ModMenus.ITEM_AWAKENER_MENU.get(), containerId);
		this.filter = filter;
		this.access = access;
		this.data = data;
		this.pos = pos;

		// filter
		for (int y = 0; y < FILTER_LENGTH; y++) {
			for (int x = 0; x < FILTER_LENGTH; x++) {
				this.addSlot(new FilterSlot(filter, x + y * FILTER_LENGTH, FILTER_LEFT_OFFSET + x * SLOT_SIZE,
						FILTER_TOP_OFFSET + y * SLOT_SIZE));
			}
		}

		// Player inv
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInv, x + y * 9 + Inventory.getSelectionSize(),
						INVENTORY_LEFT_OFFSET + x * SLOT_SIZE, INVENTORY_TOP_OFFSET + y * SLOT_SIZE));
			}
		}

		// Player hotbar
		for (int x = 0; x < Inventory.getSelectionSize(); x++)
			this.addSlot(new Slot(playerInv, x, INVENTORY_LEFT_OFFSET + x * SLOT_SIZE, HOTBAR_TOP_OFFSET));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		Slot slot = getSlot(index);
		if (slot != null && slot.hasItem() && index > ItemAwakenerBlockEntity.FILTER_SIZE)
			moveItemStackTo(slot.getItem().copy(), 0, ItemAwakenerBlockEntity.FILTER_SIZE, false);

		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(access, pPlayer, ModBlocks.ITEM_AWAKENER.get());
	}

	public void toggleDenylist() {
		data.isDenylistEnabled = !data.isDenylistEnabled;
	}

	public boolean isDenylistEnabled() {
		return data.isDenylistEnabled;
	}

	public void toggleSender() {
		data.isSender = !data.isSender;
	}

	public boolean isSender() {
		return data.isSender;
	}

	public void setlinkId(int linkId) {
		data.linkId = linkId;
	}

	public int getlinkId() {
		return data.linkId;
	}

	@Override
	public void clicked(int index, int button, ClickType clickType, Player player) {
		var slot = index >= 0 && index < slots.size() ? getSlot(index) : null;
		if (slot instanceof FilterSlot) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
				slot.set(ItemStack.EMPTY);
			} else {
				var stack = getCarried().copy();
				stack.setCount(1);
				slot.set(getCarried().isEmpty() ? ItemStack.EMPTY : stack);
			}
			return;
		}
		super.clicked(index, button, clickType, player);
	}

	private class FilterSlot extends SlotItemHandler {

		private FilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
			super(itemHandler, index, xPosition, yPosition);
		}

		@Override
		public boolean isActive() {
			return ItemAwakenerMenu.this.isSender();
		}
	}

	// Sync values from client to server
	public void sendMessage() {
		Network.INSTANCE.sendToServer(new ItemAwakenerMessage(data, pos));
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack p_38908_, Slot p_38909_) {
		return false;
	}

}
