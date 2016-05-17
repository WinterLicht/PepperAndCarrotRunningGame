package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * This effect manipulates horizontal scroll speed of the level. First in a
 * given effect duration scroll speed of the level is decreasing and then
 * increasing again, so level is at his old scroll speed as before, when the
 * effect ends.
 * 
 * @author WinterLicht
 *
 */
public class TimeDistortion extends Ability {
	// float speedDistortion = 4;
	// float oldLevelSpeed;

	public TimeDistortion() {
		// durationMax = 8f;
		// currentDuration = durationMax;
		// oldLevelSpeed = l.scrollSpeed;
	}

	@Override
	public void update(float delta) {
		// if (currentDuration >= durationMax) {
		// // Effect is ending
		// level.scrollSpeed = oldLevelSpeed;
		// } else {
		// // Effect active
		// currentDuration += delta;
		// float scrollSpeed;
		// // Calculate current scrollspeed
		// if (currentDuration < durationMax / 2) {
		// // scrollSpeed changing from 0 to speedDistortion
		// scrollSpeed = speedDistortion * currentDuration * 2 / durationMax;
		// } else {
		// // scrollSpeed changing from speedDistortion to 0
		// scrollSpeed = -((speedDistortion * currentDuration * 2 / durationMax)
		// - speedDistortion * 2);
		// }
		// level.scrollSpeed = oldLevelSpeed - scrollSpeed;
		// }
	}

	// @Override
	// protected void execute() {
	// oldLevelSpeed = level.scrollSpeed;
	// }
	@Override
	protected void execute(Runner runner, WorldStage worldStage) {
	}
}
