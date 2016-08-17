package com.peppercarrot.runninggame.screens;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AddAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.peppercarrot.runninggame.overworld.OverworldLayout;
import com.peppercarrot.runninggame.overworld.OverworldLayoutLoader;
import com.peppercarrot.runninggame.overworld.OverworldLevelNode;
import com.peppercarrot.runninggame.overworld.OverworldStage;
import com.peppercarrot.runninggame.overworld.OverworldStage.OnLevelSelectListener;
import com.peppercarrot.runninggame.overworld.OverworldStoryNode;
import com.peppercarrot.runninggame.stages.AbstractStage;
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;

public class OverworldScreen extends ScreenAdapter {
	//private static final AssetManager assetManager = new AssetManager();

	//private final OverworldStage stage;
	private Stage rootStage;
	ImageButton[] levelButtons;

	public OverworldScreen() {
		/*
		final OverworldLayout layout = loadLayout("pac.ol");
		stage = new OverworldStage(layout);
		stage.setListener(new OnLevelSelectListener() {

			@Override
			public void onStorySelect(OverworldStoryNode story) {
				ScreenSwitch.getInstance().setStoryScreen(story.getStoryboard());
			}

			@Override
			public void onLevelSelect(OverworldLevelNode level) {
				ScreenSwitch.getInstance().setWorldScreen();
			}
		});
		*/
		rootStage = new Stage(DefaultScreenConfiguration.getInstance().getViewport());
		Table rootTable = new Table();
		rootTable.setFillParent(true);
		rootStage.addActor(rootTable);
		Texture texture;
		texture = new Texture(Gdx.files.internal("world.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image background = new Image(texture);
		rootTable.addActor(background);
		Image blockedI = new Image(Assets.I.atlas.findRegion("level_not_available"));
		Image solvedI = new Image(Assets.I.atlas.findRegion("level_done"));
		Image todoI = new Image(Assets.I.atlas.findRegion("level_new"));

		levelButtons = new ImageButton[10]; //TODO: variable amount of levels
		int progress = Account.I.progress;
		for (int lvlID = 0; lvlID < this.levelButtons.length && lvlID < 10; lvlID++) {
			if (lvlID <= progress) {
				//done levels
				this.levelButtons[lvlID] = new ImageButton(solvedI.getDrawable());
			} else if (lvlID == progress+1) {
				//the very next level
				this.levelButtons[lvlID] = new ImageButton(todoI.getDrawable());
			} else {
				//other levels are still blocked
				this.levelButtons[lvlID] = new ImageButton(blockedI.getDrawable());
				this.levelButtons[lvlID].setTouchable(Touchable.disabled);
			}
			rootTable.addActor(this.levelButtons[lvlID]);
		}
	    //manually setting the places where the buttons will be placed/displayed
		this.levelButtons[0].setPosition(316, 520);
	    this.levelButtons[1].setPosition(394, 366);
	    this.levelButtons[2].setPosition(354, 234);
	    this.levelButtons[3].setPosition(198, 356);
	    this.levelButtons[4].setPosition(324, 70);
	    this.levelButtons[5].setPosition(570, 124);
	    this.levelButtons[6].setPosition(610, 314);
	    this.levelButtons[7].setPosition(680, 450);
	    this.levelButtons[8].setPosition(860, 296);
	    this.levelButtons[9].setPosition(1032, 196);
	    addListeners();
	}

	@Override
	public void show() {
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(MainMenu.getInstance());
		//multi.addProcessor(stage);
		multi.addProcessor(rootStage);
		
		Gdx.input.setInputProcessor(multi);
	}

	@Override
	public void render(float delta) {
		//stage.render(delta);
		rootStage.act();
		rootStage.draw();
		MainMenu.getInstance().render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Account.I.exit();
		}
	}
/*
	private OverworldLayout loadLayout(String layoutName) {
		if (!assetManager.isLoaded(layoutName)) {
			assetManager.setLoader(OverworldLayout.class, new OverworldLayoutLoader());
			assetManager.load("pac.ol", OverworldLayout.class);
			assetManager.finishLoading();
		}

		return assetManager.get(layoutName);
	}
*/

	public void addListeners(){		
		this.levelButtons[0].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 0;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[1].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[2].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[3].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[4].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[5].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[6].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[7].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[8].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx"));
				event.cancel();
			}
		});
		this.levelButtons[9].addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.startedLvlID = 1;
				ScreenSwitch.getInstance().setWorldScreen(Arrays.asList("level1.tmx","level2.tmx","level3.tmx",
						"level4.tmx","level5.tmx","level6.tmx","level7.tmx"));
				event.cancel();
			}
		});
	}
}
