package com.peppercarrot.runninggame.entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.entities.Runner.State;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;

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

	private final AnimatedImage effect;

	private WorldStage worldStage;

	private final Vector2 tempPosition = new Vector2();

	private final Rectangle tempEffectHitbox = new Rectangle();

	private final Rectangle tempEnemyHitbox = new Rectangle();

	public SweepAttack(Runner runner) {
		super(runner, 0, 0.6f);

		effect = new AnimatedImage(new AnimatedDrawable(
				new Animation(getDuration() / 7, Assets.I.getRegions("sweep-effect"), Animation.PlayMode.NORMAL)));
		effect.setVisible(false);
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			if (effect.isVisible() && worldStage != null) {
				checkEnemyCollision(worldStage);
			}
		}
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();
		runner.setAttacking();
		runner.addActor(effect);
		effect.setVisible(true);
		effect.reset();
	}

	private void checkEnemyCollision(WorldStage worldStage) {
		tempPosition.x = effect.getX();
		tempPosition.y = effect.getY();
		getRunner().localToStageCoordinates(tempPosition);

		tempEffectHitbox.x = tempPosition.x;
		tempEffectHitbox.y = tempPosition.y;
		tempEffectHitbox.width = effect.getWidth();
		tempEffectHitbox.height = effect.getHeight();

		final List<Enemy> enemies = worldStage.getLevelStream().getEnemiesNear(tempPosition.x);
		for (final Enemy enemy : enemies) {
			if (enemy.isAlive()) {
				enemy.retrieveRectangle(tempEnemyHitbox);
				if (tempEffectHitbox.overlaps(tempEnemyHitbox)) {
					enemy.die();
				}
			}
		}
	}

	@Override
	protected void finish() {
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
