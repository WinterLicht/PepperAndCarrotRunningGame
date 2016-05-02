package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class LoseScreen implements Screen {
	Stage stage;
	boolean goToWorldMap = false;
	boolean goToStartScreen = false;

	public LoseScreen(Viewport viewport){
		stage = new Stage(viewport);
		//set up stage
		Table table = new Table(Assets.I.skin);
		table.setFillParent(true);
		table.setWidth(Constants.VIRTUAL_WIDTH);
		table.setHeight(Constants.VIRTUAL_HEIGHT);
		TextButton tryAgainBtn = new TextButton("Try again", Assets.I.skin, "default");
		tryAgainBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				goToWorldMap = true;
				event.cancel();
				return true;
			}
		});
		TextButton exitBtn = new TextButton("Exit", Assets.I.skin, "default");
		exitBtn.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				goToStartScreen = true;
				event.cancel();
				return true;
			}
		});
		table.add(exitBtn).bottom().padBottom(60);
		table.add(tryAgainBtn).bottom().padLeft(70).padBottom(60);
		table.bottom();
		stage.addActor(table);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		PaCGame game = PaCGame.getInstance();
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.setColor(1, 1, 1, 1);
		//TODO: render some background image
		game.batch.end();
		
		stage.act(delta);
		stage.draw();

		if (goToStartScreen || goToWorldMap) {
			switchScreen(0.25f);
		}

		if(Gdx.input.isKeyJustPressed(Keys.BACK)|| Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {
		PaCGame.getInstance().viewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/**
	 * Fade out animation that takes fadeOutTime long.
	 * @param fadeOutTime
	 */
	public void switchScreen(float fadeOutTime){
		stage.getRoot().getColor().a = 1;
	    SequenceAction sequenceAction = new SequenceAction();
	    sequenceAction.addAction( Actions.fadeOut(fadeOutTime));
	    sequenceAction.addAction( Actions.run(new Runnable() {
	        @Override
	        public void run() {
	            if (goToWorldMap){
	            	PaCGame.getInstance().setScreen(new WorldScreen(stage.getViewport()));
	            }
	            if (goToStartScreen){
	            	PaCGame.getInstance().setScreen(new StartScreen(stage.getViewport()));
	            }
	        }
	    }));
	    stage.getRoot().addAction(sequenceAction);
	    /*
	    backgroundImage.getColor().a = 1;
	    SequenceAction sequenceAction2 = new SequenceAction();
	    sequenceAction2.addAction( Actions.fadeOut(fadeOutTime) );
	    backgroundImage.addAction(sequenceAction2);
	    */
	}
}
