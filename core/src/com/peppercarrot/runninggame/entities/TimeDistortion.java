package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.utils.ParticleEffectActor;
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
	public static class Effect extends Group implements IEnemyCollisionAwareActor {

		public Effect(Runner runner, float duration) {
			//clock
			Image clockFace = new Image(new TextureRegion(Assets.I.atlas.findRegion("clock_face")));
			Image clockHand = new Image(new TextureRegion(Assets.I.atlas.findRegion("clock_hand")));
			clockHand.setX(clockFace.getWidth()/2);
			clockHand.setY(clockFace.getHeight()/2);
			clockHand.setOrigin(clockHand.getWidth()/2, clockHand.getWidth()/2);
			clockHand.addAction(Actions.forever(Actions.rotateBy(360, duration)));
			this.addActor(clockFace);
			this.addActor(clockHand);
			this.setWidth(clockFace.getWidth());
			this.setHeight(clockFace.getHeight());
			this.setX((runner.getWidth()-this.getWidth())/2);
			this.setY((runner.getHeight()-this.getHeight())/2);
			this.addAction(Actions.forever(Actions.sequence(Actions.fadeIn(duration/2), Actions.fadeOut(duration/2))));
			
			/*acceleration effect image
			AnimatedImage effect1 = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.1f, Assets.I.getRegions("effect_acceleration"), Animation.PlayMode.LOOP)));
			effect1.setX(-15);
			effect1.setY(30);
			AnimatedImage effect2 = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.1f, Assets.I.getRegions("effect_acceleration"), Animation.PlayMode.LOOP)));
			effect2.setX(14);
			effect2.setY(144);
			AnimatedImage effect3 = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.1f, Assets.I.getRegions("effect_acceleration"), Animation.PlayMode.LOOP)));
			effect3.setX(-59);
			effect3.setY(120);
			AnimatedImage effect4 = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.1f, Assets.I.getRegions("effect_acceleration"), Animation.PlayMode.LOOP)));
			effect4.setX(-86);
			effect4.setY(75);
			this.addActor(effect1);
			this.addActor(effect2);
			this.addActor(effect3);
			this.addActor(effect4);
			*/
			ParticleEffectActor p = new ParticleEffectActor(50, 65, "sparks-acceleration.p");
			ParticleEffectActor p2 = new ParticleEffectActor(13, 125, "sparks-acceleration.p");
			ParticleEffectActor p3 = new ParticleEffectActor(40, 185, "sparks-acceleration.p");
			this.addActor(p);
			this.addActor(p2);
			this.addActor(p3);
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive() && !enemy.indestructible) {
				Account.I.huntEnemies += 1;
				enemy.die();
			}
			return false;
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = getColor();
			super.draw(batch, parentAlpha);
			//Reset Alpha after drawing
			batch.setColor(color.r, color.g, color.b, 1f);
		}
	}

	private final Effect effect;
	
	private float previousSpeedFactor;

	private WorldStage worldStage;

	private final float halfDuration;

	private final float maxSpeed;

	public TimeDistortion(Runner runner, int maxEnergy, float duration, float maxSpeed) {
		super(runner, maxEnergy, duration);
		effect = new Effect(runner, duration);
		this.maxSpeed = maxSpeed;
		this.halfDuration = getDuration() / 2.0f;
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();
		previousSpeedFactor = worldStage.getSpeedFactor();
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
