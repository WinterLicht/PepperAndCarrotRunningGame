package com.peppercarrot.runninggame.overworld;

import com.peppercarrot.runninggame.world.LevelLayout;

public class OverworldLevelNode extends OverworldNode {
	private LevelLayout levelLayout;

	public LevelLayout getLevelLayout() {
		return levelLayout;
	}

	public void setLevelLayout(LevelLayout levelLayout) {
		this.levelLayout = levelLayout;
	}
}