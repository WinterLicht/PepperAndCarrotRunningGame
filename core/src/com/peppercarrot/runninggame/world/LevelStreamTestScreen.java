package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.peppercarrot.runninggame.PaCGame;

public class LevelStreamTestScreen extends ScreenAdapter {
	private final LevelStream levelStream;

	private final Stage stage;

	private float speed = 5f;

	public LevelStreamTestScreen() {
		levelStream = new LevelStream(PaCGame.getInstance().batch,
				PaCGame.getInstance().viewport.getWorldWidth() * 1.5f);

		stage = new Stage(PaCGame.getInstance().viewport, PaCGame.getInstance().batch);
		stage.addActor(levelStream);

		levelStream.start();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		PaCGame.getInstance().viewport.update(width, height);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		stage.act(delta);
		stage.draw();

		levelStream.moveLeft(speed);

		if (Gdx.input.isKeyJustPressed(Keys.UP)) {
			speed += 1.0f;
		} else if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			speed -= 1.0f;
		}
	}
}
