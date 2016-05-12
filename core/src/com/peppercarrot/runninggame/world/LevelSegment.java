package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;

/**
 * A single level segment loaded from a tile map
 * 
 * @author momsen
 *
 */
public class LevelSegment extends Group {

	private final TiledMapRenderer renderer;

	private final String assetName;

	private final int segmentWidth;

	public LevelSegment(String assetName, float startOffset, TiledMap map, TiledMapRenderer renderer) {
		this.assetName = assetName;
		this.renderer = renderer;

		final Integer tilewidth = map.getProperties().get("tilewidth", Integer.class);
		final Integer tileheight = map.getProperties().get("tileheight", Integer.class);
		segmentWidth = map.getProperties().get("width", Integer.class) * tilewidth;

		for (final MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
				final String name = layer.getName();

				// TODO: Merge layers and create entities by tag, name or
				// property. Then we'll need only one method for iterating over
				// tiles
				if ("enemies".equals(name)) {
					createEnemies(tiledLayer, tilewidth, tileheight);
				} else if ("potions".equals(name)) {
					createPotions(tiledLayer, tilewidth, tileheight);
				} else {
					addActor(new TmxLayerActor(tiledLayer, renderer));
				}
			}
			// TODO: Allow image-layers, espacially for enemies and potions
		}

		setX(startOffset);
	}

	/**
	 * Gets the base asset name of which this segment is loaded
	 * 
	 * @return asset name
	 */
	public String getAssetName() {
		return assetName;
	}

	private void createEnemies(TiledMapTileLayer layer, int tilewidth, int tileheight) {
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				// TODO: recognize which enemy by cell property
				if (cell != null) {
					createEnemy(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY);
				}
			}
		}
	}

	private void createEnemy(int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 1f) * tileheight;
		final Enemy enemy = new Enemy("fly");
		enemy.setOrigin(Align.center);
		enemy.setX(posX - enemy.getWidth() / 2);
		enemy.setY(posY - enemy.getHeight() / 2);
		addActor(enemy);
	}

	private void createPotions(TiledMapTileLayer layer, Integer tilewidth, Integer tileheight) {
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				// TODO: recognize which potion by cell property
				if (cell != null) {
					createPotion(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY);
				}
			}
		}
	}

	private void createPotion(int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 1f) * tileheight;
		final Potion potion = new Potion(1);
		potion.setOrigin(Align.center);
		potion.setX(posX - potion.getWidth() / 2);
		potion.setY(posY - potion.getHeight() / 2);
		addActor(potion);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (isTransform()) {
			applyTransform(batch, computeTransform());
		}

		final OrthographicCamera baseCamera = PaCGame.getInstance().camera;
		renderer.setView(baseCamera.combined, -getX(), -getY(), baseCamera.viewportWidth, baseCamera.viewportHeight);
		drawChildren(batch, parentAlpha);

		if (isTransform()) {
			resetTransform(batch);
		}
	}

	/**
	 * Moves this segment to the left by a specific offset
	 * 
	 * @param offset
	 */
	public void moveLeft(float offset) {
		setX(getX() - offset);
	}

	/**
	 * Returns the right position of this segment.
	 * 
	 * @return right outer x position
	 */
	public float getRightX() {
		return getX() + segmentWidth;
	}
}
