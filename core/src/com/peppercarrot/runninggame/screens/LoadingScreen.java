package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class LoadingScreen extends ScreenAdapter {
	private Table rootTable;
	private Stage rootStage;
	private ProgressBar loadingBar;
	private boolean startGame = false;
	
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
		//TODO: Tell the manager to load assets for the loading screen
        //game.manager.load("loading.pack", TextureAtlas.class);
		// And wait until they are loaded
		//manager.finishLoading();
		Texture tex = new Texture(Gdx.files.internal("texture.png"), true);
		tex.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image bgTexture = new Image(tex);
		tex = new Texture(Gdx.files.internal("loading_screen.png"), true);
		tex.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image image = new Image(tex);
		image.setX(285);
		image.setY(70);
		// Populate the stage with progress bar
		ProgressBar.ProgressBarStyle pstyle = new ProgressBar.ProgressBarStyle(); 
		pstyle.knobBefore = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("loading_bar.png"))));
		loadingBar = new ProgressBar(0, 1, 0.1f, false, pstyle);
		loadingBar.setValue(0);
		loadingBar.setWidth(1000);
		loadingBar.setX(140);
		loadingBar.setY(20);

		rootTable.addActor(bgTexture);
		rootTable.addActor(image);
		rootTable.addActor(loadingBar);

		Assets.I.prepareForLoading();
	}

	@Override
	public void render(float delta) {
		rootStage.act(delta);
		rootStage.draw();

		if(Assets.I.manager.update()) {
			//Loading finished
			Assets.I.setUp();
			DefaultScreenConfiguration.getInstance().initializeMainMenu();
			setUpTransparentButton();
		}

		// display loading information
		float progress = Assets.I.manager.getProgress();
		loadingBar.setValue(progress);

		if (startGame) {
			ScreenSwitch.getInstance().setStartScreen();
		}

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
	}

	private void setUpTransparentButton() {
		//Asset manager ready, so it's possible to access to fonts
		Label label = new Label("Tap to start",
				Assets.I.skin, "default");
		label.setX(Constants.VIRTUAL_WIDTH/2-label.getWidth()/2);
		label.setY(35);
		//TODO
		label.addAction(Actions.forever(Actions.sequence( Actions.fadeOut(1f),
	    		Actions.fadeIn(1.4f))));
		rootTable.addActor(label);
		rootTable.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				startGame = true;
				event.cancel();
				return true;
			}
		});
	}
}
