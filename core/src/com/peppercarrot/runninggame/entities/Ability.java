package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.utils.AnimatedImage;

/**
 * Player's abilities extend this.
 * @author WinterLicht
 *
 */
public abstract class Ability {
	int energyMax; /** Energy to activate ability. */
	int currentEnergy = 0; /** Current energy. */
	public AnimatedImage effect; /** Image representation. */
	Level level; /** Reference to the level. */
	Runner runner; /** Reference to the player. */

	public Ability(Runner r, Level l){
		level = l;
		runner = r;
	}

	/**
	 * Update ability.
	 * @param delta timedelta
	 */
	public abstract void update(float delta);

}
