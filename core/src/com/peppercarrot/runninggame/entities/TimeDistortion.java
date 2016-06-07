package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * This effect manipulates horizontal scroll speed of the level. First in a
 * given effect duration scroll speed of the level is decreasing and then
 * increasing again, so level is at his old scroll speed as before, when the
 * effect ends.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class TimeDistortion extends Ability {

	private float previousSpeedFactor;

	private WorldStage worldStage;

	private final float halfDuration;

	public TimeDistortion(Runner runner, int maxEnergy, float duration) {
		super(runner, maxEnergy, duration);
		halfDuration = getDuration() / 2.0f;
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		previousSpeedFactor = worldStage.getSpeedFactor();
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			final float currentDuration = getCurrentDuration();
			if (currentDuration >= halfDuration) {
				worldStage.setSpeedFactor((currentDuration - halfDuration) / halfDuration);
			} else {
				worldStage.setSpeedFactor(1 - currentDuration / halfDuration);
			}
		}
	}

	@Override
	protected void finish() {
		worldStage.setSpeedFactor(previousSpeedFactor);
	}
}
