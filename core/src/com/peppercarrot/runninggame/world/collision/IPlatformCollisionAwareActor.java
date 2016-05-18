package com.peppercarrot.runninggame.world.collision;

import com.peppercarrot.runninggame.world.Platform;

public interface IPlatformCollisionAwareActor {
	float getPlatformCollisionX();

	float getPlatformCollisionY();

	boolean onHitPlatform(Platform platform, float platformHitTop);
}
