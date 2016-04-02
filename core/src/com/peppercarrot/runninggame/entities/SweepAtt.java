package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	float durationMax = 0.6f; /** How long is attack lasting. */
	float currentDuration = durationMax;
	//TODO: currentEnergy and energyMax replace by energy.getvalue.

	public SweepAtt(Runner r, Level l) {
		super(r, l);
		//Button
		button = new Button(Assets.I.skin, "default");
		button.setTouchable(Touchable.enabled);
		button.setName(""+1);
		button.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("Attack Button " + event.getListenerActor().getName() + " touched.");
				runner.activateAbility(Integer.parseInt(event.getListenerActor().getName()));
				((Button) event.getListenerActor()).setChecked(false);
				event.cancel();
			}
		});
		energy = new ProgressBar(0, 4, 1, true, Assets.I.skin, "abilityEnergy");
		energy.setValue(0);
		table.add(button).width(180).height(180).left();
		table.add(energy).height(180).right().expandY();
		//Animation of attack
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
			if (energy.getValue() >= energy.getMaxValue()) {
				//Execute attack
				runner.setAttacking();
				energy.setValue(0);
				updatePosition();
				effect.setVisible(true);
				effect.start();
			} else {
				System.out.println("not enough energy");
			}
		}
	}

	public void increaseEnergy(int incr){
		if (energy.getValue()+incr <=  energy.getMaxValue()) {
			energy.setValue(energy.getValue() + incr);
		} else {
			energy.setValue(energy.getMaxValue());
		}
		//energy.setValue(currentEnergy);
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
	 * Check collision and let the hit enemy die.
	 * @param enemies
	 */
	private void checkCollision(){
		Array<Enemy> enemies;
		enemies = level.getEnemiesInRadius(600);
		Rectangle effectRect = new Rectangle(effect.getX(), effect.getY(), effect.getWidth(), effect.getHeight());
		for (Enemy e : enemies) {
			if (e.isAlive()) {
				Rectangle enemyRect = e.getHitBox();
				if (effectRect.overlaps(enemyRect)) {
					e.die();
				}
			}
		}
	}
}
