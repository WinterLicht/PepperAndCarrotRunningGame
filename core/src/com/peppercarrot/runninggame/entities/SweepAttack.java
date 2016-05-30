package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.entities.Runner.State;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;

/**
 * Simple sweep attack. This attack is executed around the player. It's image
 * should be always larger than player's sprite, because collision check against
 * enemies is computed used the image's attributes.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class SweepAttack extends Ability {

	public static class Effect extends AnimatedImage implements IEnemyCollisionAwareActor {

		public Effect(float duration) {
			super(new AnimatedDrawable(
					new Animation(duration / 8, Assets.I.getRegions("sweep-effect"), Animation.PlayMode.NORMAL)));
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

	private WorldStage worldStage;

	public SweepAttack(Runner runner) {
		super(runner, 0, 0.6f);

		effect = new Effect(getDuration());
		effect.setVisible(false);
	}

	@Override
	protected void internalUpdate(float delta) {
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();
		runner.setAttacking();
		runner.addActor(effect);
		effect.setVisible(true);
		effect.reset();
		worldStage.addEnemyAwareActor(effect);
	}

	@Override
	protected void finish() {
		worldStage.removeEnemyAwareActor(effect);
		effect.setVisible(false);

		final Runner runner = getRunner();
		runner.removeActor(effect);

		switch (runner.getCurrentState()) {
		case ATTACK_DOUBLEJUMPING:
			runner.setState(State.DOUBLEJUMPING);
			break;
		case ATTACK_FALLING:
			runner.setState(State.FALLING);
			break;
		case ATTACK_JUMPING:
			runner.setState(State.JUMPING);
			break;
		case ATTACK_RUNNING:
			runner.setState(State.RUNNING);
			break;
		default:
			break;
		}
	}
}
