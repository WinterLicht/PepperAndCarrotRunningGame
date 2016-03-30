package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public class SweepAtt extends Ability {
	int durationMax = 50;
	int currentDuration = 0;

	public SweepAtt(int c) {
		super(c);
		effect = new AnimatedImage(new Animation(0.099f, Assets.I.getRegions("sweep-effect"), Animation.PlayMode.NORMAL));
	}

	@Override
	public void activate() {
		if (currentEnergy == energyMax) {
			currentEnergy = 0;
			
		} else {
			System.out.println("not enough energy");
		}
	}

	@Override
	public void update() {
		if (currentDuration == durationMax) {
			effect.stop();
		} else {
			currentDuration += 1;
		}
	}

	public void checkCollision(){
		Rectangle effectRect = new Rectangle(effect.getX(), effect.getY(), effect.getWidth(), effect.getHeight());

		//effectRect.overlaps( );
	}
}
