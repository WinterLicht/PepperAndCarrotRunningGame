package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * 
 * @author momsen
 *
 */
public class ScreenSwitch {
	private static ScreenSwitch instance;

	public static void initialize(Game game) {
		instance = new ScreenSwitch(game);
	}

	public static ScreenSwitch getInstance() {
		return instance;
	}

	private final Game game;

	private ScreenSwitch(Game game) {
		this.game = game;
	}

	public void setStartScreen() {
		game.setScreen(new StartScreen());
	}

	public void setLoseScreen() {
		game.setScreen(new LoseScreen());
	}

	public void setWorldScreen() {
		game.setScreen(new WorldScreen());
	}

	public void setPauseScreen(Screen worldScreen, Stage worldStage) {
		game.setScreen(new PauseScreen(worldScreen, worldStage));
	}

	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}
}
