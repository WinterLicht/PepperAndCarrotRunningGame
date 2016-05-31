package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
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
public abstract class Pet extends Group {

	public State currState = State.RUNNING;
	String name;
	Runner owner;
	Image petImage;
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	
	/**
	 * Possible states.
	 */
	enum State {
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING, DYING;
	}
	
	public Pet(String name, Runner runner) {
		this.name = name;
		owner = runner;
		petImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		addActor(petImage);
		setOrigin(Align.center);
	}

	public abstract void initAnimations();
	public abstract void updatePosition(float delta);
	public abstract void land();
	public abstract void setRunnig();
	public abstract void setFalling();
	public abstract void setJumping();
	public abstract void setDoubleJumping();
	public abstract void setDying();

}
