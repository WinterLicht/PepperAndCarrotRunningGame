package com.peppercarrot.runninggame.utils;

import java.util.Comparator;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Comparator for Actors using their x position.
 * 
 * @author momsen
 *
 */
public class ActorXPositionComparator implements Comparator<Actor> {

	private final boolean ascending;

	public ActorXPositionComparator() {
		this.ascending = true;
	}

	/**
	 * Comparison direction
	 * 
	 * @param ascending
	 *            <code>true</code>, if comparison is used for ascending order
	 */
	public ActorXPositionComparator(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public int compare(Actor left, Actor right) {
		if (ascending) {
			return Float.compare(left.getX(), right.getX());
		}
		return Float.compare(right.getX(), left.getX());
	}
}
