package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public abstract class Pet extends Group {

	//maybe needs state...later
	//public State currState = State.RUNNING;
	Runner owner;
	Image petImage;
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;
	AnimatedImage doubleJumpingAnim;
	AnimatedImage fallingAnim;
	
	public Pet(String name, Runner runner) {
		owner = runner;
		petImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(name + "_run")));
		addActor(petImage);
		setOrigin(Align.center);
	}

}
