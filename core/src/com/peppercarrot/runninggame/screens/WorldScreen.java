package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Background;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Screen for actual game.
 * In render() method are keyboard inputs handled. Handles also the
 * main game camera.
 * TODO: Main camera is alligned according the player position, also
 * other camera should consider the offset.
 * @author WinterLicht
 *
 */
public class WorldScreen implements Screen {
	WorldStage stage;
	Background background; /** Infinitely scrolling background. */

	public WorldScreen(Viewport viewport) {
		stage = new WorldStage(viewport);
		background = new Background((int) stage.level.scrollSpeed);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		PaCGame game = PaCGame.getInstance();
		//Update main game camera.
		//Main camera is placed in the middle of the game screen,
		//but moves vertically when player jumps.
		//TODO: Let the main camera unchanged and add instead
		//a special camera to render player???
		game.camera.position.set(Constants.VIRTUAL_WIDTH / 2,
				stage.runner.getY()+Constants.VIRTUAL_HEIGHT / 2 - Constants.OFFSET_TO_GROUND, 0);
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.setColor(1, 1, 1, 1);
		background.act(delta);
		background.draw(game.batch, 1f);
		game.batch.end();

		this.stage.render(delta);

		//TODO: Current keyboard controls are as follow:
		if(Gdx.input.isKeyJustPressed(Keys.BACK)|| Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
		if(Gdx.input.isKeyJustPressed(Keys.A)){
			stage.runner.activateAbility(1);
		}
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			if (!stage.playerReady) {
				stage.level.beginLevel = true;
			}
			stage.runner.jump();
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

}
