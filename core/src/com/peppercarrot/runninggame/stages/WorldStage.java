package com.peppercarrot.runninggame.stages;

import com.peppercarrot.runninggame.entities.Background;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.LevelStream;

/**
 * Contains game entities.
 * 
 * @author WinterLicht
 *
 */
public class WorldStage extends AbstractStage {
	private final LevelStream levelStream;

	private final Background background;

	private final Runner runner;

	public WorldStage(Runner runner) {
		this.runner = runner;
		background = new Background("testbg.png");
		addActor(background);

		levelStream = new LevelStream(getBatch(), 0.0f,
				0.0f/*
					 * PaCGame.getInstance().viewport.getWorldWidth() * 1.5f,
					 * PaCGame.getInstance().viewport.getWorldWidth()
					 */);
		levelStream.setY(Constants.OFFSET_TO_GROUND);
		addActor(levelStream);

		addActor(runner);
	}

	public LevelStream getLevelStream() {
		return levelStream;
	}

	public void move(float offset) {
		background.moveViewportLeft(offset);
		background.setViewportY(runner.getY());
		levelStream.moveLeft(offset);
	}
}
