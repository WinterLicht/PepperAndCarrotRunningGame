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

/**
 * TODO actually rename this and corresponding functions to "collectible".
 * 
 * @author WinterLicht
 *
 */
public class Potion extends Group {
	public enum Type {
		POTION, INGREDIENT;
	}
	public Type type;
	protected AnimatedImage sparkling;
	Image image;

	public Potion(String name, Type type) {
		this.type = type;
		this.setName(name);
		sparkling = new AnimatedImage(new AnimatedDrawable(new Animation(0.14f, Assets.I.getRegions("sparkling"), Animation.PlayMode.LOOP)));
		if (type == Type.POTION) {
			image = new Image(new TextureRegion(Assets.I.atlas.findRegion("potion_"+name)));
		} else {
			image = new Image(new TextureRegion(Assets.I.atlas.findRegion(name)));
		}
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
}
