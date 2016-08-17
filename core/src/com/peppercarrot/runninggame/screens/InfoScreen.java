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
import com.peppercarrot.runninggame.stages.TutorialTable;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Information screen contains a tutorial, credits ...
 * 
 * @author WinterLicht
 *
 */
public class InfoScreen extends ScreenAdapter {
	Stage stage;
	ButtonGroup<TextButton> tabs;
	TutorialTable tutorial;

	public InfoScreen() {
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
		TextButton tutorialbtn = new TextButton("how to play", Assets.I.skin, "transparent-bg");
		tutorialbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				hideAllTabs();
				tutorial.setVisible(true);
				event.cancel();
			}
		});
		tutorialbtn.setWidth(buttonWidth);
		tutorialbtn.setHeight(buttonHeight);
		tabs.add(tutorialbtn);
		rootTable.add(tutorialbtn).width(buttonWidth).height(buttonHeight);
		//
		tutorial = new TutorialTable();
		tutorial.setX(MainMenu.getInstance().buttonWidth);
		tutorial.setWidth(rootTable.getWidth());
		tutorial.setHeight(rootTable.getHeight()-buttonHeight);
		rootTable.addActor(tutorial);
		
		stage.addActor(rootTable);
	}

	private void hideAllTabs() {
		tutorial.setVisible(false);
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
