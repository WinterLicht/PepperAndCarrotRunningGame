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
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.stages.SettingsTable;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class SettingsScreen extends ScreenAdapter {
	Stage stage;
	ButtonGroup<TextButton> tabs;
	//Table about;
	SettingsTable settings;

	public SettingsScreen() {
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
		/*
		TextButton creditsbtn = new TextButton("credits", Assets.I.skin, "transparent-bg");
		creditsbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				hideAllTabs();
				about.setVisible(true);
				event.cancel();
			}
		});
		creditsbtn.setWidth(buttonWidth);
		creditsbtn.setHeight(buttonHeight);
		tabs.add(creditsbtn);
		rootTable.add(creditsbtn).width(buttonWidth).height(buttonHeight);
		*/
		TextButton settingsbtn = new TextButton("reset game data", Assets.I.skin, "transparent-bg");
		settingsbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				hideAllTabs();
				settings.setVisible(true);
				event.cancel();
			}
		});
		settingsbtn.setWidth(buttonWidth);
		settingsbtn.setHeight(buttonHeight);
		tabs.add(settingsbtn);
		rootTable.add(settingsbtn).width(buttonWidth).height(buttonHeight);
		rootTable.row();
		//
		/*
		about = new CreditsTable();
		about.setX(MainMenu.getInstance().buttonWidth);
		about.setWidth(rootTable.getWidth());
		about.setHeight(rootTable.getHeight()-Assets.I.bgTopTexture.getHeight());
		rootTable.addActor(about);
		*/
		settings = new SettingsTable();
		settings.setX(MainMenu.getInstance().buttonWidth);
		settings.setWidth(rootTable.getWidth());
		settings.setHeight(rootTable.getHeight()-Assets.I.bgTopTexture.getHeight());
		rootTable.addActor(settings);

		stage.addActor(rootTable);
	}

	private void hideAllTabs() {
		//about.setVisible(false);
		settings.setVisible(false);
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
