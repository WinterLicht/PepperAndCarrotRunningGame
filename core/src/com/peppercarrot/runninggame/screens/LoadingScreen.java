package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.utils.Assets;

public class LoadingScreen extends ScreenAdapter {
	private Stage rootStage;
	
	public LoadingScreen() {
		rootStage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());
		Table rootTable = new Table();
		rootTable.setFillParent(true);
		rootStage.addActor(rootTable);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(rootStage);

		// https://github.com/Matsemann/libgdx-loading-screen
		//TODO:
		// Tell the manager to load assets for the loading screen
        //game.manager.load("loading.pack", TextureAtlas.class);
		// And wait until they are loaded
		//manager.finishLoading();
		// Populate the stage with progress bar

		Assets.I.prepareForLoading();
	}

	@Override
	public void render(float delta) {
		//stage.render(delta);
		rootStage.act();
		rootStage.draw();

		if(Assets.I.manager.update()) {
			//Loading finished
			System.out.println("finished");
			Assets.I.setUp();
			DefaultScreenConfiguration.getInstance().initializeMainMenu();
			ScreenSwitch.getInstance().setStartScreen();
		}

		// display loading information
		float progress = Assets.I.manager.getProgress();
		System.out.println("Assets loading progress: "+progress);
	   }
}
