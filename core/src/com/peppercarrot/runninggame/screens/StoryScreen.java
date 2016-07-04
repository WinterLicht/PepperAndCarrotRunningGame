package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.peppercarrot.runninggame.story.Storyboard;

public class StoryScreen extends ScreenAdapter {

	private final Storyboard storyboard;

	public StoryScreen(Storyboard storyboard) {
		this.storyboard = storyboard;
	}
}
