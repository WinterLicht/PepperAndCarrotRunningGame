package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Game;

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
}
