package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.entities.Potion.Type;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.LevelSegment;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;
import com.peppercarrot.runninggame.world.collision.IPlatformCollisionAwareActor;
import com.peppercarrot.runninggame.world.collision.IPotionCollisionAwareActor;

/**
 * Playable character class. The runner is only able to move horizontally, other
 * entities are moving towards player. Runner can jump and doublejump.
 * 
 * @author WinterLicht
 *
 */
public abstract class Runner extends Group
		implements IPlatformCollisionAwareActor, IEnemyCollisionAwareActor, IPotionCollisionAwareActor {

	/**
	 * For UI.
	 * 
	 */
	public class HitPoints extends Table {
		public int points;
		public int maxPoints;
		Image heart;
		Image heartDisabled;

		public HitPoints(int maxHp) {
			super();
			padLeft(10);
			points = maxHp;
			maxPoints = maxHp;
			for (int i = 0; i < maxHp; i++) {
				heart = new Image(new TextureRegion(Assets.I.atlas.findRegion("heart")));
				heartDisabled = new Image(new TextureRegion(Assets.I.atlas.findRegion("heart-disabled")));
				Image im = new Image(new TextureRegion(Assets.I.atlas.findRegion("heart")));
				this.add(im).padTop(14);
				this.row();
			}
		}

		/**
		 * Negative when damage should be received.
		 * 
		 * @param difference
		 */
		public void updateHP(int difference) {
			final int prevDiff = maxPoints - points; // already lost points
			points += difference;
			if (points < 0)
				points = 0;
			if (points > maxPoints)
				points = maxPoints;
			// Damage
			if (difference < 0) {
				for (int i = 0; i < -difference && prevDiff + i < maxPoints; i++) {
					Image im = (Image) this.getChildren().items[prevDiff + i];
					im.setDrawable(heartDisabled.getDrawable());
					im.setColor(1f, 1f, 1f, 0.6f);
					/*
					final Image heart = (Image) this.getChildren().items[prevDiff + i];
					final Color c = Color.DARK_GRAY;
					c.a = 0.68f;
					heart.setColor(c);
					*/
				}
			}
			// Heal
			if (difference > 0) {
				for (int i = 0; i < difference && prevDiff - i - 1 >= 0; i++) {
					Image im = (Image) this.getChildren().items[prevDiff - i - 1];
					im.setDrawable(heart.getDrawable());
					im.setColor(1f, 1f, 1f, 1f);
					/*
					final Image heart = (Image) this.getChildren().items[prevDiff - i - 1];
					heart.setColor(Color.WHITE);
					*/
				}
			}
		}

	}

	String name;
	private float scaleFactor = 0.62f; //In-game scale applied to original sprite
	public boolean stunned = false;
	public boolean noGravity = false;
	public State currState = State.RUNNING;
	public Pet pet;
	public HitPoints health;
	int speedY = 0;
	/** Vertical speed in pixel. */
	int maxJumpSpeed = 26;
	/** Maximum speed when jumping in pixel */

	public Image runnerImage;
	public AnimatedImage runningAnim;
	public AnimatedImage jumpingAnim;
	public AnimatedImage doubleJumpingAnim;
	public AnimatedImage fallingAnim;
	public AnimatedImage attackingAnim;
	public AnimatedImage hitAnim;
	public AnimatedImage idleAnim;

	public Ability ability1;
	public Ability ability2;
	public Ability ability3;
	public Ability ability0;

	private int offsetForFeet = 16; //Offset so player lands with feet on the platform ground

	/**
	 * Possible states.
	 */
	public enum State {
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING,
		// TODO: for this states may be an other animation
		ATTACK_RUNNING, ATTACK_FALLING, ATTACK_JUMPING, ATTACK_DOUBLEJUMPING, DYING,
		IDLE;
	}

	public Runner(String name) {
		health = new HitPoints(5);
		this.name = name;
		runnerImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		addActor(runnerImage);
		//Scale down image, image on original size is needed for preview
		setSize(runnerImage.getWidth()*scaleFactor, runnerImage.getHeight()*scaleFactor);
		runnerImage.setSize(runnerImage.getWidth()*scaleFactor, runnerImage.getHeight()*scaleFactor);
		runnerImage.setOrigin(Align.center);
		initAbilities();
		initAnimations();
		initPet();
		// Runner is always placed with some offset
		setOrigin(Align.center);
		setX(Constants.OFFSET_TO_EDGE);
		setY(Constants.OFFSET_TO_GROUND);
	}

	protected abstract void initAbilities();

	protected abstract void initAnimations();

	protected abstract void initPet();

	public void jump() {
		if (isRunnig()) {
			Account.I.jumps += 1;
			setJumping();
			speedY = maxJumpSpeed;
		} else if (isJumping()) {
			setDoubleJumping();
			speedY = maxJumpSpeed;
		}
	}

	/**
	 * Land on given y-coordinate position.
	 * 
	 * @param y
	 */
	public void land(float y) {
		// Player lands only if his speed is small enough
		final int speedOffset = 8;
		if (speedY < speedOffset) {
			setY(y);
			speedY = 0;
			setRunnig();
			pet.land();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// Update all attacks
		ability1.update(delta);
		ability2.update(delta);
		ability3.update(delta);
		ability0.update(delta);
		// Decide which animation is displayed
		switch (currState) {
		case DOUBLEJUMPING:
			doubleJumpingAnim.act(delta);
			runnerImage.setDrawable(doubleJumpingAnim.getDrawable());
			break;
		case DYING:
			hitAnim.act(delta);
			runnerImage.setDrawable(hitAnim.getDrawable());
			break;
		case JUMPING:
			jumpingAnim.act(delta);
			runnerImage.setDrawable(jumpingAnim.getDrawable());
			break;
		case RUNNING:
			runningAnim.act(delta);
			runnerImage.setDrawable(runningAnim.getDrawable());
			break;
		case FALLING:
			fallingAnim.act(delta);
			runnerImage.setDrawable(fallingAnim.getDrawable());
			break;
		case ATTACK_RUNNING:
			attackingAnim.act(delta);
			runnerImage.setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_JUMPING:
			attackingAnim.act(delta);
			runnerImage.setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_DOUBLEJUMPING:
			attackingAnim.act(delta);
			runnerImage.setDrawable(attackingAnim.getDrawable());
			break;
		case ATTACK_FALLING:
			attackingAnim.act(delta);
			runnerImage.setDrawable(attackingAnim.getDrawable());
			break;
		case IDLE:
			prozessIdleAnimation(delta);
			break;
		default:
			System.out.println("Runner state: should not be reached");
			break;
		}
		pet.updateState(currState);
		if (stunned) {
			hitAnim.act(delta);
			runnerImage.setDrawable(hitAnim.getDrawable());
			pet.setStunned();
		}
		final float oldYPos = getY();
		if (!noGravity) {
			// Gravity is 1 pixel
			speedY -= 1;
			// Move down
			// FIXME:Tunneling, see also processPlatforms in WorldStage
			// Currently: simply cap falling speed
			if (speedY > -maxJumpSpeed) {
				setY(getY() + speedY);
			} else {
				// No infinite acceleration
				setY(getY() - maxJumpSpeed);
			}
		}
		if (getY() < oldYPos) {
			// Player is falling, if his y-position is lowered
			// and he was previously running.
			setFalling();
		}
		if (!noGravity) {
			// Player can't fall under/below the ground
			if (getY() < (Constants.OFFSET_TO_GROUND)) {
				land(Constants.OFFSET_TO_GROUND);
			}
		}
	}

	// Helper methods for states
	public void setRunnig() {
		if (currState == State.DYING) return;
		if (isAttacking())
			currState = State.ATTACK_RUNNING;
		else
			currState = State.RUNNING;
	}

	public void setFalling() {
		if (currState == State.DYING) return;
		if (isAttacking())
			currState = State.ATTACK_FALLING;
		else
			currState = State.FALLING;
	}

	public void setJumping() {
		if (currState == State.DYING) return;
		if (isAttacking())
			currState = State.ATTACK_JUMPING;
		else
			currState = State.JUMPING;
	}

	public void setDoubleJumping() {
		if (currState == State.DYING) return;
		if (isAttacking())
			currState = State.ATTACK_DOUBLEJUMPING;
		else
			currState = State.DOUBLEJUMPING;
	}

	public void setDying() {
		currState = State.DYING;
	}

	/**
	 * resets also attacking animation.
	 */
	public void setAttacking() {
		if (currState == State.DYING) return;
		attackingAnim.reset();
		switch (currState) {
		case DOUBLEJUMPING:
			currState = State.ATTACK_DOUBLEJUMPING;
			break;
		case FALLING:
			currState = State.ATTACK_FALLING;
			break;
		case JUMPING:
			currState = State.ATTACK_JUMPING;
			break;
		case RUNNING:
			currState = State.ATTACK_RUNNING;
			break;
		default:
			break;
		}
	}

	public boolean isAttacking() {
		return (currState == State.ATTACK_DOUBLEJUMPING || currState == State.ATTACK_RUNNING
				|| currState == State.ATTACK_FALLING || currState == State.ATTACK_JUMPING);
	}

	public boolean isRunnig() {
		return (currState == State.RUNNING || currState == State.ATTACK_RUNNING);
	}

	public boolean isFalling() {
		return (currState == State.FALLING || currState == State.ATTACK_FALLING);
	}

	public boolean isJumping() {
		return (currState == State.JUMPING || currState == State.ATTACK_JUMPING);
	}

	public boolean isDoubleJumping() {
		return (currState == State.DOUBLEJUMPING || currState == State.ATTACK_DOUBLEJUMPING);
	}

	public boolean isDying() {
		return (currState == State.DYING);
	}

	public State getCurrentState() {
		return currState;
	}

	public void setState(State state) {
		currState = state;
	}

	public void setScaleFactor(float newScaleFactor) {
		this.scaleFactor = newScaleFactor;
		float currentW = runningAnim.getCopyOfAnimation().getKeyFrame(0).getRegionWidth();
		float currentH = runningAnim.getCopyOfAnimation().getKeyFrame(0).getRegionHeight();
		setSize(currentW*scaleFactor, currentH*scaleFactor);
		runnerImage.setSize(currentW*scaleFactor, currentH*scaleFactor);
	}

	public float getScaleFactor() {
		return scaleFactor;
	}

	public void setStunned() {
		final SequenceAction stunAction = new SequenceAction();
		// TODO: stun duration depending on enemy/collider object
		stunAction.addAction(Actions.delay(0.2f));
		stunAction.addAction(Actions.run(new Runnable() {
			@Override
			public void run() {
				if (stunned)
					stunned = false;
			}
		}));
		stunned = true;
		hitAnim.reset();
		hitAnim.clearActions();
		hitAnim.addAction(stunAction);
	}

	@Override
	/**
	 * Slightly smaller hitbox of the player as his sprite.
	 */
	public void retrieveHitbox(Rectangle rectangle) {
		final int offsetX = 60;
		final int offsetY = 14;
		rectangle.x = getX() + offsetX;
		rectangle.y = getY() + offsetY;
		rectangle.width = runnerImage.getWidth() - offsetX * 2;
		rectangle.height = runnerImage.getHeight() - offsetY * 2;
	}

	@Override
	public boolean onHitPotion(Potion potion) {
		if (potion.isVisible()) {
			potion.collected();
			if (potion.type == Type.POTION) {
				Account.I.collectedPotions += 1;
				switch (potion.getName()) {
				case "orange":
					ability1.increaseEnergy(1);
					break;
				case "green":
					ability2.increaseEnergy(1);
					break;
				case "blue":
					ability3.increaseEnergy(1);
					break;
				case "pink":
					health.updateHP(+1);
					break;
				default:
					System.out.println("for this potion is nothing defined.");
					break;
				}
				return true;
			} else if (potion.type == Type.INGREDIENT) {
				Account.I.currentlyCollectedIngredients.add(potion.getName());
			}
		}
		return false;
	}

	@Override
	public boolean onHitEnemy(Enemy enemy) {
		if (enemy.isAlive() && !enemy.alreadyCollidedWPlayer) {
			Account.I.nimbleness = false;
			setStunned();
			health.updateHP(-enemy.damage);
			//Enemy can collide with player only once
			enemy.alreadyCollidedWPlayer = true;
			if (health.points <= 0) {
				Account.I.died += 1;
				setDying();
			}
			return true;
		}
		return false;
	}

	@Override
	public float getPlatformCollisionX() {
		return getX();
	}

	@Override
	public float getPlatformCollisionY() {
		return getY();
	}

	@Override
	public float getPlatformCollisionWidth() {
		return runnerImage.getWidth();
	}

	@Override
	public boolean onHitPlatform(LevelSegment.Platform platform, float platformHitTop) {
		// Offset so player lands with feet on the platform ground
		land(platformHitTop - offsetForFeet);
		return true;
	}

	/**
	 * Helper for idle-animation.
	 * @param delta
	 */
	private void prozessIdleAnimation(float delta) {
		idleAnim.act(delta);		
		if (idleAnim.drawable.getCurrentKeyFrameIndex() == 0
				|| idleAnim.drawable.getCurrentKeyFrameIndex() == idleAnim.drawable.animation.getKeyFrames().length-1) {
			double r = Math.random();
			if (r < 0.07 && idleAnim.drawable.isPaused()) {
				idleAnim.drawable.continuePlay();
			} else {
				idleAnim.drawable.pause();
			}
		}
		runnerImage.setDrawable(idleAnim.getDrawable());
	}
}
