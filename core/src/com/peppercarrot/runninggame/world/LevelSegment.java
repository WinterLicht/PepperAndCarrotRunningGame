package com.peppercarrot.runninggame.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;
import com.peppercarrot.runninggame.utils.ActorXPositionComparator;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.LevelSegment.Platform.PlatformXPositionComparator;

/**
 * A single level segment loaded from a tile map
 * 
 * @author momsen
 *
 */
public class LevelSegment {

	public static class Platform {

		private final float w;
		private final float h;
		private final Vector2 relativePosition;
		private final LevelSegment segment;

		public Platform(LevelSegment segment, float x, float y, float w, float h) {
			this.segment = segment;
			this.relativePosition = new Vector2(x, y);
			this.w = w;
			this.h = h;
		}

		public void retrieveAbsolutePosition(Vector2 position) {
			position.set(relativePosition);
			position.x += segment.getX();
			position.y += segment.getY();
		}

		public float getW() {
			return w;
		}

		public float getH() {
			return h;
		}

		public static class PlatformXPositionComparator implements Comparator<Platform> {

			@Override
			public int compare(Platform left, Platform right) {
				return Float.compare(left.relativePosition.x, right.relativePosition.x);
			}
		}
	}

	private final int segmentWidth;

	private final List<Potion> potions = new ArrayList<Potion>();

	private final List<Enemy> enemies = new ArrayList<Enemy>();

	private final List<Platform> platforms = new ArrayList<Platform>();

	private final String assetName;

	private final List<Actor> actors = new ArrayList<Actor>();

	private float x;

	private float y;

	private final Map<Actor, Integer> zIndexMap = new HashMap<Actor, Integer>();

	public LevelSegment(OrthographicCamera camera, String assetName, TiledMap map, BatchTiledMapRenderer renderer) {
		this.assetName = assetName;

		final Integer tilewidth = map.getProperties().get("tilewidth", Integer.class);
		final Integer tileheight = map.getProperties().get("tileheight", Integer.class);
		segmentWidth = map.getProperties().get("width", Integer.class) * tilewidth;

		for (final MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
				final MapProperties properties = layer.getProperties();

				final int zIndex = getIntProperty(properties, "z-index", 0);
				if (layer.isVisible()) {
					final TmxLayerActor actor = new TmxLayerActor(tiledLayer, renderer, camera);
					zIndexMap.put(actor, zIndex);
					actors.add(actor);
				}

				final boolean eventLayer = getBooleanProperty(properties, "events", false);
				if (eventLayer) {
					extractEntities(tiledLayer, tilewidth, tileheight, zIndex);
				}

				final boolean collisionLayer = getBooleanProperty(properties, "platforms", false);
				if (collisionLayer) {
					platforms.addAll(extractPlatforms(tiledLayer, tilewidth, tileheight));
				}
			}
		}

		Collections.sort(platforms, new PlatformXPositionComparator());
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

	private void extractEntities(TiledMapTileLayer layer, int tilewidth, int tileheight, int layerZIndex) {
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				if (cell != null) {
					final String type = cell.getTile().getProperties().get("type", String.class);
					final int zIndex = getIntProperty(cell.getTile().getProperties(), "z-index", layerZIndex);

					if ("enemy".equals(type)) {
						enemies.add(
								createEnemy(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY, zIndex));
					}

					if ("potion".equals(type)) {
						final String potionColor = cell.getTile().getProperties().get("color", String.class);
						potions.add(createPotion(potionColor, column, row, tilewidth, tileheight, centerOffsetX,
								centerOffsetY, zIndex));
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
			float centerOffsetY, int zIndex) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 0.5f) * tileheight;
		// TODO: various types
		final Enemy enemy = new Enemy("fly");
		enemy.setOrigin(Align.center);
		enemy.setX(posX - enemy.getWidth() / 2);
		enemy.setY(posY - enemy.getHeight() / 2);
		zIndexMap.put(enemy, zIndex);
		actors.add(enemy);

		return enemy;
	}

	private Potion createPotion(String color, int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY, int zIndex) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 0.5f) * tileheight;
		final Potion potion = new Potion(color);
		potion.setOrigin(Align.center);
		potion.setX(posX - potion.getWidth() / 2);
		potion.setY(posY - potion.getHeight() / 2);
		zIndexMap.put(potion, zIndex);
		actors.add(potion);

		return potion;
	}

	/**
	 * Moves this segment to the left by a specific offset
	 * 
	 * @param offset
	 */
	public void moveLeft(float offset) {
		x = x - offset;
		for (final Actor actor : actors) {
			actor.setX(actor.getX() - offset);
		}
	}

	/**
	 * Returns the right position of this segment.
	 * 
	 * @return right outer x position
	 */
	public float getRightX() {
		return x + segmentWidth;
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

	/**
	 * Adds all segment actors to the world group based on the z index of the
	 * given runner.
	 * 
	 * @param worldGroup
	 * @param runner
	 * @param segmentOffsetX
	 * @param segmentOffsetY
	 */
	public void addToWorld(Group worldGroup, Actor runner, float segmentOffsetX, float segmentOffsetY) {
		x = segmentOffsetX;
		y = segmentOffsetY;

		Collections.sort(actors, new Comparator<Actor>() {
			final Integer integerZero = Integer.valueOf(0);

			@Override
			public int compare(Actor l, Actor r) {
				if (zIndexMap.containsKey(l)) {
					final Integer zIndexL = zIndexMap.get(l);
					if (zIndexMap.containsKey(r)) {
						final Integer zIndexR = zIndexMap.get(r);
						return zIndexL.compareTo(zIndexR);
					}

					return zIndexL.compareTo(0);
				}

				if (zIndexMap.containsKey(r)) {
					final Integer zIndexR = zIndexMap.get(r);
					return integerZero.compareTo(zIndexR);
				}

				return 0;
			}
		});

		for (final Actor actor : actors) {
			actor.setX(actor.getX() + segmentOffsetX);
			actor.setY(actor.getY() + segmentOffsetY);
			worldGroup.addActor(actor);

			final int index = zIndexMap.containsKey(actor) ? zIndexMap.get(actor) : 0;
			if (index < Constants.RUNNER_ZINDEX) {
				actor.setZIndex(runner.getZIndex() - 1);
			} else {
				actor.setZIndex(runner.getZIndex() + 1);
			}
		}
	}

	public void removeFromWorld(Group worldGroup) {
		for (final Actor actor : actors) {
			worldGroup.removeActor(actor);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}
