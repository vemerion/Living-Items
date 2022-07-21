package mod.vemerion.livingitems.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class LivingItemEntity extends Mob {

	public LivingItemEntity(EntityType<? extends LivingItemEntity> type, Level level) {
		super(type, level);
		setPersistenceRequired();
		xpReward = 0;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

}
