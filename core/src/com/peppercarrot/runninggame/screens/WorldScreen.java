package com.peppercarrot.runninggame.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
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
 * Contains the game logic and additionally the different view layers of the
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

	public WorldScreen(List<String> levelSegments) {
		runner = new Pepper("pepper");

		stage = new WorldStage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, runner, levelSegments);

		ui = initializeUi();
		//After stage with levelStream is loaded:
		ui.levelProgress.resetProgressBar(stage.getLevelStream().getTotalNumberOfTiles());
	}

	private WorldUiStage initializeUi() {
		final WorldUiStage ui = new WorldUiStage(runner) {
			@Override
			public void updateLevelProgress() {
				//Queue<LevelSegment> segments = stage.getLevelSegments();
				levelProgress.setTotalPoints(stage.getLevelStream().getTotalPassedTiles());
				levelProgress.setValue(stage.getLevelStream().getTotalPassedTiles());
				/*
				//Update Progress Bar for each segment
				for (int i = 0; i < segments.size; i++) {
					LevelSegment s = segments.get(i);
					levelProgress.setValue(stage.getLevelStream().getPassedSegmentTiles());
					int size = stage.getLevelStream().getCurrSegmentLength();
					if (s.getRightX() > Constants.OFFSET_TO_EDGE && s.getX() < Constants.OFFSET_TO_EDGE &&
							size != levelProgress.getMaxValue()) {
						levelProgress.resetProgressBar(size);
					}
				}
				*/
			}
		};

		ui.onJumpTouched(new Callback() {
			@Override
			public void invoke() {
				if (!gameStarted) {
					stage.start();
				}
				runner.jump();
			}
		});

		ui.onExitTouched(new Callback() {
			@Override
			public void invoke() {
				ScreenSwitch.getInstance().setPauseScreen(WorldScreen.this, stage);
			}
		});

		ui.onActivateAbility(new AbilityActivationListener() {
			@Override
			public void activate(Ability ability) {
				ability.activate(stage);
			}
		});

		ui.setAbilitySlot0(runner.ability0);
		ui.setAbilitySlot1(runner.ability1);
		ui.setAbilitySlot2(runner.ability2);
		ui.setAbilitySlot3(runner.ability3);
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
			//Lose Condition
			switchToLoseScreen();
		}
		if (!gamePaused && !runner.isDying() && !wasDying && 
				stage.getLevelStream().allSegmentsPassed()) {
			//Win Condition
			switchToWinScreen();
		}
		ui.act(delta);
		// Print progress
		// Gdx.app.log("Passed stuff", "segments=" +
		// stage.getLevelStream().getPassedSegments() + ", tiles="
		// + stage.getLevelStream().getTotalPassedTiles());
	}

	private void draw() {
		stage.draw();
		ui.draw();
	}

	private void processInput() {
		// TODO: Current keyboard controls are as follow:
		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			ScreenSwitch.getInstance().setPauseScreen(this, stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.Y)) {
			ui.getAbilitySlot1().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.X)) {
			ui.getAbilitySlot2().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.C)) {
			ui.getAbilitySlot3().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.V)) {
			ui.getAbilitySlot0().activate(stage);
		}
		if (Gdx.input.isKeyJustPressed(Keys.TAB)) {
			Gdx.app.log("<debug>", "break");
		}
		if (Gdx.input.isKeyJustPressed(Keys.P)) {
			ScreenSwitch.getInstance().setPauseScreen(this, stage);
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
				ScreenSwitch.getInstance().setLoseScreen(stage);
			}
		});

		gamePaused = true;
	}

	public void switchToWinScreen() {
		ui.disable();

		final float fadeOutTime = 0.48f;
		ui.fadeOut(true, fadeOutTime, null);
		stage.fadeOut(true, fadeOutTime, new Runnable() {
			@Override
			public void run() {
				ScreenSwitch.getInstance().setWinScreen(stage);
			}
		});

		gamePaused = true;
	}
}
