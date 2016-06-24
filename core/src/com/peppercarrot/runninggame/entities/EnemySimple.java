package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.utils.Constants;

/**
 * Simple (rectangular) enemy which moves up and down.
 * 
 * @author WinterLicht
 *
 */
public class EnemySimple extends Enemy {
	float intervall = 30;
	float speedY = 140f;
	float initialY;
	private boolean moveUp = true;
	
	public EnemySimple(String name, int damage, float x, float y) {
		super(name, damage, x, y);
		initialY = getY()+Constants.OFFSET_TO_GROUND;
		//Random initial movement direction
		//and offset of initial position
		double r = Math.random();
		if (r < 0.5) {
			moveUp = true;
		} else {
			moveUp = false;
		}
		r = Math.random() * intervall - Math.random() * (intervall);
		setY(getY()+(float) r);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		float posY = this.getY();
		if (posY > initialY+intervall && moveUp) {
			moveUp = false;
		}
		if (posY < initialY-intervall && !moveUp) {
			moveUp = true;
		}
		if (moveUp) {
			this.setY(posY + delta*speedY);
		}else{
			this.setY(posY - delta*speedY);
		}
	}
}
