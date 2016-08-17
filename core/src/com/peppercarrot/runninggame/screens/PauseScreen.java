package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.utils.Account;
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
		table.setBackground("bg-dark");
		table.setWidth(Constants.VIRTUAL_WIDTH);
		table.setHeight(Constants.VIRTUAL_HEIGHT);
		table.center();

		final Label label = new Label("Paused", Assets.I.skin, "title");
		label.setAlignment(Align.center, Align.center);
		table.add(label).center().colspan(3).padBottom(24);
		table.row();

		final TextButton exitBtn = new TextButton("Exit", Assets.I.skin, "lavi");
		exitBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				exitGame();
				event.cancel();
				return true;
			}
		});
		table.add(exitBtn).width(200).left().padRight(30);
		
		final Image image = new Image(Assets.I.atlas.findRegion("pause"));
		table.add(image).center();

		final TextButton resumeBtn = new TextButton("Resume", Assets.I.skin, "lavi");
		resumeBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				resumeGame();
				event.cancel();
				return true;
			}
		});
		table.add(resumeBtn).width(200).right().padLeft(30);

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
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Account.I.exit();
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
