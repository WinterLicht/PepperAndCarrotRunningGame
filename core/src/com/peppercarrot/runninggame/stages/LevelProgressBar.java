package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * UI Widget for level progress.
 * 
 * @author WinterLicht
 *
 */
public class LevelProgressBar extends Group {
	//Label segmentsPassed;
	Label points;
	ProgressBar progressBar;
	Label foreground;

	public LevelProgressBar(int maximum) {
		this();
		resetProgressBar(maximum);
	}

	public LevelProgressBar() {
		//Bunch of magic numbers to arrange the ui-elements
		resetProgressBar(10);
		progressBar.setDisabled(true);
		//Foreground for level progress bar 
		/*
		foreground = new Label("", Assets.I.skin, "levelBar");
		foreground.setAlignment(Align.center, Align.right);
		foreground.setWidth(progressBar.getWidth()-32);
		foreground.setHeight(foreground.getStyle().background.getMinHeight());
		foreground.setX(progressBar.getX()+12);
		foreground.setY(progressBar.getY());
		foreground.setZIndex(60);
		this.addActor(foreground);
		*/
		/*
		segmentsPassed = new Label("0", Assets.I.skin);
		segmentsPassed.setX(120);
		segmentsPassed.setY(Constants.VIRTUAL_HEIGHT-35);
		this.addActor(segmentsPassed);
		*/
		points = new Label("0", Assets.I.skin, "default-white");
		points.setAlignment(Align.center, Align.center);
		points.setX(390);
		points.setY(Constants.VIRTUAL_HEIGHT-75);
		this.addActor(points);
	}


	/**
	 * Recreate level progress bar with new maximum value.
	 * @param maximum
	 */
	public void resetProgressBar(int maximum) {
		this.removeActor(progressBar);
		progressBar = new ProgressBar(0, maximum, 1, false, Assets.I.skin, "level");
		progressBar.setWidth(717);
		progressBar.setX(100);
		progressBar.setY(Constants.VIRTUAL_HEIGHT-32);
		progressBar.setDisabled(true);
		this.addActor(progressBar);
		progressBar.toBack();
	}

	@Override
	public void act(float delta) {
		progressBar.setDisabled(getValue() == 0);
		super.act(delta);
	}

	public void setTotalPoints(int value) {
		points.setText(String.valueOf(value));
	}

	/*
	public void setSegmentsPassed(int value) {
		segmentsPassed.setText(String.valueOf(value));
	}
	*/

	public void setValue(float value) {
		progressBar.setValue(value);
	}

	public float getValue() {
		return progressBar.getValue();
	}

	public float getMaxValue() {
		return progressBar.getMaxValue();
	}

}
