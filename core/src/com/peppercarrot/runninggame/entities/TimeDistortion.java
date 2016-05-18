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

	private float previousSpeedFactor;

	private WorldStage worldStage;

	public TimeDistortion(Runner runner) {
		super(runner, 6, 8.0f);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		previousSpeedFactor = worldStage.getSpeedFactor();

		worldStage.setSpeedFactor(0.5f);
	}

	@Override
	protected void internalUpdate(float delta) {
		// TODO: Tween the speed factor from factor => 0 => factor => previous
	}

	@Override
	protected void finish() {
		worldStage.setSpeedFactor(previousSpeedFactor);
	}
}
