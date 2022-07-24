package mod.vemerion.livingitems.screen;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.livingitems.Main;
import mod.vemerion.livingitems.menu.ItemAwakenerMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ItemAwakenerScreen extends AbstractContainerScreen<ItemAwakenerMenu> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID,
			"textures/gui/item_awakener.png");

	private static final Component ID_BOX_DEFAULT = (new TranslatableComponent(
			Main.guiTranslationKey("id_box_default"))).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

	private EditBox idBox;

	public ItemAwakenerScreen(ItemAwakenerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
		super(pMenu, pPlayerInventory, pTitle);
		imageWidth = 176;
		imageHeight = 222;
		inventoryLabelY = imageHeight - 94;
	}

	@Override
	protected void init() {
		super.init();

		// Denylist/allowlist toggle
		var toggleDenylist = addRenderableWidget(new Button(leftPos + 124 - 70 / 2, topPos + 10, 70, 20,
				new TranslatableComponent(Main.guiTranslationKey(menu.isDenylistEnabled() ? "denylist" : "allowlist")),
				(b) -> {
					menu.toggleDenylist();
					b.setMessage(new TranslatableComponent(
							Main.guiTranslationKey(menu.isDenylistEnabled() ? "denylist" : "allowlist")));
				}));

		// Sender/Receiver toggle
		addRenderableWidget(new Button(leftPos + 40 - 60 / 2, topPos + 65, 60, 20,
				new TranslatableComponent(Main.guiTranslationKey(menu.isSender() ? "sender" : "receiver")), (b) -> {
					menu.toggleSender();
					toggleDenylist.visible = menu.isSender();
					b.setMessage(
							new TranslatableComponent(Main.guiTranslationKey(menu.isSender() ? "sender" : "receiver")));
				}));

		// Id text box
		idBox = new EditBox(minecraft.font, leftPos + 25, topPos + 40, 27, 9 + 5, ID_BOX_DEFAULT);
		idBox.setMaxLength(3);
		idBox.setFilter(s -> s != null && s.matches("^[0-9]*$"));
		var id = menu.getId();
		if (id != -1)
			idBox.setValue(String.valueOf(id));
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

		idBox.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		if (!idBox.isFocused() && idBox.getValue().isEmpty())
			drawString(pPoseStack, minecraft.font, ID_BOX_DEFAULT, idBox.x + 4, idBox.y + 4, 0xffffffff);
		RenderSystem.setShaderColor(1, 1, 1, 1);

		// Hide filter slots if item awakener is receiver
		if (!menu.isSender())
			fill(pPoseStack, leftPos + ItemAwakenerMenu.FILTER_LEFT_OFFSET - 1,
					topPos + ItemAwakenerMenu.FILTER_TOP_OFFSET - 1,
					leftPos + ItemAwakenerMenu.FILTER_LEFT_OFFSET
							+ ItemAwakenerMenu.FILTER_LENGTH * ItemAwakenerMenu.SLOT_SIZE,
					topPos + ItemAwakenerMenu.FILTER_TOP_OFFSET
							+ ItemAwakenerMenu.FILTER_LENGTH * ItemAwakenerMenu.SLOT_SIZE,
					0xffc6c6c6);
	}

	@Override
	protected void containerTick() {
		idBox.tick();
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (idBox.mouseClicked(pMouseX, pMouseY, pButton))
			return true;
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (idBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			if (!idBox.getValue().isEmpty())
				menu.setId(Integer.valueOf(idBox.getValue()));
			return true;
		} else if (idBox.isFocused() && idBox.isVisible() && pKeyCode != GLFW.GLFW_KEY_ESCAPE) {
			return true;
		}

		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {
		if (idBox.charTyped(pCodePoint, pModifiers)) {
			if (!idBox.getValue().isEmpty())
				menu.setId(Integer.valueOf(idBox.getValue()));
			return true;
		}
		return super.charTyped(pCodePoint, pModifiers);
	}

}
