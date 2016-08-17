package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;

public abstract class Collectible extends Group {

	protected AnimatedImage sparkling;
	Image image;

	public Collectible(String name) {
		this.setName(name);
		sparkling = new AnimatedImage(new AnimatedDrawable(new Animation(0.14f, Assets.I.getRegions("sparkling"), Animation.PlayMode.LOOP)));
		setUp();
		addActor(image);
		addActor(sparkling);
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	public void collected() {
		setVisible(false);
	}

	public void retrieveHitbox(Rectangle rectangle) {
		CollisionUtil.retrieveHitbox(this, rectangle);
	}

	abstract void setUp();
}
