package com.peppercarrot.runninggame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.peppercarrot.runninggame.screens.StartScreen;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * The main game class.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class PaCGame extends Game {

	public SpriteBatch batch;
	public OrthographicCamera camera;
	public FitViewport viewport;

	// PaCGame is singleton
	private static PaCGame instance = new PaCGame();

	private PaCGame() {
	}

	public static PaCGame getInstance() {
		return instance;
	}

	/**
	 * Set up game.
	 */
	@Override
	public void create() {
		batch = new SpriteBatch();
		setupCamera();

		// TODO: Should start with a "start screen"
		setScreen(new StartScreen());
	}

	/**
	 * Crate orthographic camera.
	 */
	private void setupCamera() {
		// Camera in the middle
		camera = new OrthographicCamera(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		camera.setToOrtho(false, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		camera.position.set(Constants.VIRTUAL_WIDTH / 2f, Constants.VIRTUAL_HEIGHT / 2f, 0);
		camera.update();
		viewport = new FitViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, camera);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		super.resize(width, height);
	}

	public FitViewport getViewport() {
		return viewport;
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		super.render();
	}
}
