package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Shelve with ingredients.
 * 
 * @author WinterLicht
 *
 */
public class StartStage extends AbstractStage {
	Table rootTable;
	boolean goToWorldMap;
	ScrollPane shelve;

	public StartStage() {
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.setWidth(Constants.VIRTUAL_WIDTH-2*MainMenu.getInstance().buttonWidth);
		rootTable.setHeight(Constants.VIRTUAL_HEIGHT);
		rootTable.padRight(MainMenu.getInstance().buttonWidth+60);
		rootTable.padLeft(MainMenu.getInstance().buttonWidth+60);
		rootTable.top();

		Table container = new Table();
		container.pad(5);
		shelve = new ScrollPane(container, Assets.I.skin);
		shelve.setScrollingDisabled(false, true);
		shelve.setVisible(false);
		//Fill the shelve
		for (int i = 0; i < 30; i++) {
			String color = "";
			double r = Math.random();
			if (r < 0.5) {
				color = "orange";
			} else {
				color = "green";
			}
			Image potionImage = new Image(new TextureRegion(Assets.I.atlas.findRegion("potion_"+color)));
			container.add(potionImage).width(potionImage.getWidth());
		}

		TextButton shelvebtn = new TextButton("ingredients", Assets.I.skin, "default");
		shelvebtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				toggleShelve();
				event.cancel();
			}
		});
		rootTable.add(shelvebtn);
		rootTable.row();
		rootTable.add(shelve).top();

		this.addActor(rootTable);
	}

	private void toggleShelve() {
		shelve.setVisible(!shelve.isVisible());
	}

	/**
	 * 
	 * @param delta
	 */
	public void render(float delta) {
		this.act(delta);
		this.draw();
	}

	/*
	public void switchScreen(float fadeOutTime) {
		fadeOut(true, fadeOutTime, new Runnable() {
			@Override
			public void run() {
				ScreenSwitch.getInstance().setWorldScreen();
			}
		});
	}
	*/
}
