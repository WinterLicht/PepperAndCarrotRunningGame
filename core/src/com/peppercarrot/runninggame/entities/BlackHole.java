package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.stages.WorldStage;

/**
 * Moves all game entities to the top left corner of the screen.
 * 
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {
	Array<Enemy> enemies;
	Array<Potion> potions;

	public BlackHole() {
		// durationMax = 1f;
		// currentDuration = durationMax;
		// enemies = new Array<Enemy>();
		// potions = new Array<Potion>();
		// // Animation of attack
		// effect = new AnimatedImage(new AnimatedDrawable(
		// new Animation(durationMax / 8, Assets.I.getRegions("black-hole"),
		// Animation.PlayMode.LOOP_PINGPONG)));
		// effect.setVisible(false);
	}

	@Override
	public void update(float delta) {
		// if (currentDuration >= durationMax) {
		// // Effect ending
		// for (final Enemy enemy : enemies) {
		// enemy.die();
		// enemy.setVisible(false);
		// }
		// for (final Potion potion : potions) {
		// potion.collected();
		// }
		// effect.setVisible(false);
		// } else {
		// // Effect active
		// currentDuration += delta;
		// }
	}

	@Override
	protected void execute(Runner runner, WorldStage worldStage) {
		// effect.setX((Constants.VIRTUAL_WIDTH * 3) / 4);
		// effect.setY(Constants.VIRTUAL_HEIGHT);
		// effect.setVisible(true);
		// effect.reset();
		// enemies = level.getEnemiesInRadius(Constants.VIRTUAL_WIDTH);
		// potions = level.getPotionsInRadius(Constants.VIRTUAL_WIDTH);
		// for (final Enemy enemy : enemies) {
		// final ParallelAction pAction = new ParallelAction();
		// pAction.addAction(Actions.moveTo(effect.getX(), effect.getY(),
		// durationMax, Interpolation.pow2));
		// pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
		// enemy.addAction(pAction);
		// }
		// for (final Potion potion : potions) {
		// final ParallelAction pAction = new ParallelAction();
		// pAction.addAction(Actions.moveTo(effect.getX(), effect.getY(),
		// durationMax, Interpolation.pow2));
		// pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
		// potion.addAction(pAction);
		// }
	}

}
