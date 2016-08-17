package com.peppercarrot.runninggame.world.collision;

import com.peppercarrot.runninggame.entities.Ingredient;

public interface IIngredientCollisionAwareActor extends IHitBoxActor {

	boolean onHitIngredient(Ingredient potion);
}
