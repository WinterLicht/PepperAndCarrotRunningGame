package com.peppercarrot.runninggame.entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.LevelStream;
import com.peppercarrot.runninggame.world.Platform;

/**
 * Playable character class. The runner is only able to move horizontally, other
 * entities are moving towards player. Runner can jump and doublejump.
 * 
 * @author WinterLicht
 *
 */
public class Runner extends Group {
	String name;
	public State currState = State.RUNNING;
	int speedY = 0;
	/** Vertical speed in pixel. */
	int maxJumpSpeed = 24;
	/** Maximum speed when jumping in pixel */

	Image runnerImage;
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	AnimatedImage attackingAnim; // TODO: various animations for various
									// attacks?

	public SweepAttack ability1;
	/** Simple attack. */
	public BlackHole ability2;
	/** Remove game entities. */
	public TimeDistortion ability3;

	/** Slow down level scroll speed. */

	/**
	 * Possible states.
	 */
	enum State {
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING,
		// TODO: for this states may be an other animation
		ATTACK_RUNNING, ATTACK_FALLING, ATTACK_JUMPING, ATTACK_DOUBLEJUMPING, DYING;
	}

	public Runner(String name) {
		this.name = name;
		runnerImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		addActor(runnerImage);
		ability1 = new SweepAttack(this);
		ability2 = new BlackHole(this);
		ability3 = new TimeDistortion(this);
		// Runner is always placed with some offset
		setOrigin(Align.center);
		setX(Constants.OFFSET_TO_EDGE);
		setY(Constants.OFFSET_TO_GROUND);
		// Load Animations
		runningAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.079f, Assets.I.getRegions(name + "_run"), Animation.PlayMode.LOOP)));
		runningAnim.setOrigin(Align.center);
		jumpingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.144f, Assets.I.getRegions(name + "_jump"), Animation.PlayMode.LOOP_PINGPONG)));
		jumpingAnim.setOrigin(Align.center);
		doubleJumpingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.144f, Assets.I.getRegions(name + "_doublejump"), Animation.PlayMode.LOOP_PINGPONG)));
		doubleJumpingAnim.setOrigin(Align.center);
		fallingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.14f, Assets.I.getRegions(name + "_fall"), Animation.PlayMode.LOOP_PINGPONG)));
		fallingAnim.setOrigin(Align.center);

		attackingAnim = new AnimatedImage(new AnimatedDrawable(new Animation(ability1.getDuration() / 8,
				Assets.I.getRegions(name + "_attack"), Animation.PlayMode.NORMAL)));
		attackingAnim.setOrigin(Align.center);
	}

	public void jump() {
		if (isRunnig()) {
			setJumping();
			speedY = maxJumpSpeed;
			return;
		}
		if (isJumping()) {
			setDoubleJumping();
			speedY = maxJumpSpeed;
			return;
		}
	}

	public void applyCollision(LevelStream levelStream) {
		applyPlatformCollision(levelStream.getPlatformsNear(getX()));

		final Rectangle hitbox = getHitBox();
		applyEnemyCollision(hitbox, levelStream.getEnemiesNear(getX()));
		applyPotionCollision(hitbox, levelStream.getPotionsNear(getX()));
	}

	private void applyPlatformCollision(List<Platform> platforms) {
		// Offset in pixel
		final int offsetTop = 12;
		final int offsetBottom = 10;
		final Vector2 platformPosition = new Vector2();
		final float x = getX();
		final float y = getY();
		for (final Platform platform : platforms) {
			platform.retrieveAbsolutePosition(platformPosition);
			if (platformPosition.x >= x) {
				break;
			}

			if (platformPosition.x <= x && x <= platformPosition.x + platform.getW()) {
				final float platformTop = platformPosition.y + platform.getH();
				if (platformTop - offsetBottom <= y && y <= platformTop + offsetTop) {
					land(platformTop);
					break;
				}
			}
		}
	}

	private void applyEnemyCollision(Rectangle hitbox, List<Enemy> enemies) {
		final Rectangle enemyRectangle = new Rectangle();
		for (final Enemy enemy : enemies) {
			enemy.retrieveRectangle(enemyRectangle);
			if (enemy.isAlive() && hitbox.overlaps(enemyRectangle)) {
				setDying();
			}
		}
	}

	private void applyPotionCollision(Rectangle hitbox, List<Potion> potions) {
		final Rectangle potionRectangle = new Rectangle();
		for (final Potion potion : potions) {
			potion.retrieveRectangle(potionRectangle);
			if (potion.isVisible() && hitbox.overlaps(potionRectangle)) {
				potion.collected();
				// TODO various potions
				ability1.increaseEnergy(1);
				ability2.increaseEnergy(1);
				ability3.increaseEnergy(1);
			}
		}
	}

	private Rectangle getHitBox() {
		final int offset = 30;

		// slightly smaller hitbox of the player as his sprite.
		final Rectangle hitBox = new Rectangle(getX() + offset, getY() + offset, getWidth() - offset * 2,
				getHeight() - offset * 2);
		return hitBox;
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
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// Update all attacks
		ability1.update(delta);
		ability2.update(delta);
		ability3.update(delta);
		// Decide which animation is displayed
		switch (currState) {
		case DOUBLEJUMPING:
			doubleJumpingAnim.act(delta);
			runnerImage.setDrawable(doubleJumpingAnim.getDrawable());
			break;
		case DYING:
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
		// Gravity is 1 pixel
		speedY -= 1;
		// Move
		final float oldYPos = getY();
		setY(getY() + speedY);
		if (getY() < oldYPos && !isAttacking()) {
			// Player is falling, if his y-position is lowered
			// and he was previously running.
			setFalling();
		}
		// Player can't fall under/below the ground
		if (getY() < Constants.OFFSET_TO_GROUND) { // land
			setRunnig();
			setY(Constants.OFFSET_TO_GROUND);
		}
	}

	// Helper methods for states
	public void setRunnig() {
		if (isAttacking())
			currState = State.ATTACK_RUNNING;
		else
			currState = State.RUNNING;
	}

	public void setFalling() {
		if (isAttacking())
			currState = State.ATTACK_FALLING;
		else
			currState = State.FALLING;
	}

	public void setJumping() {
		if (isAttacking())
			currState = State.ATTACK_JUMPING;
		else
			currState = State.JUMPING;
	}

	public void setDoubleJumping() {
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

	private float getCollisionBottom() {
		return getY();
	}

	public State getCurrentState() {
		return currState;
	}

	public void setState(State state) {
		currState = state;
	}
}
