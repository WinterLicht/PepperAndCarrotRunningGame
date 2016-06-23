package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.math.Interpolation;
import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * This effect manipulates horizontal scroll speed of the level. First in a
 * given effect duration scroll speed of the level is increasing by pow3 and
 * then decreasing again, so level is at his old scroll speed as before, when
 * the effect ends.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class TimeDistortion extends Ability {

	private float previousSpeedFactor;

	private WorldStage worldStage;

	private final float halfDuration;

	private final float maxSpeed;

	public TimeDistortion(Runner runner, int maxEnergy, float duration, float maxSpeed) {
		super(runner, maxEnergy, duration);
		this.maxSpeed = maxSpeed;
		this.halfDuration = getDuration() / 2.0f;
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
			final float factor = getFactor(currentDuration);
			worldStage.setSpeedFactor(factor);
		}
	}

	private float getFactor(final float currentDuration) {
		if (currentDuration >= halfDuration) {
			final float percentage = (currentDuration - halfDuration) / halfDuration;
			return Interpolation.pow3In.apply(maxSpeed, 1, percentage);
		}

		final float percentage = currentDuration / halfDuration;
		return Interpolation.pow3Out.apply(1, maxSpeed, percentage);
	}

	@Override
	protected void finish() {
		worldStage.setSpeedFactor(previousSpeedFactor);
	}
}
