package com.peppercarrot.runninggame.world;

import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Platform {

	private final Actor parent;
	private final float w;
	private final float h;
	private final Vector2 relativePosition;

	public Platform(Actor parent, float x, float y, float w, float h) {
		this.parent = parent;
		this.relativePosition = new Vector2(x, y);
		this.w = w;
		this.h = h;
	}

	public void retrieveAbsolutePosition(Vector2 position) {
		position.set(relativePosition);
		parent.localToStageCoordinates(position);
	}

	public float getW() {
		return w;
	}

	public float getH() {
		return h;
	}

	public static class XPositionComparator implements Comparator<Platform> {

		@Override
		public int compare(Platform left, Platform right) {
			return Float.compare(left.relativePosition.x, right.relativePosition.x);
		}
	}
}
