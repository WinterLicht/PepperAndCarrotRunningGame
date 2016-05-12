package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.peppercarrot.runninggame.Callback;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Background;
import com.peppercarrot.runninggame.entities.Level;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.stages.WorldUiStage;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Screen for actual game.
 * 
 * Contains the game logic and additionaly the different view layers of the
 * game:<br />
 * - Background<br />
 * - World<br />
 * - Player<br />
 * - UI<br />
 * 
 * In render() method are keyboard inputs handled. Handles also the main game
 * camera. TODO: Main camera is alligned according the player position, also
 * other camera should consider the offset. TODO: background in world stage as
 * attribute?
 * 
 * TODO:
 * 
 * - Decide who is responsible for interaction: ui or screen. if the screen
 * itself is responsible, then it has to delegate the initial hinting to the ui.
 * it also has to observe the touch events of the ui-jump-button.
 * 
 * - Level streaming. After a tmx is loaded, its content will be inserted into
 * the world stage. If the level is done, the content will be removed (if still
 * remaining) while the next is loaded.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class WorldScreen extends ScreenAdapter {

	private boolean gamePaused = false;

	private boolean gameStarted = false;

	private final Level level;

	private final Runner runner;

	private final WorldStage stage;

	private final WorldUiStage ui;

	/**
	 * TODO: Should be calculated within the background, but since there are no
	 * seemingless scrolling cameras at the moment, this is just incremented by
	 * scroll-speed.
	 */
	private float backgroundScrollX = 0.0f;

	/**
	 * Infinitely scrolling background.
	 */
	private final Background background;

	public WorldScreen() {
		level = new Level();
		runner = new Runner(level, "pepper");

		ui = new WorldUiStage(runner);
		ui.onJumpTouched(new Callback() {
			@Override
			public void invoke() {
				level.beginLevel = true;
			}
		});
		background = new Background("testbg.png");
		stage = new WorldStage(runner, level);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(ui);
	}

	@Override
	public void render(float delta) {
		update();

		draw(delta);

		processInput();
	}

	private void update() {
		if (!runner.isDying() && !gamePaused) {
			level.update();
		} else {
			switchToLoseScreen();
		}
	}

	private void draw(float delta) {
		final PaCGame game = PaCGame.getInstance();
		// Update main game camera.
		// Main camera is placed in the middle of the game screen,
		// but moves vertically when player jumps.
		// TODO: Let the main camera unchanged and add instead
		// a special camera to render player???
		game.camera.position.set(Constants.VIRTUAL_WIDTH / 2,
				runner.getY() + Constants.VIRTUAL_HEIGHT / 2 - Constants.OFFSET_TO_GROUND, 0);

		renderBackground(game);
		stage.render(delta);
		ui.act(delta);
		ui.draw();
	}

	private void processInput() {
		// TODO: Current keyboard controls are as follow:
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			runner.activateAbility(1);
		}
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			runner.activateAbility(2);
		}
		if (Gdx.input.isKeyJustPressed(Keys.Y)) {
			runner.activateAbility(3);
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (!gameStarted) {
				level.beginLevel = true;
				gameStarted = true;
			}
			runner.jump();
		}
	}

	private void renderBackground(PaCGame game) {
		game.batch.begin();
		game.batch.setColor(1, 1, 1, 1);
		background.draw(game.batch, backgroundScrollX, runner.getY());
		backgroundScrollX += level.scrollSpeed;
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void switchToLoseScreen() {
		if (!gamePaused) {
			ui.disable();

			final float fadeOutTime = 0.48f;
			ui.fadeOut(true, fadeOutTime, null);
			stage.fadeOut(true, fadeOutTime, new Runnable() {
				@Override
				public void run() {
					PaCGame.getInstance().setScreen(new LoseScreen());
				}
			});

			gamePaused = true;
		}
	}
}
