package com.peppercarrot.runninggame.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.EnemySimple;
import com.peppercarrot.runninggame.entities.Potion;
import com.peppercarrot.runninggame.utils.ActorXPositionComparator;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.utils.ParticleEffectActor;
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

	private final List<ParticleEffectActor> pEmitters = new ArrayList<ParticleEffectActor>();

	private final String assetName;

	private final List<Actor> actors = new ArrayList<Actor>();

	private float x;

	private float y;

	private final Map<Actor, Integer> zIndexMap = new HashMap<Actor, Integer>();

	private final int tileWidth;

	public LevelSegment(OrthographicCamera camera, String assetName, TiledMap map, BatchTiledMapRenderer renderer) {
		this.assetName = assetName;

		tileWidth = map.getProperties().get("tilewidth", Integer.class);
		final Integer tileHeight = map.getProperties().get("tileheight", Integer.class);
		segmentWidth = map.getProperties().get("width", Integer.class) * tileWidth;

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
					extractEntities(tiledLayer, tileWidth, tileHeight, zIndex);
				}

				final boolean collisionLayer = getBooleanProperty(properties, "platforms", false);
				if (collisionLayer) {
					platforms.addAll(extractPlatforms(tiledLayer, tileWidth, tileHeight));
				}
				//Every Cell/Tile may have a particle emitter
				extractParticleEmitters(tiledLayer, tileWidth, tileHeight, zIndex);
			}
		}

		Collections.sort(platforms, new PlatformXPositionComparator());
		Collections.sort(enemies, new ActorXPositionComparator());
		Collections.sort(potions, new ActorXPositionComparator());
		Collections.sort(pEmitters, new ActorXPositionComparator());
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

	private void extractParticleEmitters(TiledMapTileLayer layer, int tilewidth, int tileheight, int layerZIndex) {
		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				if (cell != null) {
					final int zIndex = getIntProperty(cell.getTile().getProperties(), "z-index", layerZIndex);
					if (cell.getTile().getProperties().get("emitter") != null) {
						final String emitterName = cell.getTile().getProperties().get("emitter", String.class);
						ParticleEffectActor p = new ParticleEffectActor((column + 0.5f) * tilewidth,
								(row + 0.5f) * tileheight, emitterName);
						zIndexMap.put(p, zIndex+1);
						actors.add(p);
						pEmitters.add(p);
					}
				}
			}
		}
	}

	private void extractEntities(TiledMapTileLayer layer, int tilewidth, int tileheight, int layerZIndex) {
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				if (cell != null) {
					final int zIndex = getIntProperty(cell.getTile().getProperties(), "z-index", layerZIndex);
					if (cell.getTile().getProperties().get("type") != null) {
						final String type = cell.getTile().getProperties().get("type", String.class);
						if ("enemy".equals(type)) {
							final String enemyName = cell.getTile().getProperties().get("name", String.class);
							enemies.add(createEnemy(enemyName, column, row, tilewidth, tileheight, centerOffsetX,
									centerOffsetY, zIndex));
						}
						if ("potion".equals(type)) {
							final String name = cell.getTile().getProperties().get("color", String.class);
							potions.add(createPotion(name, Potion.Type.POTION, column, row, tilewidth, tileheight, centerOffsetX,
									centerOffsetY, zIndex));
						}
						if ("ingredient".equals(type)) {
							final String name = cell.getTile().getProperties().get("name", String.class);
							potions.add(createPotion(name, Potion.Type.INGREDIENT, column, row, tilewidth, tileheight, centerOffsetX,
									centerOffsetY, zIndex));
						}
					}
					if (cell.getTile().getProperties().get("obstacle") != null) {
						if (cell.getTile().getProperties().get("obstacle", String.class).equals("deadly")) {
							enemies.add(createObstacle(cell.getTile().getTextureRegion(), column, row, tilewidth,
									tileheight, centerOffsetX, centerOffsetY, zIndex));
						}
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

	private Enemy createObstacle(TextureRegion tRegion, int column, int row, int tilewidth, int tileheight,
			float centerOffsetX, float centerOffsetY, int zIndex) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 0.5f) * tileheight;
		final Enemy obstacle = new Enemy("", 6, posX, posY, true, tRegion);
		zIndexMap.put(obstacle, zIndex);
		actors.add(obstacle);
		return obstacle;
	}

	private Enemy createEnemy(String enemyName, int column, int row, int tilewidth, int tileheight, float centerOffsetX,
			float centerOffsetY, int zIndex) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 0.5f) * tileheight;
		switch (enemyName) {
		case "fly":
			final Enemy fly = new Enemy(enemyName, 1, posX, posY);
			zIndexMap.put(fly, zIndex);
			actors.add(fly);
			return fly;
		case "spider":
			final EnemySimple spider = new EnemySimple(enemyName, 1, posX, posY);
			zIndexMap.put(spider, zIndex);
			actors.add(spider);
			return spider;
		default:
			final Enemy defaultEnemy = new Enemy(enemyName, 1, posX, posY);
			zIndexMap.put(defaultEnemy, zIndex);
			actors.add(defaultEnemy);
			return defaultEnemy;
		}
	}

	private Potion createPotion(String name, Potion.Type type, int column, int row, int tilewidth,
			int tileheight, float centerOffsetX, float centerOffsetY, int zIndex) {
		final float posX = (column + 0.5f) * tilewidth;
		final float posY = (row + 0.5f) * tileheight;
		final Potion potion = new Potion(name, type);
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

	public int getTilesInRange(float left, float right) {
		left = MathUtils.clamp(left, 0, segmentWidth);
		right = MathUtils.clamp(right, 0, segmentWidth);

		return getTileIndexAt(right) - getTileIndexAt(left);
	}

	private int getTileIndexAt(float position) {
		return (int) (position / tileWidth);
	}

	public int getLengthInTiles() {
		return segmentWidth / tileWidth;
	}
}
