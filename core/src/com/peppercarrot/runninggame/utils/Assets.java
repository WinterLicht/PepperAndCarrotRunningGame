package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
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
	}

	/**
	 * Add everything to be loaded.
	 */
	public void prepareForLoading() {
		TextureParameter tParam = new TextureParameter();
		tParam.minFilter = TextureFilter.MipMapLinearNearest;
		tParam.genMipMaps = true;
		
		manager = new AssetManager();
		manager.load("skin.atlas", TextureAtlas.class);
		manager.load("skin.json", Skin.class);
		manager.load("texture.png", Texture.class, tParam);
		manager.load("top_bg.png", Texture.class, tParam);
		manager.load("tree.png", Texture.class, tParam);
	}

	/**
	 * After assets are ready.
	 */
	public void setUp() {
		bgTexture = new Image(manager.get("texture.png", Texture.class));
		bgTopTexture = new Image(manager.get("top_bg.png", Texture.class));
		bgTopTexture.setY(Constants.VIRTUAL_HEIGHT-bgTopTexture.getHeight());
		evolutionSketch = new Image(manager.get("tree.png", Texture.class));
		
		atlas = manager.get("skin.atlas", TextureAtlas.class);
		skin = manager.get("skin.json", Skin.class);
	}

	public Array<TextureAtlas.AtlasRegion> getRegions(String name) {
		Array<TextureAtlas.AtlasRegion> regions;
		regions = atlas.findRegions(name);
		return regions;
	}
}
