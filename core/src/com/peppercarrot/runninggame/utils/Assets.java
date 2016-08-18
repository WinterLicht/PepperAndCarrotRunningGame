package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Stores assets and skin.
 * 
 * @author WinterLicht
 *
 */
public enum Assets {
	I; // Singleton

	public AssetManager manager;
	public Skin skin;
	public TextureAtlas atlas;

	public Image bgTexture;
	public Image bgTopTexture;

	public Image evolutionSketch;

	Assets() {
		setUp();
	}

	public void setUp() {
		manager = new AssetManager();
		manager.load("skin.atlas", TextureAtlas.class);
		manager.finishLoading();
		atlas = manager.get("skin.atlas", TextureAtlas.class);
		skin = new Skin(Gdx.files.internal("skin.json"));

		Texture texture;
		texture = new Texture(Gdx.files.internal("texture.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		bgTexture = new Image(texture);
		texture = new Texture(Gdx.files.internal("top_bg.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		bgTopTexture = new Image(texture);
		bgTopTexture.setY(Constants.VIRTUAL_HEIGHT-bgTopTexture.getHeight());

		texture = new Texture(Gdx.files.internal("tree.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		evolutionSketch = new Image(texture);
	}

	public Array<TextureAtlas.AtlasRegion> getRegions(String name) {
		Array<TextureAtlas.AtlasRegion> regions;
		regions = atlas.findRegions(name);
		return regions;
	}
}
