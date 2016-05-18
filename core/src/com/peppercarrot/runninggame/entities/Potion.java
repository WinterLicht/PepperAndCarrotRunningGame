package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

public class Potion extends AnimatedImage {
	int type;
	private final Vector2 tempPosition = new Vector2();

	public Potion(int type) {
		super(new AnimatedDrawable(new Animation(0.14f, Assets.I.getRegions("potion"), Animation.PlayMode.LOOP)));
		// TODO: various types
		this.type = type;
	}

	public void collected() {
		setVisible(false);
	}

	public void retrieveRectangle(Rectangle rectangle) {
		tempPosition.x = getX();
		tempPosition.y = getY();
		getParent().localToStageCoordinates(tempPosition);

		rectangle.x = tempPosition.x;
		rectangle.y = tempPosition.y;
		rectangle.width = getWidth();
		rectangle.height = getHeight();
	}
}
