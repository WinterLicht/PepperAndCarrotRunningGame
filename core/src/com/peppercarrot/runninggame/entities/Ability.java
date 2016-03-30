package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.utils.AnimatedImage;

public abstract class Ability {
	int energyMax; /** Energy to activate ability. */
	int currentEnergy = 0; /** Current energy */
	AnimatedImage effect;

	public Ability(int c){
		energyMax = c;
	}
	
	public abstract void activate();
	public abstract void update();
}
