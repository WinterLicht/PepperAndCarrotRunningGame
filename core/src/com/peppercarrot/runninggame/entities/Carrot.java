package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Peppers pet. Has no functions except of graphical
 * representation. By casting Carrot's abilities, set this
 * invisible and use instead the Ability-class. 
 * 
 * @author WinterLicht
 *
 */
public class Carrot extends Pet {

	public Carrot(String name, Runner runner) {
		super(name, runner);
		initAnimations();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		this.updatePosition(delta);
	}
	
	@Override
	public void updatePosition(float delta) {
		// Change his position (offset), so he
		// sits f.e on Pepper's broom.
		switch (currState) {
		case DOUBLEJUMPING:
			this.setSize(doubleJumpingAnim.getWidth()*owner.getScaleFactor(), doubleJumpingAnim.getHeight()*owner.getScaleFactor());
			this.setDrawable(doubleJumpingAnim.getDrawable());
			setX(-12*owner.getScaleFactor());
			setY(140*owner.getScaleFactor());
			doubleJumpingAnim.act(delta);
			break;
		case DYING:
			this.setSize(hitAnim.getWidth()*owner.getScaleFactor(), hitAnim.getHeight()*owner.getScaleFactor());
			this.setDrawable(hitAnim.getDrawable());
			setX(-34*owner.getScaleFactor());
			setY(-4*owner.getScaleFactor());
			hitAnim.act(delta);
			break;
		case JUMPING:
			this.setSize(jumpingAnim.getWidth()*owner.getScaleFactor(), jumpingAnim.getHeight()*owner.getScaleFactor());
			this.setDrawable(jumpingAnim.getDrawable());
			setY(49*owner.getScaleFactor());
			setX(0);
			jumpingAnim.act(delta);
			break;
		case RUNNING:
			this.setSize(runningAnim.getWidth()*owner.getScaleFactor(), runningAnim.getHeight()*owner.getScaleFactor());
			this.setDrawable(runningAnim.getDrawable());
			setX(-100*owner.getScaleFactor());
			setY(-4*owner.getScaleFactor());
			runningAnim.act(delta);
			break;
		case FALLING:
			this.setSize(fallingAnim.getWidth()*owner.getScaleFactor(), fallingAnim.getHeight()*owner.getScaleFactor());
			this.setDrawable(fallingAnim.getDrawable());
			setX(48*owner.getScaleFactor());
			setY(123*owner.getScaleFactor());
			fallingAnim.act(delta);
			break;
		case HIT:
			setX(-34*owner.getScaleFactor());
			setY(-4*owner.getScaleFactor());
			hitAnim.act(delta);
			break;
		case IDLE:
			this.setSize(idleAnim.getWidth()*owner.getScaleFactor(), idleAnim.getHeight()*owner.getScaleFactor());
			setX(-34*owner.getScaleFactor());
			setY(-4*owner.getScaleFactor());
			prozessIdleAnimation(delta);
			break;
		default: // Should not be reached
			break;
		}
	}

	@Override
	public void initAnimations() {
		runningAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.059f, Assets.I.getRegions(name + "_run"), Animation.PlayMode.LOOP)));
		runningAnim.setOrigin(Align.center);
		jumpingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.144f, Assets.I.getRegions(name + "_jump"), Animation.PlayMode.LOOP_PINGPONG)));
		jumpingAnim.setOrigin(Align.center);
		doubleJumpingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.164f, Assets.I.getRegions(name + "_doublejump"), Animation.PlayMode.LOOP_PINGPONG)));
		doubleJumpingAnim.setOrigin(Align.center);
		fallingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.18f, Assets.I.getRegions(name + "_fall"), Animation.PlayMode.LOOP_PINGPONG)));
		fallingAnim.setOrigin(Align.center);
		hitAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.18f, Assets.I.getRegions(name + "_hit"), Animation.PlayMode.NORMAL)));
		hitAnim.setOrigin(Align.center);
		idleAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.18f, Assets.I.getRegions(name + "_idle"), Animation.PlayMode.LOOP_PINGPONG)));
		//TODO: Carrot needs animation when Pepper attacks
	}

	@Override
	public void land() {
		currState = State.RUNNING;
	}

	@Override
	public void setRunnig() {
		currState = State.RUNNING;	
	}

	@Override
	public void setFalling() {
		currState = State.FALLING;
	}

	@Override
	public void setJumping() {
		currState = State.JUMPING;
	}

	@Override
	public void setDoubleJumping() {
		currState = State.DOUBLEJUMPING;
	}

	@Override
	public void setDying() {
		currState = State.DYING;
	}

	public void setStunned() {
		currState = State.HIT;
		this.setSize(hitAnim.getWidth()*owner.getScaleFactor(), hitAnim.getHeight()*owner.getScaleFactor());
		this.setDrawable(hitAnim.getDrawable());
	}
}
