package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.entities.Level;
import com.peppercarrot.runninggame.entities.Runner;

/**
 * Contains game entities.
 * 
 * @author WinterLicht
 *
 */
public class WorldStage extends AbstractStage {
	Table charTable;
	/** Contains characters: player, enemies... */
	public boolean playerReady = false;
	private final Level level;

	/** when false, level logic will be updated.s */

	public WorldStage(Runner runner, Level level) {
		this.level = level;
		charTable = new Table();
		charTable.setFillParent(true);

		charTable.addActor(runner);
		charTable.addActor(runner.ability1.effect);
		charTable.addActor(runner.ability2.effect);
		// charTable.addActor(runner.ability3.effect);

		this.addActor(charTable);
	}

	public void render(float delta) {
		this.act(delta);

		level.renderBackground();
		this.draw();
		level.renderEntities(delta);
		level.renderForeground();
	}
}
