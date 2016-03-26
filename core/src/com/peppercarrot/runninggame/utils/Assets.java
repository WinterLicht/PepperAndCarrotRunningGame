package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * Stores assets and skin.
 * @author WinterLicht
 *
 */
public enum Assets {
	I; // Singleton

	public AssetManager manager;
	public Skin skin;
	public TextureAtlas atlas;
	
	Assets() {
		setUp();
	}

	public void setUp(){
		manager = new AssetManager();
		manager.load("skin.atlas", TextureAtlas.class);
		manager.finishLoading();
		atlas = manager.get("skin.atlas", TextureAtlas.class);
		skin = new Skin(Gdx.files.internal("skin.json"));
	}

	public Array<TextureAtlas.AtlasRegion> getRegions(String name){
		Array<TextureAtlas.AtlasRegion> regions;
		regions = atlas.findRegions(name);
		return regions;
	}
}
