package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;

/**
 * Carrot dashes to given amount of near enemies.
 * 
 * @author WinterLicht
 *
 */
public class CarrotCharge extends Ability {

	public static class Effect extends AnimatedImage implements IEnemyCollisionAwareActor {
		public int counter = 0;
		public int times = 3; //Jumps to an enemy ... times.
		public List<Enemy> nearEnemies = new ArrayList<Enemy>(); //Stores here near enemies.
		public boolean jumpToNext = false;

		public Effect() {
			super(new AnimatedDrawable(
					new Animation(0.06f, Assets.I.getRegions("carrot_run"), Animation.PlayMode.LOOP)));
			setOrigin(Align.center);
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		public void jumpToEnemy() {
			jumpToNext = false;
			if (counter < times && nearEnemies.size() > counter){
	        	clearActions();
				//Jump to next enemy
				Rectangle tempRect = new Rectangle();
				nearEnemies.get(counter).retrieveHitbox(tempRect);
				SequenceAction seq = new SequenceAction();
				seq.addAction( Actions.moveTo(tempRect.x, tempRect.y, 0.5f, Interpolation.pow2));
				seq.addAction(Actions.run(new Runnable() {
			        @Override
			        public void run() {
			        	/*if (nearEnemies.get(counter-1).isAlive()) {
			        		nearEnemies.get(counter-1).die();
			        	}*/
			        	jumpToNext = true;
			        }
			    }));
				this.addAction(seq);
				mirrorIfNeeded(tempRect.x);
				counter ++;
			} else {
				//Return back to Pepper
				clearActions();
				//FIXME: when after this assignment Pepper jumps,
				//his destination should be updated...?
				SequenceAction seq = new SequenceAction();
				seq.addAction(Actions.moveTo(Constants.OFFSET_TO_EDGE, Constants.OFFSET_TO_GROUND, 0.8f, Interpolation.pow2));
				seq.addAction(Actions.run(new Runnable() {
					@Override
					public void run() {
						//finished effect, so set this false
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
			}
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive()) enemy.die();
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

	private final float RADIUS = Constants.VIRTUAL_WIDTH; //Effect radius
	private final Effect effect;
	WorldStage worldStage;
	
	public CarrotCharge(Runner runner, int maxEnergy) {
		//no duration
		//skill-duration ends when Carrot returns
		super(runner, maxEnergy, -2f);
		effect = new Effect();
		effect.setVisible(false);
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			if (!effect.isVisible()) {
				//cancel when he reaches the destination
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
		Runner runner = getRunner();
		effect.nearEnemies.clear();
		Rectangle runnersRect = new Rectangle();
		((IEnemyCollisionAwareActor) runner).retrieveHitbox(runnersRect);
		//Get near enemies
		List<Enemy> nearEnemies = worldStage.getLevelStream().getEnemiesNear(Constants.OFFSET_TO_EDGE, Constants.VIRTUAL_HEIGHT/2+runnersRect.y, RADIUS);
		int counter = 0;
		for (Enemy enemy : nearEnemies) {
			if (enemy.isAlive()) {
				Rectangle enemyRect = new Rectangle();
				enemy.retrieveHitbox(enemyRect);
				if (enemyRect.x > Constants.OFFSET_TO_EDGE) { //if still on screen and in front of player
					while(counter < effect.times) {
						effect.nearEnemies.add(enemy);
						counter++;
					}
				}
			}
		}
		if (effect.nearEnemies.isEmpty()) {
			//No enemies near, cancel ability
			System.out.println("no enemies - cancel");
			this.cancel();
			return;
		}
		effect.setVisible(true);
		effect.reset();
		effect.setX(runner.pet.getX());
		effect.setY(runner.getY());
		worldStage.addActor(effect);
		runner.pet.setVisible(false);
		worldStage.addEnemyAwareActor(effect);
		//Move to the first enemy
		effect.jumpToNext = true;
	}
}
