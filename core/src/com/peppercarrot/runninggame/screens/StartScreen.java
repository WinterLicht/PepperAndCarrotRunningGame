package com.peppercarrot.runninggame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.peppercarrot.runninggame.stages.MainMenu;
import com.peppercarrot.runninggame.stages.StartStage;
import com.peppercarrot.runninggame.utils.Account;

/**
 * The game starts in Pepper's kitchen.
 * TODO: ghost, peppers idle animation, ghost evolution etc...
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class StartScreen extends ScreenAdapter {
	StartStage stage;
	Image backgroundImage;

	public StartScreen() {
		stage = new StartStage();
		Texture texture;
		texture = new Texture(Gdx.files.internal("kitchen.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		backgroundImage = new Image(texture);
	}

	@Override
	public void show() {
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(MainMenu.getInstance());
		multi.addProcessor(stage);
		
		Gdx.input.setInputProcessor(multi);
	}

	@Override
	public void render(float delta) {
		final Batch batch = DefaultScreenConfiguration.getInstance().getBatch();
		batch.begin();
		batch.setColor(1, 1, 1, 1);
		backgroundImage.act(delta);
		backgroundImage.draw(batch, 1f);
		batch.end();

		stage.render(delta);
		MainMenu.getInstance().render(delta);

		if (Gdx.input.isKeyJustPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Account.I.exit();
		}
	}
}
