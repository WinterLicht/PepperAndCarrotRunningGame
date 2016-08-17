package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.stages.CharacterTable;
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.stages.StatisticsTable;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class AchievementsScreen extends ScreenAdapter {
	Stage stage;
	ButtonGroup<TextButton> tabs;
	CharacterTable charInfo;
	StatisticsTable statistics;

	public AchievementsScreen() {
		stage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());
		final Table rootTable = new Table(Assets.I.skin);
		rootTable.setFillParent(true);
		rootTable.top();
		rootTable.addActor(Assets.I.bgTexture);
		rootTable.addActor(Assets.I.bgTopTexture);
		rootTable.setWidth(Constants.VIRTUAL_WIDTH-2*MainMenu.getInstance().buttonWidth);
		rootTable.setHeight(Constants.VIRTUAL_HEIGHT);
		rootTable.padRight(MainMenu.getInstance().buttonWidth);
		rootTable.padLeft(MainMenu.getInstance().buttonWidth);
		rootTable.padTop(25);
		//Set up tabs
		tabs = new ButtonGroup<TextButton>();
		int buttonWidth = 210;
		int buttonHeight = 85;
		TextButton charbtn = new TextButton("character", Assets.I.skin, "transparent-bg");
		charbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				hideAllTabs();
				charInfo.setVisible(true);
				event.cancel();
			}
		});
		charbtn.setWidth(buttonWidth);
		charbtn.setHeight(buttonHeight);
		tabs.add(charbtn);
		rootTable.add(charbtn).width(buttonWidth).height(buttonHeight);
		TextButton statisticsbtn = new TextButton("statistics", Assets.I.skin, "transparent-bg");
		statisticsbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				hideAllTabs();
				statistics.setVisible(true);
				event.cancel();
			}
		});
		statisticsbtn.setWidth(buttonWidth);
		statisticsbtn.setHeight(buttonHeight);
		tabs.add(statisticsbtn);
		rootTable.add(statisticsbtn).width(buttonWidth).height(buttonHeight);

		charInfo = new CharacterTable();
		charInfo.setX(MainMenu.getInstance().buttonWidth);
		charInfo.setWidth(rootTable.getWidth());
		charInfo.setHeight(rootTable.getHeight()-Assets.I.bgTopTexture.getHeight());
		rootTable.addActor(charInfo);

		statistics = new StatisticsTable();
		statistics.setVisible(false);
		statistics.setX(MainMenu.getInstance().buttonWidth);
		statistics.setWidth(rootTable.getWidth());
		statistics.setHeight(rootTable.getHeight()-Assets.I.bgTopTexture.getHeight());
		rootTable.addActor(statistics);

		stage.addActor(rootTable);
	}

	private void hideAllTabs() {
		charInfo.setVisible(false);
		statistics.setVisible(false);
	}

	@Override
	public void show() {
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(stage);
		multi.addProcessor(MainMenu.getInstance());		
		Gdx.input.setInputProcessor(multi);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();

		MainMenu.getInstance().render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Account.I.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
}
