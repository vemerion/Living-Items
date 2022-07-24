package mod.vemerion.livingitems.menu;

import org.lwjgl.glfw.GLFW;

import mod.vemerion.livingitems.init.ModBlocks;
import mod.vemerion.livingitems.init.ModMenus;
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

	private ItemStackHandler filter = new ItemAwakenerStackHandler(FILTER_LENGTH * FILTER_LENGTH);
	private final ContainerLevelAccess access;
	private boolean denylistEnabled = true;
	private boolean sender = true;
	private int id = -1;

	public ItemAwakenerMenu(int id, Inventory playerInv) {
		this(id, playerInv, ContainerLevelAccess.NULL);
	}

	public ItemAwakenerMenu(int id, Inventory playerInv, ContainerLevelAccess access) {
		super(ModMenus.ITEM_AWAKENER_MENU.get(), id);
		this.access = access;

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
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		return ItemStack.EMPTY; // TODO
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		return stillValid(access, pPlayer, ModBlocks.ITEM_AWAKENER.get());
	}

	public void toggleDenylist() {
		denylistEnabled = !denylistEnabled;
	}

	public boolean isDenylistEnabled() {
		return denylistEnabled;
	}

	public void toggleSender() {
		sender = !sender;
	}

	public boolean isSender() {
		return sender;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
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

	private static class ItemAwakenerStackHandler extends ItemStackHandler {

		private ItemAwakenerStackHandler(int size) {
			super(size);
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
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

}
