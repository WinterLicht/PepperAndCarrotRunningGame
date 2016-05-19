package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Utility that is used to calculate actor hitboxes without producing garbage.
 * The methods may NEVER be used in a concurrent context, since they use static
 * temp variables.
 * 
 * @author momsen
 *
 */
public class CollisionUtil {
	private final static Vector2 tempPosition = new Vector2();

	private CollisionUtil() {
		// Utility class
	}

	/**
	 * Retrieves the axis-aligned hitbox of an actor by calculating the world
	 * coordinates. Rotation and scale are not applied to the hitbox.
	 * 
	 * @param actor
	 *            actor of which the hitbox will be calculated
	 * @param rectangle
	 *            result rectangle
	 */
	public static void retrieveHitbox(Actor actor, Rectangle rectangle) {
		tempPosition.x = 0;
		tempPosition.y = 0;
		actor.localToStageCoordinates(tempPosition);

		rectangle.x = tempPosition.x;
		rectangle.y = tempPosition.y;
		rectangle.width = actor.getWidth();
		rectangle.height = actor.getHeight();
	}
}
