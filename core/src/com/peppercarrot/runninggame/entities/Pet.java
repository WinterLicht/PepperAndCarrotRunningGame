package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Abstract class for a pet of the witch. Consider updating graphics
 * and position relative to witch and her states.
 * 
 * @author WinterLicht
 *
 */
public abstract class Pet extends Image {

	public State currState = State.RUNNING;
	String name;
	Runner owner;
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	AnimatedImage hitAnim;
	AnimatedImage idleAnim;
	
	/**
	 * Possible states.
	 */
	enum State {
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING, DYING, HIT, IDLE;
	}
	
	public Pet(String name, Runner runner) {
		super(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		this.name = name;
		owner = runner;
		runner.addActor(this);
		setOrigin(Align.center);
	}

	public void updateState(Runner.State runnerState) {
		switch (runnerState) {
		case ATTACK_DOUBLEJUMPING:
			currState = State.DOUBLEJUMPING;
			break;
		case ATTACK_FALLING:
			currState = State.FALLING;
			break;
		case ATTACK_JUMPING:
			currState = State.JUMPING;
			break;
		case ATTACK_RUNNING:
			currState = State.RUNNING;
			break;
		case DOUBLEJUMPING:
			currState = State.DOUBLEJUMPING;
			break;
		case DYING:
			currState = State.DYING;
			break;
		case FALLING:
			currState = State.FALLING;
			break;
		case JUMPING:
			currState = State.JUMPING;
			break;
		case RUNNING:
			currState = State.RUNNING;
			break;
		case IDLE:
			currState = State.IDLE;
			break;
		default:
			break;
		}
	}

	public abstract void initAnimations();
	public abstract void updatePosition(float delta);
	public abstract void land();
	public abstract void setRunnig();
	public abstract void setFalling();
	public abstract void setJumping();
	public abstract void setDoubleJumping();
	public abstract void setDying();
	public abstract void setStunned();

	/**
	 * Helper for idle-animation.
	 * @param delta
	 */
	protected void prozessIdleAnimation(float delta) {
		idleAnim.act(delta);		
		if (idleAnim.drawable.getCurrentKeyFrameIndex() == 0
				|| idleAnim.drawable.getCurrentKeyFrameIndex() == idleAnim.drawable.animation.getKeyFrames().length-1) {
			double r = Math.random();
			if (r < 0.05 && idleAnim.drawable.isPaused()) {
				idleAnim.drawable.continuePlay();
			} else {
				idleAnim.drawable.pause();
			}
		}
		this.setDrawable(idleAnim.getDrawable());
	}

}
