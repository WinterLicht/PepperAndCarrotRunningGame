package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * Player's abilities extend this.
 * 
 * @author WinterLicht
 *
 */
public abstract class Ability {
	/**
	 * This bar shows if ability can be activated and stores values of energy
	 * amount to activate.
	 */
	float durationMax;
	/** How long is effect lasting. */
	private float currentDuration;

	private int energy;

	private int maxEnergy;

	public void increaseEnergy(int incr) {
		energy += incr;
		if (energy > maxEnergy) {
			energy = maxEnergy;
		}
	}

	public void activate(Runner runner, WorldStage worldStage) {
		if (currentDuration >= durationMax) {
			// Activate ability only possible when the previous was finished
			if (energy >= maxEnergy) {
				energy = 0;
				currentDuration = 0f;
				execute(runner, worldStage);
			}
		} else {
			Gdx.app.log(getClass().getSimpleName(), "not enough energy");
		}
	}

	public abstract void update(float delta);

	protected abstract void execute(Runner runner, WorldStage worldStage);

	public int getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}
}
