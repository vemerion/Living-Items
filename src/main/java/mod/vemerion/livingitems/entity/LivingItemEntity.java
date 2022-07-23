package mod.vemerion.livingitems.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;

public class LivingItemEntity extends Mob {

	private static final EntityDataAccessor<Byte> ANIMATION_VARIANT = SynchedEntityData.defineId(Llama.class,
			EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(Llama.class,
			EntityDataSerializers.ITEM_STACK);

	private static final byte ANIMATION_VARIANT_COUNT = 5;
	private static final byte ANIMATION_JUMP = 0;
	private static final byte ANIMATION_WALK = 1;
	private static final byte ANIMATION_WORM = 2;
	private static final byte ANIMATION_ROLL = 3;
	private static final byte ANIMATION_FLY = 4;

	private Vec3 animationRotation = Vec3.ZERO;
	private Vec3 animationOffset = Vec3.ZERO;
	private Vec3 animationScale = new Vec3(1, 1, 1);
	private Vec3 animationRotation0 = Vec3.ZERO;
	private Vec3 animationOffset0 = Vec3.ZERO;
	private Vec3 animationScale0 = new Vec3(1, 1, 1);

	public LivingItemEntity(EntityType<? extends LivingItemEntity> type, Level level) {
		super(type, level);
		setPersistenceRequired();
		xpReward = 0;
	}

	public LivingItemEntity(EntityType<? extends LivingItemEntity> type, Level level, ItemStack stack) {
		this(type, level);
		setItemStack(stack);
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty,
			MobSpawnType pReason, SpawnGroupData pSpawnData, CompoundTag pDataTag) {
		setAnimationVariant((byte) getRandom().nextInt(ANIMATION_VARIANT_COUNT));
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
	}
	
	@Override
	protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
		super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
		spawnAtLocation(getItemStack());
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ANIMATION_VARIANT, ANIMATION_JUMP);
		entityData.define(ITEM_STACK, Items.STONE.getDefaultInstance());
	}

	private void setAnimationVariant(byte variant) {
		entityData.set(ANIMATION_VARIANT, variant);
	}

	public byte getAnimationVariant() {
		return entityData.get(ANIMATION_VARIANT);
	}

	private void setItemStack(ItemStack stack) {
		entityData.set(ITEM_STACK, stack);
	}

	public ItemStack getItemStack() {
		return entityData.get(ITEM_STACK);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag pCompound) {
		super.readAdditionalSaveData(pCompound);
		if (pCompound.contains("animationVariant"))
			setAnimationVariant(pCompound.getByte("animationVariant"));
		if (pCompound.contains("itemStack"))
			setItemStack(ItemStack.of(pCompound.getCompound("itemStack")));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag pCompound) {
		super.addAdditionalSaveData(pCompound);
		pCompound.putByte("animationVariant", getAnimationVariant());
		pCompound.put("itemStack", getItemStack().serializeNBT());
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide) {
			animationRotation0 = animationRotation;
			animationOffset0 = animationOffset;
			animationScale0 = animationScale;

			switch (getAnimationVariant()) {
			case ANIMATION_JUMP:
				animationOffset = new Vec3(0, Math.abs(Mth.sin(tickCount * Mth.PI / 30f)), 0);
				animationRotation = new Vec3(tickCount * Mth.HALF_PI / 30f, Mth.HALF_PI, 0);
				break;
			case ANIMATION_WALK:
				animationOffset = new Vec3(0, Math.abs(Mth.sin(tickCount * Mth.PI / 10f)) * 0.5, 0);
				animationRotation = new Vec3(0, 0, Mth.sin(tickCount * Mth.PI / 10f) * 0.2f);
				break;
			case ANIMATION_WORM:
				animationRotation = new Vec3(0, Mth.HALF_PI, 0);
				animationScale = new Vec3(Mth.sin(tickCount * Mth.PI / 30f) * 0.5 + 1, 1, 1);
				break;
			case ANIMATION_ROLL:
				animationRotation = new Vec3(tickCount * Mth.HALF_PI / 17f, Mth.HALF_PI, 0);
				break;
			case ANIMATION_FLY:
				animationOffset = new Vec3(0, Mth.sin(tickCount * Mth.PI / 35f) * 0.4 + 0.5, 0);
				animationRotation = new Vec3(0, 0, Mth.HALF_PI + tickCount * Mth.HALF_PI / 30f);
				break;
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
		if (!level.isClientSide) {
			ItemHandlerHelper.giveItemToPlayer(pPlayer, getItemStack());
			discard();
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	private Vec3 lerpVector(float value, Vec3 start, Vec3 end) {
		return new Vec3(Mth.lerp(value, start.x, end.x), Mth.lerp(value, start.y, end.y),
				Mth.lerp(value, start.z, end.z));
	}

	public Vec3 getAnimationOffset(float partialTicks) {
		return lerpVector(partialTicks, animationOffset0, animationOffset);
	}

	public Vec3 getAnimationRotation(float partialTicks) {
		return lerpVector(partialTicks, animationRotation0, animationRotation);
	}

	public Vec3 getAnimationScale(float partialTicks) {
		return lerpVector(partialTicks, animationScale0, animationScale);
	}

}
