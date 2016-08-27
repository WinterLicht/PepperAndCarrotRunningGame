package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.utils.Constants;

public class DefaultScreenConfiguration {

	private final Batch batch;

	private final OrthographicCamera camera;

	private final FitViewport viewport;

	private static DefaultScreenConfiguration instance;

	private DefaultScreenConfiguration() {
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		camera.setToOrtho(false, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		camera.position.set(Constants.VIRTUAL_WIDTH / 2f, Constants.VIRTUAL_HEIGHT / 2f, 0);
		camera.update();

		viewport = new FitViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, camera);
	}

	public Batch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public FitViewport getViewport() {
		return viewport;
	}

	/**
	 * After Assets are loaded
	 */
	public void initializeMainMenu() {
		MainMenu.initialize(viewport);
	}

	public static void initializeInstance() {
		instance = new DefaultScreenConfiguration();
	}

	public static DefaultScreenConfiguration getInstance() {
		if (instance == null) {
			initializeInstance();
		}

		return instance;
	}

	public void update(int width, int height) {
		viewport.update(width, height);
	}

	public void preRender() {
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}
}
