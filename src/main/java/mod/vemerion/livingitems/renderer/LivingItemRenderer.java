package mod.vemerion.livingitems.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.livingitems.entity.LivingItemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class LivingItemRenderer extends EntityRenderer<LivingItemEntity> {

	private ItemRenderer itemRenderer;

	public LivingItemRenderer(Context context) {
		super(context);
		itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(LivingItemEntity entity, float p_114486_, float p_114487_, PoseStack poseStack,
			MultiBufferSource source, int p_114490_) {
		super.render(entity, p_114486_, p_114487_, poseStack, source, p_114490_);

		poseStack.pushPose();
		var scale = 0.5f;
		poseStack.scale(scale, scale, scale);
		poseStack.translate(0, scale, 0);
		itemRenderer.renderStatic(Items.APPLE.getDefaultInstance(), ItemTransforms.TransformType.FIXED, 15728880,
				OverlayTexture.NO_OVERLAY, poseStack, source, entity.getId());
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingItemEntity entity) {
		return null;
	}

}
