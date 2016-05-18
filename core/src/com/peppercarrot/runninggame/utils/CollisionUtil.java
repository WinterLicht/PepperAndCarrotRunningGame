package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CollisionUtil {
	private final static Vector2 tempPosition = new Vector2();

	private CollisionUtil() {

	}

	public static void retrieveHitbox(Actor actor, Rectangle rectangle) {
		tempPosition.x = actor.getX();
		tempPosition.y = actor.getY();
		actor.getParent().localToStageCoordinates(tempPosition);

		rectangle.x = tempPosition.x;
		rectangle.y = tempPosition.y;
		rectangle.width = actor.getWidth();
		rectangle.height = actor.getHeight();
	}
}
