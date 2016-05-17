package com.peppercarrot.runninggame.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
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

	private final OrthographicCamera camera;

	private final TiledMapRenderer renderer;

	private final String assetName;

	private final int segmentWidth;

	private final List<Potion> potions = new ArrayList<Potion>();

	private final List<Enemy> enemies = new ArrayList<Enemy>();

	private final List<Platform> platforms = new ArrayList<Platform>();

	public LevelSegment(OrthographicCamera camera, String assetName, float startOffset, TiledMap map,
			TiledMapRenderer renderer) {
		this.camera = camera;
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
					enemies.addAll(createEnemies(tiledLayer, tilewidth, tileheight));
				} else if ("potions".equals(name)) {
					potions.addAll(createPotions(tiledLayer, tilewidth, tileheight));
				} else {
					addActor(new TmxLayerActor(tiledLayer, renderer));
					platforms.addAll(extractPlatforms(tiledLayer, tilewidth, tileheight));
				}
			}
			// TODO: Allow image-layers, espacially for enemies and potions
		}

		platforms.sort(new Platform.XPositionComparator());
		enemies.sort(new ActorXPositionComparator());
		potions.sort(new ActorXPositionComparator());
		setX(startOffset);
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

	private Collection<Enemy> createEnemies(TiledMapTileLayer layer, int tilewidth, int tileheight) {
		final List<Enemy> enemiesInLayer = new ArrayList<Enemy>();
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				// TODO: recognize which enemy by cell property
				if (cell != null) {
					final Enemy enemy = createEnemy(column, row, tilewidth, tileheight, centerOffsetX, centerOffsetY);
					enemiesInLayer.add(enemy);
				}
			}
		}
		return enemiesInLayer;
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

	private List<Potion> createPotions(TiledMapTileLayer layer, Integer tilewidth, Integer tileheight) {
		final List<Potion> potionsInLayer = new ArrayList<Potion>();
		final float centerOffsetX = tilewidth / 2.0f;
		final float centerOffsetY = tileheight / 2.0f;

		for (int column = 0; column < layer.getWidth(); column++) {
			for (int row = 0; row < layer.getHeight(); row++) {

				final Cell cell = layer.getCell(column, row);
				// TODO: recognize which potion by cell property
				if (cell != null) {
					final Potion potion = createPotion(column, row, tilewidth, tileheight, centerOffsetX,
							centerOffsetY);
					potionsInLayer.add(potion);
				}
			}
		}
		return potionsInLayer;
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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (isTransform()) {
			applyTransform(batch, computeTransform());
		}

		renderer.setView(camera.combined, -getX(), -getY(), camera.viewportWidth, camera.viewportHeight);
		drawChildren(batch, parentAlpha);

		if (isTransform()) {
			resetTransform(batch);
		}
	}

	/**
	 * Gets the base asset name of which this segment is loaded
	 * 
	 * @return asset name
	 */
	public String getAssetName() {
		return assetName;
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
