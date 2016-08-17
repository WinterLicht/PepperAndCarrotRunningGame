package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * 
 * @author momsen
 *
 */
public class LoseScreen extends ScreenAdapter {
	Stage stage;
	private final Stage sceneToRender;
	boolean goToWorldMap = false;
	boolean goToStartScreen = false;

	public LoseScreen(Stage sceneToRender) {
		stage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());
		this.sceneToRender = sceneToRender;
		// set up stage
		final Table table = new Table(Assets.I.skin);
		table.setFillParent(true);
		table.setBackground("bg-dark");
		table.setWidth(Constants.VIRTUAL_WIDTH);
		table.setHeight(Constants.VIRTUAL_HEIGHT);
		table.center();

		final Label label = new Label("Game Over", Assets.I.skin, "title");
		label.setAlignment(Align.center, Align.center);
		table.add(label).center().colspan(3).padBottom(24);
		table.row();

		final TextButton exitBtn = new TextButton("Exit", Assets.I.skin, "lavi");
		exitBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				goToStartScreen = true;
				event.cancel();
				return true;
			}
		});
		table.add(exitBtn).width(200).left().padRight(30);

		final Image image = new Image(Assets.I.atlas.findRegion("lose"));
		table.add(image).center();

		final TextButton tryAgainBtn = new TextButton("Try again", Assets.I.skin, "lavi");
		tryAgainBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				goToWorldMap = true;
				event.cancel();
				return true;
			}
		});
		table.add(tryAgainBtn).width(200).left().padRight(30);

		stage.addActor(table);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		sceneToRender.draw();
		stage.act(delta);
		stage.draw();

		if (goToStartScreen || goToWorldMap) {
			switchScreen(0.25f);
		}

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Account.I.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	/**
	 * Fade out animation that takes fadeOutTime long.
	 * 
	 * @param fadeOutTime
	 */
	public void switchScreen(float fadeOutTime) {
		stage.getRoot().getColor().a = 1;
		final SequenceAction sequenceAction = new SequenceAction();
		sequenceAction.addAction(Actions.fadeOut(fadeOutTime));
		sequenceAction.addAction(Actions.run(new Runnable() {
			@Override
			public void run() {
				if (goToWorldMap) {
					ScreenSwitch.getInstance().setWorldScreen(((WorldStage) sceneToRender).getLevelStream().getAllFileNames());
				}
				if (goToStartScreen) {
					ScreenSwitch.getInstance().setStartScreen();
				}
			}
		}));
		stage.getRoot().addAction(sequenceAction);
		/*
		 * backgroundImage.getColor().a = 1; SequenceAction sequenceAction2 =
		 * new SequenceAction(); sequenceAction2.addAction(
		 * Actions.fadeOut(fadeOutTime) );
		 * backgroundImage.addAction(sequenceAction2);
		 */
	}
}
