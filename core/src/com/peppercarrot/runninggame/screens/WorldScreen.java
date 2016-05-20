package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.entities.Pepper;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.stages.AbilityWidget.AbilityActivationListener;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.stages.WorldUiStage;
import com.peppercarrot.runninggame.utils.Callback;
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
 * @author WinterLicht
 * @author momsen
 *
 */
public class WorldScreen extends ScreenAdapter {

	private boolean gamePaused = false;

	private boolean gameStarted = false;

	private final Runner runner;

	private final WorldStage stage;

	private final WorldUiStage ui;

	public WorldScreen() {
		runner = new Pepper("pepper");

		stage = new WorldStage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, runner);

		ui = initializeUi();
	}

	private WorldUiStage initializeUi() {
		final WorldUiStage ui = new WorldUiStage();

		ui.onJumpTouched(new Callback() {
			@Override
			public void invoke() {
				if (!gameStarted) {
					stage.start();
				}
				runner.jump();
			}
		});

		ui.onActivateAbility(new AbilityActivationListener() {
			@Override
			public void activate(Ability ability) {
				ability.activate(stage);
			}
		});

		ui.setAbilitySlot1(runner.ability3);
		ui.setAbilitySlot2(runner.ability2);
		ui.setAbilitySlot3(runner.ability1);
		return ui;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(ui);
	}

	@Override
	public void render(float delta) {
		update(delta);

		draw();

		processInput();
	}

	private void update(float delta) {
		if (!gamePaused) {
			stage.move(delta);
		}

		final boolean wasDying = runner.isDying();
		stage.act(delta);
		if (!gamePaused && !wasDying && runner.isDying()) {
			switchToLoseScreen();
		}

		ui.act(delta);
	}

	private void draw() {
		stage.draw();
		ui.draw();
	}

	private void processInput() {
		// TODO: Current keyboard controls are as follow:
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			ui.getAbilitySlot1().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			ui.getAbilitySlot2().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.Y)) {
			ui.getAbilitySlot3().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			Gdx.app.log("<debug>", "break");
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (!gameStarted) {
				ui.hideHint();
				stage.start();
				gameStarted = true;
			}
			runner.jump();
		}
	}

	public void switchToLoseScreen() {
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
