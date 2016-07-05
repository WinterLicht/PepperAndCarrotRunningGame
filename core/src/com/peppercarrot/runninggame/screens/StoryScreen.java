package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.story.Storyboard;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class StoryScreen extends ScreenAdapter {

	private final Stage ui;

	public StoryScreen(Storyboard storyboard) {
		this.ui = initializeUi();
	}

	private Stage initializeUi() {
		final Stage uiStage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());

		final Table table = new Table(Assets.I.skin);
		table.setFillParent(true);
		table.setBackground("bg-dark");
		table.setWidth(Constants.VIRTUAL_WIDTH);
		table.setHeight(Constants.VIRTUAL_HEIGHT);
		table.center();

		final Label label = new Label("STORY", Assets.I.skin, "title");
		label.setAlignment(Align.center, Align.center);
		table.add(label).center().colspan(3).padBottom(24);
		table.row();

		final TextButton exitBtn = new TextButton("Exit", Assets.I.skin, "lavi");
		exitBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ScreenSwitch.getInstance().setOverworldScreen();
				event.cancel();
				return true;
			}
		});
		table.add(exitBtn).width(200).left().padRight(30);

		uiStage.addActor(table);

		return uiStage;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(ui);
	}

	@Override
	public void render(float delta) {
		ui.act(delta);

		ui.draw();
	}
}
