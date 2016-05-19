package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Moves all game entities to the top left corner of the screen.
 * 
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {
	private final AnimatedImage effect;

	private final List<Enemy> affectedEnemies = new ArrayList<Enemy>();

	private final List<Potion> affectedPotions = new ArrayList<Potion>();

	public BlackHole(Runner runner) {
		super(runner, 1, 1.0f);
		effect = new AnimatedImage(new AnimatedDrawable(
				new Animation(getDuration() / 8, Assets.I.getRegions("black-hole"), Animation.PlayMode.LOOP_PINGPONG)));
		effect.setVisible(false);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		// effect.setX((Constants.VIRTUAL_WIDTH * 3) / 4);
		// effect.setY(Constants.VIRTUAL_HEIGHT);
		// effect.setVisible(true);
		// effect.reset();
		//
		// worldStage.getLevelStream().getEnemiesNear(x)
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

	@Override
	protected void internalUpdate(float delta) {
	}

	@Override
	protected void finish() {
		for (final Enemy enemy : affectedEnemies) {
			enemy.die();
			enemy.setVisible(false);
		}
		for (final Potion potion : affectedPotions) {
			potion.collected();
		}
		effect.setVisible(false);
	}
}
