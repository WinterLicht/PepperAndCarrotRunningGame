package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

		public Effect() {
			super(new AnimatedDrawable(
					new Animation(0.06f, Assets.I.getRegions("carrot_run"), Animation.PlayMode.LOOP)));
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive()) {
				//Destroy hit enemy
				enemy.die();
				counter ++;
				if (counter < times){
					//Jump to next enemy
					Rectangle tempRect = new Rectangle();
					nearEnemies.get(counter).retrieveHitbox(tempRect);
					this.clearActions();
					this.addAction(Actions.moveTo(tempRect.x, tempRect.y, 0.5f, Interpolation.pow2));
					mirrorIfNeeded(tempRect.x);
				} else {
					//Return back to Pepper
					this.clearActions();
					//FIXME: when after this assignment Pepper jumps,
					//his destination should be updated...
					this.addAction(Actions.moveTo(Constants.OFFSET_TO_EDGE, Constants.OFFSET_TO_GROUND, 0.8f, Interpolation.pow2));
					mirrorIfNeeded(Constants.OFFSET_TO_EDGE);
				}
			}
			return false;
		}

		/**
		 * Flip image when Carrot is moving left.
		 * 
		 * @param destinationX
		 */
		private void mirrorIfNeeded(float destinationX) {
			if (getX() > destinationX) {
				flipHorizontally(true);
			}else{
				flipHorizontally(false);
			}
		}
	}

	private final Effect effect;
	WorldStage worldStage;
	
	public CarrotCharge(Runner runner) {
		//runner, maxEnergy, duration
		super(runner, 0, -1f);
		effect = new Effect();
		effect.setVisible(false);
	}

	@Override
	protected void internalUpdate(float delta) {
		if (isRunning()) {
			int offset = 10;
			if (effect.getX() <= Constants.OFFSET_TO_EDGE+offset &&
				effect.getY() <= Constants.OFFSET_TO_GROUND+offset &&
				effect.counter > 0) {
				//cancel when he reaches the destination
				cancel();
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
		getRunner().pet.setVisible(true);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		//Get near enemies
		effect.nearEnemies.clear();
		for (int i=0; i<effect.times; i++ ){
			if (worldStage.getLevelStream().getEnemiesNear(Constants.VIRTUAL_WIDTH).size() > i) {
				//FIXME: "getEnemiesNear" seems to be a wrong methog here
				effect.nearEnemies.add(worldStage.getLevelStream().getEnemiesNear(Constants.VIRTUAL_WIDTH).get(i));
				//System.out.println(worldStage.getLevelStream().getEnemiesNear(Constants.VIRTUAL_WIDTH).get(i).getX());
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
		Runner r = getRunner();
		effect.setX(r.pet.getX());
		effect.setY(r.pet.getY());
		worldStage.addActor(effect);
		r.pet.setVisible(false);
		worldStage.addEnemyAwareActor(effect);
		//Move to the first enemy
		Rectangle tempRect = new Rectangle();
		effect.nearEnemies.get(0).retrieveHitbox(tempRect);
		effect.addAction(Actions.moveTo(tempRect.x, tempRect.y, 0.5f, Interpolation.pow2));
	}

	/*
	private boolean isAffected(float effectXPosition, Actor actor) {
		return Math.abs(effectXPosition - actor.getX()) < Constants.VIRTUAL_WIDTH;
	}*/
}
