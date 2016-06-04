package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;

public class Potion extends Group {
	public enum Color {
		ORANGE, GREEN, BLUE;
	}

	public Color type;
	AnimatedImage sparkling;
	Image potionImage;

	public Potion(String color) {
		potionImage = new Image(new TextureRegion(Assets.I.atlas.findRegion("potion_"+color)));
		sparkling = new AnimatedImage(new AnimatedDrawable(new Animation(0.14f, Assets.I.getRegions("sparkling"), Animation.PlayMode.LOOP)));
		switch (color) {
		case "orange":
			type = Potion.Color.ORANGE;
			break;
		case "green":
			type = Potion.Color.GREEN;
			break;
		case "blue":
			type = Potion.Color.BLUE;
			break;	
		default:
			System.out.println("not valid color");
			break;
		}
		addActor(potionImage);
		addActor(sparkling);
		setWidth(potionImage.getWidth());
		setHeight(potionImage.getHeight());
	}

	public void collected() {
		setVisible(false);
	}

	public void retrieveHitbox(Rectangle rectangle) {
		CollisionUtil.retrieveHitbox(this, rectangle);
	}
}
