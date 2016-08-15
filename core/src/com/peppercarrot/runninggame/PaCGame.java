package com.peppercarrot.runninggame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.peppercarrot.runninggame.screens.DefaultScreenConfiguration;
import com.peppercarrot.runninggame.screens.ScreenSwitch;

/**
 * The main game class.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class PaCGame extends Game {

	/**
	 * Set up game.
	 */
	@Override
	public void create() {
		DefaultScreenConfiguration.initializeInstance();

		ScreenSwitch.initialize(this);
		ScreenSwitch.getInstance().setStartScreen();
	}

	@Override
	public void resize(int width, int height) {
		DefaultScreenConfiguration.getInstance().update(width, height);
		super.resize(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		DefaultScreenConfiguration.getInstance().preRender();
		super.render();
	}
}
