package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;

/**
 * This effect manipulates horizontal scroll speed of the level. First in a
 * given effect duration scroll speed of the level is increasing by pow3 and
 * then decreasing again, so level is at his old scroll speed as before, when
 * the effect ends. Enemies are destroyed on collision. 
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class TimeDistortion extends Ability {
	public static class Effect extends Image implements IEnemyCollisionAwareActor {

		public Effect(Runner runner) {
			super(new TextureRegion(Assets.I.atlas.findRegion("timeDistortion_effect")));
			setX((runner.getWidth()-this.getWidth())/2);
			setY((runner.getHeight()-this.getHeight())/2);
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive()) {
				enemy.die();
			}
			return false;
		}
	}
	private final Effect effect;
	
	private float previousSpeedFactor;

	private WorldStage worldStage;

	private final float halfDuration;

	private final float maxSpeed;

	public TimeDistortion(Runner runner, int maxEnergy, float duration, float maxSpeed) {
		super(runner, maxEnergy, duration);
		effect = new Effect(runner);
		this.maxSpeed = maxSpeed;
		this.halfDuration = getDuration() / 2.0f;
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();previousSpeedFactor = worldStage.getSpeedFactor();
		runner.addActor(effect);
		worldStage.addEnemyAwareActor(effect);
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			final float currentDuration = getCurrentDuration();
			final float factor = getFactor(currentDuration);
			worldStage.setSpeedFactor(factor);
		}
	}

	private float getFactor(final float currentDuration) {
		if (currentDuration >= halfDuration) {
			final float percentage = (currentDuration - halfDuration) / halfDuration;
			return Interpolation.pow3In.apply(maxSpeed, 1, percentage);
		}

		final float percentage = currentDuration / halfDuration;
		return Interpolation.pow3Out.apply(1, maxSpeed, percentage);
	}

	@Override
	protected void finish() {
		worldStage.removeEnemyAwareActor(effect);
		final Runner runner = getRunner();
		runner.removeActor(effect);
		worldStage.setSpeedFactor(previousSpeedFactor);
	}
}
