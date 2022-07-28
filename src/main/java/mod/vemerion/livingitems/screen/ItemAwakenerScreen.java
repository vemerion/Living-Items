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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ItemAwakenerScreen extends AbstractContainerScreen<ItemAwakenerMenu> {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID,
			"textures/gui/item_awakener.png");

	private static final Component ID_BOX_DEFAULT = (new TranslatableComponent(
			Main.guiTranslationKey("id_box_default"))).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);

	private static final int INFO_BUTTON_SIZE = 9;
	private static final int INFO_BUTTON_RIGHT_OFFSET = 14;
	private static final int INFO_BUTTON_TOP_OFFSET = 5;

	private EditBox linkIdBox;
	private Button toggleDenylist;
	private Button toggleSender;

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
		toggleDenylist = addRenderableWidget(new Button(leftPos + 124 - 70 / 2, topPos + 10, 70, 20,
				new TranslatableComponent(Main.guiTranslationKey(menu.isDenylistEnabled() ? "denylist" : "allowlist")),
				(b) -> {
					menu.toggleDenylist();
					b.setMessage(new TranslatableComponent(
							Main.guiTranslationKey(menu.isDenylistEnabled() ? "denylist" : "allowlist")));
				}));
		toggleDenylist.visible = menu.isSender();

		// Sender/Receiver toggle
		toggleSender = addRenderableWidget(new Button(leftPos + 40 - 60 / 2, topPos + 65, 60, 20,
				new TranslatableComponent(Main.guiTranslationKey(menu.isSender() ? "sender" : "receiver")), (b) -> {
					menu.toggleSender();
					toggleDenylist.visible = menu.isSender();
					b.setMessage(
							new TranslatableComponent(Main.guiTranslationKey(menu.isSender() ? "sender" : "receiver")));
				}));

		// Id text box
		linkIdBox = new EditBox(minecraft.font, leftPos + 25, topPos + 40, 27, 9 + 5, ID_BOX_DEFAULT);
		linkIdBox.setMaxLength(3);
		linkIdBox.setFilter(s -> s != null && s.matches("^[0-9]*$"));
		var linkId = menu.getlinkId();
		if (linkId != -1)
			linkIdBox.setValue(String.valueOf(linkId));
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

		linkIdBox.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		if (!linkIdBox.isFocused() && linkIdBox.getValue().isEmpty())
			drawString(pPoseStack, minecraft.font, ID_BOX_DEFAULT, linkIdBox.x + 4, linkIdBox.y + 4, 0xffffffff);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, BACKGROUND);

		// Hide filter slots if item awakener is receiver
		if (!menu.isSender())
			fill(pPoseStack, leftPos + ItemAwakenerMenu.FILTER_LEFT_OFFSET - 1,
					topPos + ItemAwakenerMenu.FILTER_TOP_OFFSET - 1,
					leftPos + ItemAwakenerMenu.FILTER_LEFT_OFFSET
							+ ItemAwakenerMenu.FILTER_LENGTH * ItemAwakenerMenu.SLOT_SIZE,
					topPos + ItemAwakenerMenu.FILTER_TOP_OFFSET
							+ ItemAwakenerMenu.FILTER_LENGTH * ItemAwakenerMenu.SLOT_SIZE,
					0xffc6c6c6);

		// Help texts
		var isHoveringInfo = isHovering(imageWidth - INFO_BUTTON_RIGHT_OFFSET, INFO_BUTTON_TOP_OFFSET, INFO_BUTTON_SIZE,
				INFO_BUTTON_SIZE, pMouseX, pMouseY);
		blit(pPoseStack, leftPos + imageWidth - INFO_BUTTON_RIGHT_OFFSET, topPos + INFO_BUTTON_TOP_OFFSET,
				isHoveringInfo ? INFO_BUTTON_SIZE : 0, imageHeight, INFO_BUTTON_SIZE, INFO_BUTTON_SIZE);
		if (isHoveringInfo) {
			renderHelpText(pPoseStack, 0, linkIdBox.y - 25,
					new TranslatableComponent(Main.guiTranslationKey("help.id_box")), linkIdBox.x);

			renderHelpText(pPoseStack, 0, toggleSender.y + 30,
					new TranslatableComponent(Main.guiTranslationKey("help.sender_toggle")), toggleSender.x);

			renderHelpText(pPoseStack, toggleDenylist.x + toggleDenylist.getWidth(), toggleDenylist.y,
					new TranslatableComponent(Main.guiTranslationKey("help.allowlist_toggle")), 130);

			renderHelpText(pPoseStack, toggleDenylist.x + toggleDenylist.getWidth() - 40, toggleDenylist.y + 100,
					new TranslatableComponent(Main.guiTranslationKey("help.filter")), 150);
		}
	}

	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		renderTooltip(pPoseStack, pMouseX, pMouseY);
	}

	private void renderHelpText(PoseStack pPoseStack, int x, int y, Component text, int maxLength) {
		renderTooltip(pPoseStack, font.split(text, maxLength), x, y);
	}

	@Override
	protected void containerTick() {
		linkIdBox.tick();
	}

	@Override
	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (linkIdBox.mouseClicked(pMouseX, pMouseY, pButton))
			return true;
		return super.mouseClicked(pMouseX, pMouseY, pButton);
	}

	@Override
	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (linkIdBox.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			if (!linkIdBox.getValue().isEmpty())
				menu.setlinkId(Integer.valueOf(linkIdBox.getValue()));
			return true;
		} else if (linkIdBox.isFocused() && linkIdBox.isVisible() && pKeyCode != GLFW.GLFW_KEY_ESCAPE) {
			return true;
		}

		return super.keyPressed(pKeyCode, pScanCode, pModifiers);
	}

	@Override
	public boolean charTyped(char pCodePoint, int pModifiers) {
		if (linkIdBox.charTyped(pCodePoint, pModifiers)) {
			if (!linkIdBox.getValue().isEmpty())
				menu.setlinkId(Integer.valueOf(linkIdBox.getValue()));
			return true;
		}
		return super.charTyped(pCodePoint, pModifiers);
	}

	@Override
	public void onClose() {
		super.onClose();
		menu.sendMessage();
	}

}
