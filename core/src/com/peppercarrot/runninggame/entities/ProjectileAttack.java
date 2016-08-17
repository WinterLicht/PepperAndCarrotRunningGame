package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.entities.Runner.State;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;

/**
 * WIP projectile attack. Currently: proj. moving in sinusoids.
 * Spread angle, amount, speed etc. can be adjusted.
 * 
 * @author WinterLicht
 *
 */
public class ProjectileAttack extends Ability {

	public static class Projectile extends Group implements IEnemyCollisionAwareActor {
		boolean active = false;
		//testing sinusoids movement... TODO: all in constructor or all public!
		int interval = 28; // Interval in pixel for up-down of sinusoids
		private boolean moveUp = true; // if projectile is moving up initially
		Vector2 origin = new Vector2(); // original position
		private Vector2 positionNotRot = new Vector2(); // Position, if direction would not be rotated 
		float angle; // Rotation of movement-direction vector
		//TODO: maybe range of attack?

		public Projectile(float angle) {
			Image image1 = new Image(new TextureRegion(Assets.I.atlas.findRegion("projectile")));
			image1.setOrigin(Align.center);
			setHeight(image1.getHeight());
			setWidth(image1.getWidth());
			addActor(image1);
			this.angle = angle;
			setOrigin(Align.center);
			addAction(Actions.forever(Actions.rotateBy(360f, 1.8f)));
		}

		@Override
		public void retrieveHitbox(Rectangle rectangle) {
			CollisionUtil.retrieveHitbox(this, rectangle);
		}

		@Override
		public boolean onHitEnemy(Enemy enemy) {
			if (enemy.isAlive() && active && !enemy.indestructible) {
				Account.I.huntEnemies += 1;
				enemy.die();
				active = false;
				setVisible(false);
			}
			return false;
		}
		
		@Override
		public void act(float delta) {
			super.act(delta);
			if(active) {
				// Firstly, calculate position of the projectile
				// as if it has not rotated moving direction
				positionNotRot.x = positionNotRot.x+8; //TODO: 8 as attribute
				int speedY = 4;
				if (moveUp) {
					positionNotRot.y = positionNotRot.y+speedY;
				}else{
					positionNotRot.y = positionNotRot.y-speedY;
				}
				if (origin.y+interval <= positionNotRot.y && moveUp) {
					positionNotRot.y = positionNotRot.y-speedY;
					moveUp = false;
				}
				if (origin.y-interval >= positionNotRot.y && !moveUp) {
					positionNotRot.y = positionNotRot.y+speedY;
					moveUp = true;
				}
				// Rotate calculated position
				Vector2 v = rotatePoint(positionNotRot, origin, angle);
				setPosition(v.x, v.y);
			}
		}

		public void startMoving() {
			active = true;
			moveUp = (angle < 0); // TODO: random?
			//..
			origin.set(getX(), getY());
			positionNotRot.set(getX(), getY());
		}

		/**
		 * Rotate one point "point" around the other point "rotationCenter" by an given angle.
		 * 
		 * @param point
		 * @param rotationCenter
		 * @param angle
		 * @return Rotated point
		 */
		private Vector2 rotatePoint(Vector2 point, Vector2 rotationCenter, float angle){
            angle = (float) (angle * (Math.PI/180f));
            float rotatedX = (float) (Math.cos(angle) * (point.x - rotationCenter.x) - Math.sin(angle) * (point.y-rotationCenter.y) + rotationCenter.x);
            float rotatedY = (float) (Math.sin(angle) * (point.x - rotationCenter.x) + Math.cos(angle) * (point.y - rotationCenter.y) + rotationCenter.y);
            return new Vector2(rotatedX,rotatedY);
        }
	}

	private final Group projectiles = new Group(); // All projectiles stored here.
	private int amount = 3; //TODO: in constructor!
	WorldStage worldStage;
	float spread = 40; //Spread angle. Make it 0 and amount > 1, and get projectile percing "amount"-times


	public ProjectileAttack(Runner runner, int maxEnergy, float duration) {
		super(runner, maxEnergy, duration);

		for (int i = 0; i < amount; i++) {
			// Distribute amount of projectiles inside the spread
			Projectile effect = new Projectile(spread-i*(2*spread/(amount-1)));
			effect.setVisible(false);
			projectiles.addActor(effect);
		}
	}

	@Override
	protected void internalUpdate(float delta) {
	}

	@Override
	protected void finish() {
		for (Actor projectile : projectiles.getChildren()) {
			worldStage.removeEnemyAwareActor((IEnemyCollisionAwareActor) projectile);
			((Projectile)projectile).active = false;
		}
		setAllProjVisible(false);
		final Runner runner = getRunner();

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

	@Override
	protected void execute(WorldStage worldStage) {
		this.worldStage = worldStage;
		final Runner runner = getRunner();
		runner.setAttacking(); //TODO: reset attack-Animation earlier
		//runner.addActor(projectiles);
		worldStage.addActor(projectiles);
		setAllProjVisible(true);
		for (Actor projectile : projectiles.getChildren()) {
			// Reset position of projectiles
			projectile.setX(runner.runnerImage.getWidth()/2+runner.getX());
			projectile.setY(runner.runnerImage.getHeight()/2+runner.getY());
			((Projectile) projectile).startMoving();
			worldStage.addEnemyAwareActor((IEnemyCollisionAwareActor) projectile);
		}
	}

	/**
	 * Calls setVisible on every projectile.
	 * 
	 * @param visibility
	 */
	public void setAllProjVisible(boolean visibility) {
		for (Actor projectile : projectiles.getChildren()) {
			projectile.setVisible(visibility);
		}
	}
}
