package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Moves all game entities to the top left corner of the screen.
 * 
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {

	public static class Effect extends AnimatedImage {

		public Effect(float duration) {
			super(new AnimatedDrawable(
					new Animation(duration / 8, Assets.I.getRegions("black-hole"), Animation.PlayMode.LOOP_PINGPONG)));
		}
	}

	private final Effect effect;

	private final List<Enemy> affectedEnemies = new ArrayList<Enemy>();

	private final List<Potion> affectedPotions = new ArrayList<Potion>();

	private final float RADIUS = Constants.VIRTUAL_WIDTH;

	public BlackHole(Runner runner) {
		super(runner, 1, 1.0f);
		effect = new Effect(getDuration());
		effect.setVisible(false);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		final float effectXPosition = getRunner().getX() + (Constants.VIRTUAL_WIDTH * 3) / 4;
		final float effectYPosition = getRunner().getY() + Constants.VIRTUAL_HEIGHT;

		effect.setX(effectXPosition);
		effect.setY(effectYPosition);
		effect.setVisible(true);
		effect.reset();
		worldStage.addActor(effect);

		final List<Enemy> enemies = worldStage.getLevelStream().getEnemiesNear(effectXPosition);
		for (final Enemy enemy : enemies) {
			if (isAffected(effectXPosition, enemy)) {
				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
				enemy.addAction(pAction);
				affectedEnemies.add(enemy);
			}
		}

		final List<Potion> potions = worldStage.getLevelStream().getPotionsNear(effectXPosition);
		for (final Potion potion : potions) {
			if (isAffected(effectXPosition, potion)) {
				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
				potion.addAction(pAction);
				affectedPotions.add(potion);
			}
		}

	}

	private boolean isAffected(float effectXPosition, Actor actor) {
		return Math.abs(effectXPosition - actor.getX()) < RADIUS;
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

		affectedPotions.clear();
		affectedEnemies.clear();

		effect.getParent().removeActor(effect);
		effect.setVisible(false);
	}
}
