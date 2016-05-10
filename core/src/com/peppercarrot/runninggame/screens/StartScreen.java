package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.stages.StartStage;

/**
 * 
 * @author momsen
 *
 */
public class StartScreen extends ScreenAdapter {
	StartStage stage;

	public StartScreen(Viewport viewport) {
		stage = new StartStage(viewport);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		PaCGame game = PaCGame.getInstance();

		game.batch.begin();
		game.batch.setColor(1, 1, 1, 1);
		// TODO: render some background image
		game.batch.end();

		stage.render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
