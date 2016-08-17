package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;

/**
 * Carrot dashes to given amount of near enemies, enemies collided with him are destroyed.
 * 
 * @author WinterLicht
 *
 */
public class CarrotCharge extends Ability {

	public class Effect extends AnimatedImage implements IEnemyCollisionAwareActor {
		private int counter = 0; //Stores number of current jumps

		/**
		 * Jumps to an enemy ... times.
		 */
		public final int times = 3;

		/**
		 * Stores here near enemies.
		 */
		public final List<Enemy> nearEnemies = new ArrayList<Enemy>();

		public boolean jumpToNext = false;
		private Vector2 destination = new Vector2(); //Movement destination
		MoveToAction moveTo;
		Runner runner;

		public Effect(Runner runner) {
			super(new AnimatedDrawable(
					new Animation(0.06f, Assets.I.getRegions("carrot_run"), Animation.PlayMode.LOOP)));
			setScale(runner.getScaleFactor(), runner.getScaleFactor());
			setOrigin(Align.center);
			this.runner = runner;
			moveTo = Actions.action(MoveToAction.class);
			moveTo.setPosition(0, 0);
			moveTo.setDuration(0.4f);
			moveTo.setInterpolation(Interpolation.pow2);
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		private void jumpToEnemy() {
			jumpToNext = false;
			if (nearEnemies.size() > counter){
				counter ++; //jumping, so increase this
	        	clearActions();
				//Jump to next enemy
				SequenceAction seq = new SequenceAction();
				moveTo.reset();
				updateDestinationTowardsCurrentEnemy();
				seq.addAction(moveTo);
				seq.addAction(Actions.run(new Runnable() {
			        @Override
			        public void run() {
			        	jumpToNext = true;
			        	clearActions();
			        }
			    }));
				this.addAction(seq);
				mirrorIfNeeded(destination.x);
			} else {
				// Return back to Pepper
				clearActions();
				SequenceAction seq = new SequenceAction();
				moveTo.reset();
				updateDestinationTowardsRunner();
				seq.addAction(moveTo);
				seq.addAction(Actions.run(new Runnable() {
					@Override
					public void run() {
						// finished effect, so set this false
						setVisible(false);
					}
				}));
				this.addAction(seq);
				mirrorIfNeeded(Constants.OFFSET_TO_EDGE);
			}
		}

		public void update() {
			if (jumpToNext) {
				jumpToEnemy();
			} else {
				if(isVisible()) {
					//This function is needed, because destination
					//point is dynamic.
					updateDestinationForMovement();
				}
			}
		}

		private void updateDestinationForMovement() {
			//Update moveTo destinations
			if (nearEnemies.size() > counter && counter > 0) {
				//Is currently moving towards an enemy
				updateDestinationTowardsCurrentEnemy();
			}
			if (counter > nearEnemies.size()){
				//Is currently moving back to Pepper
				updateDestinationTowardsRunner();
			}
		}

		private void updateDestinationTowardsCurrentEnemy() {
			Rectangle tempRect = new Rectangle();
			//counter-1 needed, because it stores number of jumps
			nearEnemies.get(counter-1).retrieveHitbox(tempRect);
			destination.set(tempRect.x, tempRect.y);
			moveTo.setPosition(destination.x, destination.y);
		}

		private void updateDestinationTowardsRunner() {
			destination.set(Constants.OFFSET_TO_EDGE, runner.getY());
			moveTo.setPosition(destination.x, destination.y);
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive() && !enemy.indestructible)
				Account.I.huntEnemies += 1;
				enemy.die();
			return false;
		}

		/**
		 * Flip image when Carrot is moving left.
		 * 
		 * @param destinationX
		 */
		private void mirrorIfNeeded(float destinationX) {
			if (getX() > destinationX) {
				flipHorizontally();
			}
		}
	}

	/**
	 * Effect radius.
	 */
	private final float RADIUS = Constants.VIRTUAL_WIDTH+70;

	private final Effect effect;

	private WorldStage worldStage;

	private final List<Enemy> tempNearEnemies = new ArrayList<Enemy>();

	public CarrotCharge(Runner runner, int maxEnergy) {
		// no duration
		// skill-duration ends when Carrot returns
		super(runner, maxEnergy, -2f);
		effect = new Effect(runner);
		effect.setVisible(false);
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			if (!effect.isVisible()) {
				// cancel when he reaches the destination
				cancel();
			} else {
				effect.update();
			}
		}
	}

	@Override
	protected void finish() {
		if (effect.getParent() != null) {
			effect.getParent().removeActor(effect);
			worldStage.removeEnemyAwareActor(effect);
		}

		effect.setVisible(false);
		effect.counter = 0;
		effect.jumpToNext = false;
		getRunner().pet.setVisible(true);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();
		effect.nearEnemies.clear();
		final Rectangle tempRect = new Rectangle();
		runner.retrieveHitbox(tempRect);

		// Get near enemies
		worldStage.getLevelStream().getEnemiesNear(Constants.OFFSET_TO_EDGE, Constants.VIRTUAL_HEIGHT / 2 + tempRect.y,
				RADIUS, tempNearEnemies);
		int counter = 0;
		for (final Enemy enemy : tempNearEnemies) {
			if (enemy.isAlive() && !enemy.indestructible) {
				Rectangle enemyRect = new Rectangle();
				enemy.retrieveHitbox(enemyRect);
				if (enemyRect.x > Constants.OFFSET_TO_EDGE) { //if still on screen and in front of player
					counter++;
					if (counter <= effect.times) {
						effect.nearEnemies.add(enemy);
					} else {
						break;
					}
				}
			}
		}
		if (effect.nearEnemies.isEmpty()) {
			// No enemies near, cancel ability
			Gdx.app.log(getClass().getSimpleName(), "no enemies - cancel");
			this.cancel();
			return;
		}
		effect.setVisible(true);
		effect.reset();
		effect.setX(runner.getX());
		effect.setY(runner.getY());

		worldStage.addActor(effect);
		runner.pet.setVisible(false);
		worldStage.addEnemyAwareActor(effect);

		// Move to the first enemy
		effect.jumpToNext = true;
	}
}
