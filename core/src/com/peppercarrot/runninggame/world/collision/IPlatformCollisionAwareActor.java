package com.peppercarrot.runninggame.world.collision;

import com.peppercarrot.runninggame.world.LevelSegment;

public interface IPlatformCollisionAwareActor {
	float getPlatformCollisionX();

	float getPlatformCollisionY();

	float getPlatformCollisionWidth();

	boolean onHitPlatform(LevelSegment.Platform platform, float platformHitTop);
}
