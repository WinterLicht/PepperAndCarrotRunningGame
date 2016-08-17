package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;

/**
 * Player's abilities extend this.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public abstract class Ability {

	/**
	 * The parent runner
	 */
	private final Runner runner;

	/**
	 * How much energy does this ability currently have
	 */
	private int energy = 0;

	/**
	 * Hoch much energy does this ability need
	 */
	private final int maxEnergy;

	/**
	 * How long is effect lasting, negative means no cooldown.
	 */
	protected final float duration;

	/**
	 * Is this ability currently running
	 */
	protected boolean running;

	/**
	 * How long is this ability running.
	 */
	protected float currentDuration = 0;

	public Ability(Runner runner, int maxEnergy, float duration) {
		this.runner = runner;
		this.maxEnergy = maxEnergy;
		this.duration = duration;
	}

	/**
	 * Increate the available energy for this ability. Will be clipped at {#link
	 * {@link #maxEnergy}.
	 * 
	 * @param value
	 *            energy to add
	 */
	public void increaseEnergy(int value) {
		energy += value;
		if (energy > maxEnergy) {
			energy = maxEnergy;
		}
	}

	/**
	 * Updates the effect. If it is currently running, a cool down will be done.
	 * 
	 * @param delta
	 *            time since last frame
	 */
	public void update(float delta) {
		if (running) {
			coolDown(delta);
			internalUpdate(delta);
		}
	}

	/**
	 * Cancel ability.
	 */
	protected void cancel() {
		finish();
		running = false;
		currentDuration = 0.0f;
		energy = 0;
	}


	/**
	 * Specific ability update
	 * 
	 * @param delta
	 *            time since last frame
	 */
	protected abstract void internalUpdate(float delta);

	private void coolDown(float delta) {
		if (running && duration > -1) {
			currentDuration += delta;
			if (currentDuration > duration) {
				finish();
				running = false;
				currentDuration = 0.0f;
			}
		}
	}

	/**
	 * Called after the effect is done
	 */
	protected abstract void finish();

	/**
	 * Activate the effect for a corresponding world, if it is not currently
	 * running and it has enough energy.
	 * 
	 * @param worldStage
	 *            game world
	 */
	public void activate(WorldStage worldStage) {
		if (!running) {
			if (energy >= maxEnergy) {
				currentDuration = 0f;
				energy = 0;
				running = true;
				Account.I.usedSkills += 1;
				execute(worldStage);
			} else {
				//Not enough energy
			}
		} else {
			//Is already executing
		}
	}

	/**
	 * Specific ability execution
	 * 
	 * @param worldStage
	 *            game world
	 */
	protected abstract void execute(WorldStage worldStage);

	public int getEnergy() {
		return energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public float getCurrentDuration() {
		return currentDuration;
	}

	public float getDuration() {
		return duration;
	}

	public boolean isRunning() {
		return running;
	}

	public Runner getRunner() {
		return runner;
	}
}
