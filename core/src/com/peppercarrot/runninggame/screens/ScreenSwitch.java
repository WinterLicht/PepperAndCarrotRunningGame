package com.peppercarrot.runninggame.screens;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.story.Storyboard;
import com.peppercarrot.runninggame.utils.Account;

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

	public void setLoadingScreen() {
		game.setScreen(new LoadingScreen());
	}

	public void setStartScreen() {
		game.setScreen(new StartScreen());
		MainMenu.getInstance().setChecked(1);
	}

	public void setLoseScreen(Stage worldStage) {
		Account.I.saveData();
		game.setScreen(new LoseScreen(worldStage));
	}

	public void setWinScreen(Stage worldStage) {
		//Not to forget to update Account with the progress
		Account.I.updateAccountAfterWin();
		game.setScreen(new WinScreen(worldStage));
	}

	public void setWorldScreen(List<String> levelSegments) {
		//Not to forget to reset data from previous session
		Account.I.resetHelper();
		game.setScreen(new WorldScreen(levelSegments));
	}

	public void setPauseScreen(Screen worldScreen, Stage worldStage) {
		game.setScreen(new PauseScreen(worldScreen, worldStage));
	}

	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}

	public void setOverworldScreen() {
		game.setScreen(new OverworldScreen());
		MainMenu.getInstance().setChecked(4);
	}

	public void setStoryScreen(Storyboard storyboard) {
		game.setScreen(new StoryScreen(storyboard));
	}

	public void setInfoScreen() {
		game.setScreen(new InfoScreen());
		MainMenu.getInstance().setChecked(2);
	}

	public void setAchievementsScreen() {
		game.setScreen(new AchievementsScreen());
		MainMenu.getInstance().setChecked(5);
	}

	public void setSettingsScreen() {
		game.setScreen(new SettingsScreen());
		MainMenu.getInstance().setChecked(3);
	}
}
