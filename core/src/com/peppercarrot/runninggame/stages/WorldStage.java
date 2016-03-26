package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Background;
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
	Level level;

	public WorldStage(Viewport viewport){
		super(viewport);
		charTable = new Table();
		charTable.setFillParent(true);

		int uiPadding = 60; //padding of borders for ui in pixel
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiTable.pad(uiPadding);
		uiTable.setWidth(Constants.VIRTUAL_WIDTH - uiPadding*2);
		uiTable.setHeight(Constants.VIRTUAL_HEIGHT - uiPadding*2);

		level = new Level();
		runner = new Runner();
		charTable.addActor(runner);

		//Set up UI:
		TextButton jumpButton = new TextButton ("JUMP", Assets.I.skin, "default");
		jumpButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				runner.jump();
				event.cancel();
			}
		});

		Background background = new Background(); //infinitely scrolling background
		uiTable.addActor(background); //TODO: background belongs not to ui...
		//because it should not updated as ui vertically!
		uiTable.add(jumpButton);
		uiTable.bottom().left();

		this.addActor(uiTable);
		this.addActor(charTable);
	}

	public void render(float delta){
		//Ui table moves with main game camera, so it appears ui
		//stays always on the same place.
		uiTable.setPosition(PaCGame.getInstance().camera.position.x - Constants.VIRTUAL_WIDTH/2,
				PaCGame.getInstance().camera.position.y - Constants.VIRTUAL_HEIGHT/2);

		this.act(delta);
		this.draw();
		//TODO: render player over platforms !!!
		level.render();
		runner.checkCollision(level.getPlatforms());
	}

}
