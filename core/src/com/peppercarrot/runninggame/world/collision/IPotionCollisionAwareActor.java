package com.peppercarrot.runninggame.world.collision;

import com.peppercarrot.runninggame.entities.Potion;

public interface IPotionCollisionAwareActor extends IHitBoxActor {

	boolean onHitPotion(Potion potion);
}
