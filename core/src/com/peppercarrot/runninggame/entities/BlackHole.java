package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Moves all game entities to the given point on the screen. Enemies are
 * instantly destroyed when in area of effect.
 * 
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {

	public static class Effect extends AnimatedImage {

		public Effect(float duration) {
			super(new AnimatedDrawable(
					new Animation(duration, Assets.I.getRegions("black-hole"), Animation.PlayMode.LOOP_PINGPONG)));
		}
	}

	private final Effect effect;

	private final Group affectedEnemies = new Group();
	private final Group affectedPotions = new Group();

	private final float RADIUS = 700; // Effect radius
	private final float OFFSET_X = (Constants.VIRTUAL_WIDTH * 3) / 4; // where
																		// on
																		// the
																		// screen
																		// effect
																		// appears
	private final float OFFSET_Y = (Constants.VIRTUAL_HEIGHT * 4) / 5 - Constants.OFFSET_TO_GROUND;

	private final List<Enemy> tempAffectedEnemies = new ArrayList<Enemy>();
	private final List<Potion> tempAffectedPotions = new ArrayList<Potion>();
	private final Rectangle tempRect = new Rectangle();

	public BlackHole(Runner runner, int maxEnergy, float duration) {
		super(runner, maxEnergy, duration);
		effect = new Effect(getDuration());
		effect.setVisible(false);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		final float effectXPosition = OFFSET_X;
		final float effectYPosition = OFFSET_Y + worldStage.runner.getY();
		effect.reset();
		// Position centered
		effect.setX(effectXPosition - effect.getWidth() / 2);
		effect.setY(effectYPosition - effect.getHeight() / 2);
		effect.setVisible(true);

		worldStage.getLevelStream().getEnemiesNear(effectXPosition, effectYPosition, RADIUS, tempAffectedEnemies);
		for (final Enemy enemy : tempAffectedEnemies) {
			if (enemy.isAlive()) {
				enemy.die();
				enemy.setVisible(false);
				//
				final AnimatedImage animation = enemy.dyingAnim;
				animation.setVisible(true);
				// Set image on initial enemy position
				enemy.retrieveHitbox(tempRect);
				animation.setX(tempRect.x);
				animation.setY(tempRect.y);
				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
				animation.addAction(pAction);
				affectedEnemies.addActor(animation);
			}
		}
		worldStage.getLevelStream().getPotionsNear(effectXPosition, effectYPosition, RADIUS, tempAffectedPotions);
		for (final Potion potion : tempAffectedPotions) {
			if (potion.isVisible()) {
				potion.collected();
				final AnimatedImage animation = new AnimatedImage(new AnimatedDrawable(potion.getCopyOfAnimation()));
				animation.setVisible(true);
				// Set image on initial potion position
				potion.retrieveHitbox(tempRect);
				animation.setX(tempRect.x);
				animation.setY(tempRect.y);
				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
				animation.addAction(pAction);
				affectedPotions.addActor(animation);
			}
		}
		worldStage.addActor(affectedPotions);
		worldStage.addActor(affectedEnemies);
		worldStage.addActor(effect);
	}

	@Override
	protected void internalUpdate(float delta) {
	}

	@Override
	protected void finish() {
		affectedEnemies.remove();
		affectedPotions.remove();
		affectedPotions.clear();
		affectedEnemies.clear();
		effect.getParent().removeActor(effect);
		effect.setVisible(false);
	}
}
