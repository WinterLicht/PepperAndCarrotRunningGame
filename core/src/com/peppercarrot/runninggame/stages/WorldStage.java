package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Level;
import com.peppercarrot.runninggame.entities.Runner;
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
		runner = new Runner(level);
		charTable.addActor(runner);
		charTable.addActor(runner.ability1.effect);

		//Set up UI:
		Button jumpBtnTransparent = new Button(Assets.I.skin, "transparent");
		int jumpBtnTransparentWidth = 470;
		jumpBtnTransparent.setTouchable(Touchable.enabled);
		//TextButton jumpButton = new TextButton ("JUMP", Assets.I.skin, "default");
		//TODO: try something else to pass touch event to this button
		//jumpButton.setTouchable(Touchable.disabled);
		//jumpBtnTransparent.add(jumpButton);
		//jumpBtnTransparent.bottom().left();
		jumpBtnTransparent.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (! ((WorldStage) event.getStage()).playerReady) {
					level.beginLevel = true;
				}
				runner.jump();
				event.cancel();
				return true;
			}
		});
		jumpBtnTransparent.debug();
		uiTable.add(jumpBtnTransparent).width(jumpBtnTransparentWidth).height(uiTable.getHeight()).expandX().left();
		//Attack Buttons
		attackButtons = new Table();
		attackButtons.debug();
		int attackBtnWidth = 470;
		attackButtons.setWidth(attackBtnWidth);
		for (int i = 1; i < 4; i++) {
			Button btn = new Button(Assets.I.skin, "transparent");
			btn.setTouchable(Touchable.enabled);
			btn.setName(""+i);
			btn.addListener(new InputListener() { //TODO: attacks
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					System.out.println("Attack Button " + event.getListenerActor().getName() + " touched.");
					runner.activateAbility(Integer.parseInt(event.getListenerActor().getName()));
					event.cancel();
					return false;
				}
			});
			attackButtons.add(btn).width(attackBtnWidth).height(uiTable.getHeight()/3).right();
			attackButtons.row();
		}
		uiTable.add(attackButtons).width(attackBtnWidth).height(uiTable.getHeight()).right();
		uiTable.debug();
		System.out.println("UiTable: "+uiTable.getX());

		this.addActor(uiTable);
		this.addActor(charTable);
	}

	public void render(float delta){
		//Ui table moves with main game camera, so it appears ui
		//stays always on the same place.
		uiTable.setPosition(PaCGame.getInstance().camera.position.x - Constants.VIRTUAL_WIDTH/2,
				PaCGame.getInstance().camera.position.y - Constants.VIRTUAL_HEIGHT/2);

		this.act(delta);
		level.update();

		level.renderBackground();
		this.draw();
		level.renderEnemies(delta);
		level.renderForeground();
		runner.checkCollision(level.getWallsYPosNearPlayer());
	}

	public void beginLevel(){
		
	}
}
