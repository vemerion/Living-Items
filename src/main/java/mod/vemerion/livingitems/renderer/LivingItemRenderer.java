package mod.vemerion.livingitems.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import mod.vemerion.livingitems.entity.LivingItemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LivingItemRenderer extends EntityRenderer<LivingItemEntity> {

	private ItemRenderer itemRenderer;

	public LivingItemRenderer(Context context) {
		super(context);
		itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(LivingItemEntity entity, float yaw, float partialTicks, PoseStack poseStack,
			MultiBufferSource source, int light) {
		super.render(entity, yaw, partialTicks, poseStack, source, light);

		poseStack.pushPose();
		var baseScale = 0.5f;
		poseStack.scale(baseScale, baseScale, baseScale);
		poseStack.translate(0, baseScale, 0);
		var offset = entity.getAnimationOffset(partialTicks);
		poseStack.translate(offset.x(), offset.y(), offset.z());
		poseStack.mulPose(Quaternion.fromXYZ(new Vector3f(entity.getAnimationRotation(partialTicks))));
		var scale = entity.getAnimationScale(partialTicks);
		poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);
		itemRenderer.renderStatic(entity.getItemStack(), ItemTransforms.TransformType.FIXED, light,
				OverlayTexture.NO_OVERLAY, poseStack, source, entity.getId());
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(LivingItemEntity entity) {
		return null;
	}

}
