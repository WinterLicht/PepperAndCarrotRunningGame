package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public class Carrot extends Pet {

	public Carrot(String name, Runner runner) {
		super(name, runner);
		initAnimations();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// Decide which animation to display and
		// change his position (offset), so he
		// sits f.e on Pepper's broom.
		if (owner.isDoubleJumping()) {
			setX(owner.getX()-16);
			setY(owner.getY()+67);
			doubleJumpingAnim.act(delta);
			petImage.setDrawable(doubleJumpingAnim.getDrawable());
		}
		if (owner.isJumping()) {
			setX(owner.getX()+6);
			setY(owner.getY()+28);
			jumpingAnim.act(delta);
			petImage.setDrawable(jumpingAnim.getDrawable());
		}
		if (owner.isRunnig()) {
			setX(owner.getX()-35);
			setY(owner.getY()+0);
			runningAnim.act(delta);
			petImage.setDrawable(runningAnim.getDrawable());
		}
		if (owner.isFalling()) {
			setX(owner.getX()+7);
			setY(owner.getY()+65);
			fallingAnim.act(delta);
			petImage.setDrawable(fallingAnim.getDrawable());
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

}
