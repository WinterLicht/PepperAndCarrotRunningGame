package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.utils.AnimatedImage;

/**
 * Player's abilities extend this.
 * @author WinterLicht
 *
 */
public abstract class Ability {
	public AnimatedImage effect; /** Image representation. */
	Level level; /** Reference to the level. */
	Runner runner; /** Reference to the player. */
	public Table table; /** Add this to the world stage for graphical representation. */
	Button button; /** Button for activation. */
	ProgressBar energy; /** This bar shows if ability can be activated and stores values of energy amount to activate. */
	float durationMax; /** How long is effect lasting. */
	float currentDuration;

	public Ability(Runner r, Level l){
		level = l;
		runner = r;
		table = new Table();
	}

	public void increaseEnergy(int incr){
		if (energy.getValue()+incr <=  energy.getMaxValue()) {
			energy.setValue(energy.getValue() + incr);
		} else {
			energy.setValue(energy.getMaxValue());
		}
	}

	public void activate() {
		if (currentDuration >= durationMax ) {
			//Activate ability only possible when the previous was finished
			if (energy.getValue() >= energy.getMaxValue()) {
				energy.setValue(0);
				currentDuration = 0f;
				execute();
			}
		} else {
			System.out.println("not enough energy");
		}
	}

	public abstract void update(float delta);
	protected abstract void execute();
}
