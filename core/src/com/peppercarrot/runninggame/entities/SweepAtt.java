package com.peppercarrot.runninggame.entities;

import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * Simple sweep attack. This attack is executed around the player. It's image
 * should be always larger than player's sprite, because collision check against
 * enemies is computed used the image's attributes.
 * 
 * @author WinterLicht
 *
 */
public class SweepAtt extends Ability {

	public SweepAtt() {
		// durationMax = 0.6f;
		// currentDuration = durationMax;
		// // Animation of attack
		// effect = new AnimatedImage(new AnimatedDrawable(
		// new Animation(durationMax / 7, Assets.I.getRegions("sweep-effect"),
		// Animation.PlayMode.NORMAL)));
		// effect.setVisible(false);
	}

	// /**
	// * Update effect position, so it is always centered at player.
	// */
	// private void updatePosition() {
	// effect.setX(runner.getX());
	// effect.setY(runner.getY());
	// }

	@Override
	public void update(float delta) {
		// if (currentDuration >= durationMax) {
		// // Attack ending
		// switch (runner.currState) {
		// case ATTACK_DOUBLEJUMPING:
		// runner.currState = State.DOUBLEJUMPING;
		// break;
		// case ATTACK_FALLING:
		// runner.currState = State.FALLING;
		// break;
		// case ATTACK_JUMPING:
		// runner.currState = State.JUMPING;
		// break;
		// case ATTACK_RUNNING:
		// runner.currState = State.RUNNING;
		// break;
		// default:
		// break;
		// }
		// effect.setVisible(false);
		// } else {
		// if (effect.isVisible()) {
		// // Attack is currently executing
		// updatePosition();
		// checkCollision();
		// currentDuration += delta;
		// }
		// }
	}

	/**
	 * Check collision and let the hit enemy die.
	 * 
	 * @param enemies
	 */
	private void checkCollision() {
		// final Array<Enemy> enemies = level.getEnemiesInRadius(600);
		// final Rectangle effectRect = new Rectangle(effect.getX(),
		// effect.getY(), effect.getWidth(), effect.getHeight());
		// for (final Enemy e : enemies) {
		// if (e.isAlive()) {
		// final Rectangle enemyRect = e.getHitBox();
		// if (effectRect.overlaps(enemyRect)) {
		// e.die();
		// }
		// }
		// }
	}

	// @Override
	// protected void execute() {
	// runner.setAttacking();
	// updatePosition();
	// effect.setVisible(true);
	// effect.reset();
	// }
	@Override
	protected void execute(Runner runner, WorldStage worldStage) {

	}
}
