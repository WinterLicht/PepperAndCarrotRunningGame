package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public class Potion extends AnimatedImage {
	int type;

	public Potion(int type) {
		super(new Animation(0.14f, Assets.I.getRegions("potion"), Animation.PlayMode.LOOP));
		//TODO: various types
		this.type = type;
	}

	public void collected() {
		setVisible(false);
	}
}
