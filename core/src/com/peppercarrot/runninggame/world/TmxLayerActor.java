package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

/**
 * Renders a tiled layer.
 * 
 * @author momsen
 *
 */
public class TmxLayerActor extends Group {

	private final class DrawableActor extends Actor {
		final Vector3 tempPosition = new Vector3();

		@Override
		public void draw(Batch batch, float parentAlpha) {
			final Matrix4 computeTransform = computeTransform();
			computeTransform.getTranslation(tempPosition);
			TmxLayerActor.this.renderer.getViewBounds().set(-tempPosition.x, -tempPosition.y,
					TmxLayerActor.this.camera.viewportWidth * computeTransform.getScaleX(),
					TmxLayerActor.this.camera.viewportHeight * computeTransform.getScaleY());
			TmxLayerActor.this.renderer.renderTileLayer(TmxLayerActor.this.layer);
		}
	}

	private final TiledMapTileLayer layer;

	private final BatchTiledMapRenderer renderer;

	private final Camera camera;

	public TmxLayerActor(TiledMapTileLayer layer, BatchTiledMapRenderer renderer, Camera camera) {
		this.layer = layer;
		this.renderer = renderer;
		this.camera = camera;
		addActor(new DrawableActor());
	}
}
