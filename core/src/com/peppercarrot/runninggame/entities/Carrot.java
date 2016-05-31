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
	public void updatePosition(float delta) {
		// Change his position (offset), so he
		// sits f.e on Pepper's broom.
		switch (currState) {
		case DOUBLEJUMPING:
			setX(owner.getX()-16);
			setY(owner.getY()+67);
			doubleJumpingAnim.act(delta);
			break;
		case DYING:
			break;
		case JUMPING:
			setX(owner.getX()+6);
			setY(owner.getY()+28);
			jumpingAnim.act(delta);
			break;
		case RUNNING:
			setX(owner.getX()-35);
			setY(owner.getY()+0);
			runningAnim.act(delta);
			break;
		case FALLING:
			setX(owner.getX()+7);
			setY(owner.getY()+65);
			fallingAnim.act(delta);
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
		//TODO: Carrot needs animation when Pepper attacks
	}

	@Override
	public void land() {
		currState = State.RUNNING;
		petImage.setDrawable(runningAnim.getDrawable());
	}

	@Override
	public void setRunnig() {
		currState = State.RUNNING;
		petImage.setDrawable(runningAnim.getDrawable());	
	}

	@Override
	public void setFalling() {
		currState = State.FALLING;
		petImage.setDrawable(fallingAnim.getDrawable());
	}

	@Override
	public void setJumping() {
		currState = State.JUMPING;
		petImage.setDrawable(jumpingAnim.getDrawable());
	}

	@Override
	public void setDoubleJumping() {
		currState = State.DOUBLEJUMPING;
		petImage.setDrawable(doubleJumpingAnim.getDrawable());
	}

	@Override
	public void setDying() {
		currState = State.DYING;
	}

}
