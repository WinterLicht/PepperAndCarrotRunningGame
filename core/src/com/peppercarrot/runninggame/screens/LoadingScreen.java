package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.peppercarrot.runninggame.utils.Assets;

public class LoadingScreen extends ScreenAdapter {
	private Table rootTable;
	private Stage rootStage;
	private ProgressBar loadingBar;
	
	public LoadingScreen() {
		rootStage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());
		rootTable = new Table();
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
		ProgressBar.ProgressBarStyle pstyle = new ProgressBar.ProgressBarStyle(); 
		pstyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("bg_yellow.9.png"))));
		loadingBar = new ProgressBar(0, 1, 0.1f, false, pstyle);
		loadingBar.setValue(0);
		rootTable.add(loadingBar).width(600).bottom().padBottom(60);

		Assets.I.prepareForLoading();
	}

	@Override
	public void render(float delta) {
		//stage.render(delta);
		rootStage.act();
		rootStage.draw();

		if(Assets.I.manager.update()) {
			//Loading finished
			Assets.I.setUp();
			DefaultScreenConfiguration.getInstance().initializeMainMenu();
			ScreenSwitch.getInstance().setStartScreen();
		}

		// display loading information
		float progress = Assets.I.manager.getProgress();
		loadingBar.setValue(progress);
	   }
}
