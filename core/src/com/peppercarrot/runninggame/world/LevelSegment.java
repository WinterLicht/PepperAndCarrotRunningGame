package com.peppercarrot.runninggame.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;
import com.peppercarrot.runninggame.utils.ActorXPositionComparator;

/**
 * A single level segment loaded from a tile map
 * 
 * @author momsen
 *
 */
public class LevelSegment extends Group {

	private final int segmentWidth;

	private final List<Potion> potions = new ArrayList<Potion>();

	private final List<Enemy> enemies = new ArrayList<Enemy>();

	private final List<Platform> platforms = new ArrayList<Platform>();

	private final String assetName;

	public LevelSegment(OrthographicCamera camera, String assetName, TiledMap map, BatchTiledMapRenderer renderer) {
		this.assetName = assetName;

		final Integer tilewidth = map.getProperties().get("tilewidth", Integer.class);
		final Integer tileheight = map.getProperties().get("tileheight", Integer.class);
		segmentWidth = map.getProperties().get("width", Integer.class) * tilewidth;

		for (final MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
				final MapProperties properties = layer.getProperties();

				final boolean renderLayer = getBooleanProperty(properties, "render", false);
				if (renderLayer) {
					final int zIndex = getIntProperty(properties, "z-index", 0);
					final TmxLayerActor actor = new TmxLayerActor(tiledLayer, renderer, camera);
					actor.setZIndex(zIndex);
					addActor(actor);
				}

				final boolean eventLayer = getBooleanProperty(properties, "events", false);
				if (eventLayer) {
					extractEnemies(tiledLayer, tilewidth, tileheight);
				}

				final boolean collisionLayer = getBooleanProperty(properties, "platforms", false);
				if (collisionLayer) {
					platforms.addAll(extractPlatforms(tiledLayer, tilewidth, tileheight));
				}
			}
		}

		Collections.sort(platforms, new Platform.XPositionComparator());
		Collections.sort(enemies, new ActorXPositionComparator());
		Collections.sort(potions, new ActorXPositionComparator());
	}

	/**
	 * Gets the base asset name of which this segment is loaded
	 * 
	 * @return asset name
	 */
	public String getAssetName() {
		return assetName;
	}

	private static boolean getBooleanProperty(MapProperties properties, String name, boolean defaultValue) {
		final String value = properties.get(name, String.class);
		if (value == null) {
			return defaultValue;
		}

		return Boolean.valueOf(value);
	}

	private static int getIntProperty(MapProperties properties, String name, int defaultValue) {
		final String value = properties.get(name, String.class);
		if (value == null) {
			return defaultValue;
		}

		return Integer.parseInt(value);
	}

	private void extractEnemies(TiledMapTileLayer layer, int tilewidth, int tileheight) {
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				if (cell != null) {
					final String type = cell.getTile().getProperties().get("type", String.class);
					if ("enemy".equals(type)) {
						enemies.add(createEnemy(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY));
					}
					if ("potion".equals(type)) {
						potions.add(createPotion(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY));
					}
				}
			}
		}
	}

	private Collection<Platform> extractPlatforms(TiledMapTileLayer tiledLayer, float tileWidth, float tileHeight) {
		final List<Platform> platformsInLayer = new ArrayList<>();
		for (int column = 0; column <= tiledLayer.getWidth(); column++) {
			for (int row = 0; row < tiledLayer.getHeight(); row++) {
				final Cell cell = tiledLayer.getCell(column, row);
				if (cell != null) {
					platformsInLayer
							.add(new Platform(this, column * tileWidth, row * tileHeight, tileWidth, tileHeight));
				}
			}
		}

		return platformsInLayer;
	}

	private Enemy createEnemy(int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 1f) * tileheight;
		final Enemy enemy = new Enemy("fly");
		enemy.setOrigin(Align.center);
		enemy.setX(posX - enemy.getWidth() / 2);
		enemy.setY(posY - enemy.getHeight() / 2);
		addActor(enemy);

		return enemy;
	}

	private Potion createPotion(int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 1f) * tileheight;
		final Potion potion = new Potion(1);
		potion.setOrigin(Align.center);
		potion.setX(posX - potion.getWidth() / 2);
		potion.setY(posY - potion.getHeight() / 2);
		addActor(potion);

		return potion;
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
		return getX() + segmentWidth * getScaleX();
	}

	/**
	 * Returns the list of all platforms ordered by x position.
	 * 
	 * @return list of all platforms
	 */
	public List<Platform> getPlatforms() {
		return platforms;
	}

	/**
	 * Returns the list of all enemies ordered by x position.
	 * 
	 * @return list of all enemies
	 */
	public List<Enemy> getEnemies() {
		return enemies;
	}

	/**
	 * Returns the list of all potions ordered by x position.
	 * 
	 * @return list of all potions
	 */
	public List<Potion> getPotions() {
		return potions;
	}
}
