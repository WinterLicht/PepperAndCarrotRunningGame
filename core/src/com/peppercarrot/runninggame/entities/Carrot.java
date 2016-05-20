package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public class Carrot extends Pet {

	public Carrot(String name, Runner runner) {
		super(name, runner);
		
		// Load Animations
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
	public void act(float delta) {
		super.act(delta);
		// Decide which animation to display and
		// change his position (offset), so he
		// sits f.e on Pepper's broom.
		if (owner.isDoubleJumping()) {
			setX(-16);
			setY(67);
			doubleJumpingAnim.act(delta);
			petImage.setDrawable(doubleJumpingAnim.getDrawable());
		}
		if (owner.isJumping()) {
			setX(6);
			setY(28);
			jumpingAnim.act(delta);
			petImage.setDrawable(jumpingAnim.getDrawable());
		}
		if (owner.isRunnig()) {
			setX(-35);
			setY(0);
			runningAnim.act(delta);
			petImage.setDrawable(runningAnim.getDrawable());
		}
		if (owner.isFalling()) {
			setX(7);
			setY(65);
			fallingAnim.act(delta);
			petImage.setDrawable(fallingAnim.getDrawable());
		}
		
	}
}
