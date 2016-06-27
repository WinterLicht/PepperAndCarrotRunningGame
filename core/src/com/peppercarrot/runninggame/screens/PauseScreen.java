package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Pause screen.
 * 
 * @author momsen
 *
 */
public class PauseScreen extends ScreenAdapter {

	private final Screen screenToResume;
	private final Stage sceneToRender;
	private final Stage ui;

	public PauseScreen(Screen screenToResume, Stage sceneToRender) {
		this.screenToResume = screenToResume;
		this.sceneToRender = sceneToRender;
		this.ui = initializeUi();
	}

	private Stage initializeUi() {
		final Stage uiStage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());

		final Table table = new Table(Assets.I.skin);
		table.setFillParent(true);
		table.setWidth(Constants.VIRTUAL_WIDTH);
		table.setHeight(Constants.VIRTUAL_HEIGHT);
		table.center();

		final Label label = new Label("Pause", Assets.I.skin, "default");
		table.row();
		table.add(label).center();

		final TextButton resumeBtn = new TextButton("Resume", Assets.I.skin, "default");
		resumeBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				event.cancel();
				return true;
			}
		});
		table.row();
		table.add(resumeBtn).bottom();

		final TextButton exitBtn = new TextButton("Exit", Assets.I.skin, "default");
		exitBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				exitGame();
				event.cancel();
				return true;
			}
		});
		table.row();
		table.add(exitBtn).bottom();

		uiStage.addActor(table);

		return uiStage;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(ui);
	}

	@Override
	public void render(float delta) {
		update(delta);

		draw();

		processInput();
	}

	private void processInput() {
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			resumeGame();
		}
	}

	private void resumeGame() {
		ScreenSwitch.getInstance().setScreen(screenToResume);
	}

	private void exitGame() {
		ScreenSwitch.getInstance().setStartScreen();
	}

	private void draw() {
		sceneToRender.draw();
		ui.draw();
	}

	private void update(float delta) {
		ui.act(delta);
	}
}
