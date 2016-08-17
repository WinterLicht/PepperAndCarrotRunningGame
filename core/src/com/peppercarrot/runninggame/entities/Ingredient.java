package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.peppercarrot.runninggame.utils.Assets;

public class Ingredient extends Collectible {
	public Ingredient(String name) {
		super(name);
	}

	@Override
	void setUp() {
		image = new Image(new TextureRegion(Assets.I.atlas.findRegion(getName())));
	}
}
