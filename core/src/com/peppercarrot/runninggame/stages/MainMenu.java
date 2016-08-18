package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.screens.ScreenSwitch;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * 0 - exit
 * 1 - kitchen (startscreen)
 * 2 - info / help / tutorial / (infoscreen)
 * 3 - settings / credits (settingsscreen)
 * 4 - basement / select level (overworldscreen)
 * 5 - statistics / magic book / achievements / information about the ghost & ingredients /  character info (achievementsscreen)
 * 
 * @author WinterLicht
 *
 */
public class MainMenu extends Stage {
	private static MainMenu instance;

	public static void initialize(Viewport viewport) {
		instance = new MainMenu(viewport);
	}

	public static MainMenu getInstance() {
		return instance;
	}

	private ButtonGroup<TextButton> buttons;
	public int buttonWidth = 160;

	private MainMenu(Viewport viewport) {
		super(viewport);
		Table rootTable = new Table();
		rootTable.setFillParent(true);
		Image bg_right = new Image(Assets.I.atlas.findRegion("main_menu_bg"));
		AtlasRegion main_menu_bg = new AtlasRegion(Assets.I.atlas.findRegion("main_menu_bg"));
		main_menu_bg.flip(true, false);
		Image bg_left = new Image(main_menu_bg);
		bg_right.setX(Constants.VIRTUAL_WIDTH-bg_right.getWidth());
		rootTable.addActor(bg_left);
		rootTable.addActor(bg_right);
		buttons = new ButtonGroup<TextButton>();
		for (int n = 0; n < 6; n++) {
			TextButton button = new TextButton("", Assets.I.skin, "transparent-bg");
			button.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					// previously checked button is accessible with buttons.getChecked
					int currentlyTouchedBtn = Integer.parseInt(buttons.getAllChecked().get(buttons.getAllChecked().size-1).getName());
					if (currentlyTouchedBtn != buttons.getCheckedIndex()) {
						setChecked(currentlyTouchedBtn);
						switchScreen(currentlyTouchedBtn);
					}
					event.cancel();
				}
			});
			button.setWidth(buttonWidth);
			button.setHeight(Constants.VIRTUAL_HEIGHT/3);
			button.setName(""+n);
			switch(n) {
			case 0:
				button.setX(0);
				button.setY(Constants.VIRTUAL_HEIGHT-Constants.VIRTUAL_HEIGHT/3);
				button.setText("Exit");
				break;
			case 1:
				button.setX(0);
				button.setY(Constants.VIRTUAL_HEIGHT/3);
				button.setText("Kitchen");
				break;
			case 2:
				button.setX(0);
				button.setY(0);
				button.setText("Info");
				break;
			case 3:
				button.setX(Constants.VIRTUAL_WIDTH-buttonWidth);
				button.setY(Constants.VIRTUAL_HEIGHT-Constants.VIRTUAL_HEIGHT/3);
				button.setText("Settings");
				break;
			case 4:
				button.setX(Constants.VIRTUAL_WIDTH-buttonWidth);
				button.setY(Constants.VIRTUAL_HEIGHT/3);
				button.setText("Basement");
				break;
			case 5:
				button.setX(Constants.VIRTUAL_WIDTH-buttonWidth);
				button.setY(0);
				button.setText("Statistics");
				break;
			}
			buttons.setMaxCheckCount(2);
			buttons.add(button);
			rootTable.addActor(button);
		}
		setChecked(1);
		this.addActor(rootTable);
	}

	private void switchScreen(int n) {
		switch (n) {
		case 0:
			Account.I.exit();
			break;
		case 1:
			ScreenSwitch.getInstance().setStartScreen();
			break;
		case 2:
			ScreenSwitch.getInstance().setInfoScreen();
			break;
		case 3:
			ScreenSwitch.getInstance().setSettingsScreen();
			break;
		case 4:
			ScreenSwitch.getInstance().setOverworldScreen();
			break;
		case 5:
			ScreenSwitch.getInstance().setAchievementsScreen();
			break;
		}
	}

	public void setChecked(int n) {
		buttons.uncheckAll();
		buttons.getButtons().get(n).setChecked(true);
	}

	public void render(float delta){
		this.act(delta);
		this.draw();
	}
}
