package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Renders a tiled layer. Does only work with the level segment as a parent,
 * since the renderer needs to be configured correctly.
 * 
 * TODO: define property for z-index
 * 
 * @author momsen
 *
 */
public class TmxLayerActor extends Actor {

	private final TiledMapTileLayer layer;

	private final TiledMapRenderer renderer;

	public TmxLayerActor(TiledMapTileLayer layer, TiledMapRenderer renderer) {
		this.layer = layer;
		this.renderer = renderer;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		renderer.renderTileLayer(layer);
	}
}
