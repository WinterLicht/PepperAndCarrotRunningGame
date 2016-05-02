package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Level;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.screens.LoseScreen;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Contains game entities.
 * @author WinterLicht
 *
 */
public class WorldStage extends Stage {
	Table charTable; /** Contains characters: player, enemies... */
	Table uiTable;
	public Runner runner;
	public boolean playerReady = false;
	public Level level;
	Table attackButtons;
	boolean gamePaused = false; /** when false, level logic will be updated.s */
	Label hintLabel;

	public WorldStage(Viewport viewport){
		super(viewport);
		charTable = new Table();
		charTable.setFillParent(true);

		int uiPadding = 30; //padding of borders for ui in pixel
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiTable.pad(uiPadding);
		uiTable.setWidth(Constants.VIRTUAL_WIDTH - uiPadding*2);
		uiTable.setHeight(Constants.VIRTUAL_HEIGHT - uiPadding*2);

		level = new Level();
		runner = new Runner(level, "pepper");
		charTable.addActor(runner);
		charTable.addActor(runner.ability1.effect);
		charTable.addActor(runner.ability2.effect);
		//charTable.addActor(runner.ability3.effect);

		//Set up UI:
		Button jumpBtnTransparent = new Button(Assets.I.skin, "transparent");
		int jumpBtnTransparentWidth = 470;
		jumpBtnTransparent.setTouchable(Touchable.enabled);
		hintLabel = new Label("press on the left side of the screen to 'jump'", Assets.I.skin, "default");
		hintLabel.setWrap(true);
		hintLabel.setTouchable(Touchable.disabled);
		jumpBtnTransparent.add(hintLabel).width(jumpBtnTransparentWidth).top();
		jumpBtnTransparent.top();
		//TextButton jumpButton = new TextButton ("JUMP", Assets.I.skin, "default");
		//TODO: try something else to pass touch event to this button
		//jumpButton.setTouchable(Touchable.disabled);
		//jumpBtnTransparent.add(jumpButton);
		//jumpBtnTransparent.bottom().left();
		jumpBtnTransparent.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (! ((WorldStage) event.getStage()).playerReady) {
					hintLabel.addAction(Actions.fadeOut(0.48f));
					level.beginLevel = true;
				}
				runner.jump();
				event.cancel();
				return true;
			}
		});
		uiTable.add(jumpBtnTransparent).width(jumpBtnTransparentWidth).height(uiTable.getHeight()).expandX().left();
		//Attack Buttons
		attackButtons = new Table();
		attackButtons.add(runner.ability3.table).expandY().right().top();
		attackButtons.row();
		attackButtons.add(runner.ability2.table).expandY().right().center();
		attackButtons.row();
		attackButtons.add(runner.ability1.table).expandY().right().bottom();
		attackButtons.row();

		uiTable.add(attackButtons).height(uiTable.getHeight()).expand().right();

		this.addActor(charTable);
		this.addActor(uiTable);
	}

	public void render(float delta){
		//Ui table moves with main game camera, so it appears ui
		//stays always on the same place.
		uiTable.setPosition(PaCGame.getInstance().camera.position.x - Constants.VIRTUAL_WIDTH/2,
				PaCGame.getInstance().camera.position.y - Constants.VIRTUAL_HEIGHT/2);

		this.act(delta);
		if (!runner.isDying() && !gamePaused){
			level.update();
		}else{
			switchToLoseScreen();
		}

		level.renderBackground();
		this.draw();
		level.renderEntities(delta);
		level.renderForeground();
	}

	public void switchToLoseScreen(){
		if (!gamePaused) {
			uiTable.setTouchable(Touchable.disabled);
			//fade out animation
			float fadeOutTime = 0.48f;
			getRoot().getColor().a = 1;
		    SequenceAction sequenceAction = new SequenceAction();
		    sequenceAction.addAction( Actions.fadeOut(fadeOutTime) );
		    sequenceAction.addAction( Actions.run(new Runnable() {
		        @Override
		        public void run() {
		        	PaCGame.getInstance().setScreen(new LoseScreen(getViewport()));
		        }
		    }));
		    getRoot().addAction(sequenceAction);
		    gamePaused = true;
		}
	}
}
