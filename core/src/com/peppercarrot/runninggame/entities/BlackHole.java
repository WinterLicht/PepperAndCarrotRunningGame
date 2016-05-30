package com.peppercarrot.runninggame.entities;

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
 * Moves all game entities to the given point on the screen.
 * Enemies are instantly destroyed when in area of effect.
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

	private Group affectedEnemies = new Group();
	private Group affectedPotions = new Group();

	private final float RADIUS = 700; //Effect radius
	private float OFFSET_X = (Constants.VIRTUAL_WIDTH * 3) / 4; //where on the screen effect appears
	private float OFFSET_Y = (Constants.VIRTUAL_HEIGHT * 4) / 5 - Constants.OFFSET_TO_GROUND;

	public BlackHole(Runner runner, int maxEnergy, float duration) {
		super(runner, maxEnergy, duration);
		effect = new Effect(getDuration());
		effect.setVisible(false);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		float effectXPosition = OFFSET_X;
		float effectYPosition = OFFSET_Y + worldStage.runner.getY();
		effect.reset();
		//Position centered
		effect.setX(effectXPosition - effect.getWidth()/2);
		effect.setY(effectYPosition - effect.getHeight()/2);
		effect.setVisible(true);

		final List<Enemy> enemies = worldStage.getLevelStream().getEnemiesNear(effectXPosition, effectYPosition, RADIUS);
		for (final Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				enemy.die();
				enemy.setVisible(false);
				//
				AnimatedImage animation = enemy.dyingAnim;
				animation.setVisible(true);
				Rectangle enemyRect = new Rectangle();
				enemy.retrieveHitbox(enemyRect);
				//Set image on initial enemy position
				animation.setX(enemyRect.x);
				animation.setY(enemyRect.y);
				ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
				animation.addAction(pAction);
				affectedEnemies.addActor(animation);
			}
		}
		final List<Potion> potions = worldStage.getLevelStream().getPotionsNear(effectXPosition, effectYPosition, RADIUS);
		for (final Potion potion : potions) {
			if (potion.isVisible()) {
				potion.collected();
				AnimatedImage animation = new AnimatedImage(new AnimatedDrawable(potion.getCopyOfAnimation()));
				animation.setVisible(true);
				Rectangle potionRect = new Rectangle();
				potion.retrieveHitbox(potionRect);
				//Set image on initial potion position
				animation.setX(potionRect.x);
				animation.setY(potionRect.y);
				ParallelAction pAction = new ParallelAction();
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
