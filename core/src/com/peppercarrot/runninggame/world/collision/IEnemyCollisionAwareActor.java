package com.peppercarrot.runninggame.world.collision;

import com.peppercarrot.runninggame.entities.Enemy;

public interface IEnemyCollisionAwareActor extends IHitBoxActor {

	boolean onHitEnemy(Enemy enemy);
}
