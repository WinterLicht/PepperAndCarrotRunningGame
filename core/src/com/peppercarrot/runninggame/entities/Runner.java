package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedImage;
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

		public HitPoints(int maxHp) {
			super();
			padLeft(10);
			points = maxHp;
			maxPoints = maxHp;
			for (int i = 0; i < maxHp; i++) {
				final Image heart = new Image(new TextureRegion(Assets.I.atlas.findRegion("heart")));
				this.add(heart).padTop(14);
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
					final Image heart = (Image) this.getChildren().items[prevDiff + i];
					final Color c = Color.DARK_GRAY;
					c.a = 0.68f;
					heart.setColor(c);
				}
			}
			// Heal
			if (difference > 0) {
				for (int i = 0; i < difference && prevDiff - i - 1 >= 0; i++) {
					final Image heart = (Image) this.getChildren().items[prevDiff - i - 1];
					heart.setColor(Color.WHITE);
				}
			}
		}

	}

	String name;
	boolean stunned = false;
	public State currState = State.RUNNING;
	public Pet pet;
	public HitPoints health;
	int speedY = 0;
	/** Vertical speed in pixel. */
	int maxJumpSpeed = 26;
	/** Maximum speed when jumping in pixel */

	public Image runnerImage;
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	AnimatedImage attackingAnim;
	AnimatedImage hitAnim;

	public Ability ability1;
	public Ability ability2;
	public Ability ability3;
	public Ability ability0;

	/**
	 * Possible states.
	 */
	enum State {
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING,
		// TODO: for this states may be an other animation
		ATTACK_RUNNING, ATTACK_FALLING, ATTACK_JUMPING, ATTACK_DOUBLEJUMPING, DYING;
	}

	public Runner(String name) {
		health = new HitPoints(5);
		this.name = name;
		runnerImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		addActor(runnerImage);
		setWidth(runnerImage.getWidth());
		setHeight(runnerImage.getHeight());
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
		default: // Should not be reached
			break;
		}
		hitAnim.act(delta);
		if (stunned) {
			runnerImage.setDrawable(hitAnim.getDrawable());
		}
		// Gravity is 1 pixel
		speedY -= 1;
		// Move down
		final float oldYPos = getY();
		// FIXME:Tunneling, see also processPlatforms in WorldStage
		// Currently: simply cap falling speed
		if (speedY > -maxJumpSpeed) {
			setY(getY() + speedY);
		} else {
			// No infinite acceleration
			setY(getY() - maxJumpSpeed);
		}
		//pet.updatePosition(delta);
		if (getY() < oldYPos) {
			// Player is falling, if his y-position is lowered
			// and he was previously running.
			setFalling();
		}
		// Player can't fall under/below the ground
		if (getY() < Constants.OFFSET_TO_GROUND) {
			land(Constants.OFFSET_TO_GROUND);
		}
	}

	// Helper methods for states
	public void setRunnig() {
		if (isAttacking())
			currState = State.ATTACK_RUNNING;
		else
			currState = State.RUNNING;
		pet.setRunnig();
	}

	public void setFalling() {
		if (isAttacking())
			currState = State.ATTACK_FALLING;
		else
			currState = State.FALLING;
		pet.setFalling();
	}

	public void setJumping() {
		if (isAttacking())
			currState = State.ATTACK_JUMPING;
		else
			currState = State.JUMPING;
		pet.setJumping();
	}

	public void setDoubleJumping() {
		if (isAttacking())
			currState = State.ATTACK_DOUBLEJUMPING;
		else
			currState = State.DOUBLEJUMPING;
		pet.setDoubleJumping();
	}

	public void setDying() {
		currState = State.DYING;
		pet.setDying();
	}

	/**
	 * resets also attacking animation.
	 */
	public void setAttacking() {
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
		pet.setStunned();
	}

	@Override
	/**
	 * Slightly smaller hitbox of the player as his sprite.
	 */
	public void retrieveHitbox(Rectangle rectangle) {
		final int offsetX = 60;
		final int offsetY = 20;
		rectangle.x = getX() + offsetX;
		rectangle.y = getY() + offsetY;
		rectangle.width = runnerImage.getWidth() - offsetX * 2;
		rectangle.height = runnerImage.getHeight() - offsetY * 2;
	}

	@Override
	public boolean onHitPotion(Potion potion) {
		if (potion.isVisible()) {
			potion.collected();
			switch (potion.type) {
			case ORANGE:
				ability1.increaseEnergy(1);
				break;
			case GREEN:
				ability2.increaseEnergy(1);
				break;
			case BLUE:
				ability3.increaseEnergy(1);
				break;
			default:
				System.out.println("for this potion is nothing defined.");
				break;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onHitEnemy(Enemy enemy) {
		if (enemy.isAlive()) {
			setStunned();
			health.updateHP(-enemy.damage);
			enemy.die();
			if (health.points <= 0) {
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
		final float offset = 16;
		land(platformHitTop - offset);
		return true;
	}
}
