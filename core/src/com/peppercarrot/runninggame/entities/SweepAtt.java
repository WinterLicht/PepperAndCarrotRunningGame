package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.entities.Runner.State;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Simple sweep attack.
 * This attack is executed around the player. It's image should be
 * always larger than player's sprite, because collision check against
 * enemies is computed used the image's attributes. 
 * @author WinterLicht
 *
 */
public class SweepAtt extends Ability {
	float durationMax = 0.8f;
	float currentDuration = durationMax;

	public SweepAtt(int energyMax, Runner r, Level l) {
		super(energyMax, r, l);
		effect = new AnimatedImage(new Animation(durationMax/8, Assets.I.getRegions("sweep-effect"), Animation.PlayMode.NORMAL));
		effect.setVisible(false);
		effect.stop();
	}

	/**
	 * Activate ability and check if any enemy was hit.
	 * @param enemies
	 */
	public void activate() {
		if (currentDuration >= durationMax ) {
			//Attack only possible when the previous was finished
			if (currentEnergy == energyMax) {
				//Execute attack
				runner.setAttacking();
				currentEnergy = 0;
				updatePosition();
				effect.setVisible(true);
				effect.start();
			} else {
				System.out.println("not enough energy");
			}
		}
	}

	/**
	 * Update effect position, so it is always centered at player.
	 */
	private void updatePosition(){
		effect.setX(runner.getX()-runner.getWidth()/2);
		effect.setY(runner.getY()-runner.getHeight()/2);
	}

	@Override
	public void update(float delta) {
		if (currentDuration < 0) {
			//Attack ending
			switch (runner.currState) {
			case ATTACK_DOUBLEJUMPING:
				runner.currState = State.DOUBLEJUMPING;
				break;
			case ATTACK_FALLING:
				runner.currState = State.FALLING;
				break;
			case ATTACK_JUMPING:
				runner.currState = State.JUMPING;
				break;
			case ATTACK_RUNNING:
				runner.currState = State.RUNNING;
				break;
			default:
				break;
			}
			currentDuration = durationMax;
			effect.stop();
			effect.setVisible(false);
		} else {
			if (effect.isVisible()) {
				//Attack is currently executing
				updatePosition();
				checkCollision();
				currentDuration -= delta;
			}
		}
	}

	/**
	 * Check collision and TODO remove the enemy.
	 * @param enemies
	 */
	private void checkCollision(){
		Array<Image> enemies = level.getAllEnemies();
		Rectangle effectRect = new Rectangle(effect.getX(), effect.getY(), effect.getWidth(), effect.getHeight());
		for (Image enemy : enemies) {
			Rectangle enemyRect = new Rectangle( enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
			if (effectRect.overlaps(enemyRect)) {
				enemy.setVisible(false);
			}
		}
	}
}
