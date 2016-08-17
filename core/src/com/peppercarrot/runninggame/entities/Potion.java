package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Skill and health potions.
 * 
 * @author WinterLicht
 *
 */
public class Potion extends Collectible {
	public enum Color {
		ORANGE, GREEN, BLUE, PINK;
	}

	public Color type;

	public Potion(String color) {
		super(color);
	}

	@Override
	void setUp() {
		image = new Image(new TextureRegion(Assets.I.atlas.findRegion("potion_"+getName())));
		switch (getName()) {
		case "orange":
			type = Potion.Color.ORANGE;
			break;
		case "green":
			type = Potion.Color.GREEN;
			break;
		case "blue":
			type = Potion.Color.BLUE;
			break;
		case "pink":
			type = Potion.Color.PINK;
			break;
		default:
			System.out.println("not valid color");
			break;
		}
	}
}
