package com.peppercarrot.runninggame.world;

import java.util.Comparator;

public class Platform {

	private final float x;
	private final float y;
	private final float w;
	private final float h;

	public Platform(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
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
			return Float.compare(left.x, right.x);
		}
	}
}
