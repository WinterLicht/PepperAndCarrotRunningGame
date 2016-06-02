package com.peppercarrot.runninggame.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Queue;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;

/**
 * Level stream. Appends a new segment, when a currently one is to be finished.
 * 
 * @author momsen
 *
 */
public class LevelStream extends Group {
	private static final String LOG_TAG = LevelStream.class.getSimpleName();

	/**
	 * TODO: The segments have to be seperated by difficulty.
	 */
	private final List<String> allFiles = Arrays.asList("level1.tmx", "level2.tmx");

	private int lastLoadedIndex = 0;

	private int levelIndex = 0;

	/**
	 * Asset manager to load all segment assets (tile maps etc)
	 */
	private final AssetManager assetManager = new AssetManager();

	/**
	 * All segments in this scene
	 */
	private final Queue<LevelSegment> segments = new Queue<LevelSegment>();

	/**
	 * All segments that should be removed during this frame
	 */
	private final List<LevelSegment> segmentsToRemove = new ArrayList<LevelSegment>();

	/**
	 * Name of the currently loaded asset
	 */
	private String currentlyLoadedSegmentName;

	/**
	 * Never call {@link TiledMapRenderer#render()} on this instance, since the
	 * map object will always be <code>null</code>
	 */
	private final BatchTiledMapRenderer renderer;

	/**
	 * Offset at which new segments should start (after the last segment)
	 */
	private final float segmentStartOffset;

	/**
	 * Offset at which the first segment should start in addition to
	 * {@link #segmentStartOffset}.
	 */
	private final float firstSegmentAdditionalStartOffset;

	/**
	 * World camera to render the segments.
	 */
	private final OrthographicCamera camera;

	/**
	 * Temp vector used for calculating near platforms.
	 */
	private final Vector2 tempVector = new Vector2();

	/**
	 * Temp rectangle used for calculating near enemies and potions.
	 */
	private final Rectangle tempRect = new Rectangle();

	/**
	 * Temp circle used for calculating near enemies and potions.
	 */
	private final Circle tempCircle = new Circle();

	public LevelStream(OrthographicCamera camera, Batch batch, float segmentStartOffset,
			float firstSegmentAdditionalStartOffset) {
		this.camera = camera;
		this.renderer = new OrthogonalTiledMapRenderer(null, batch);
		this.assetManager.setLoader(LevelSegment.class, new LevelSegmentLoader(camera, renderer));
		this.assetManager.setLoader(TiledMap.class, new LevelSegmentTmxLoader());
		this.segmentStartOffset = segmentStartOffset;
		this.firstSegmentAdditionalStartOffset = firstSegmentAdditionalStartOffset;
	}

	/**
	 * Starts streaming the segments
	 */
	public void start() {
		if (currentlyLoadedSegmentName == null) {
			startLoadingNextLevelSegment();
		}
	}

	private void startLoadingNextLevelSegment() {
		final String currentFile = allFiles.get(lastLoadedIndex);
		lastLoadedIndex = (lastLoadedIndex + 1) % allFiles.size();

		currentlyLoadedSegmentName = "segment" + (levelIndex++);
		assetManager.load(currentlyLoadedSegmentName, LevelSegment.class,
				new LevelSegmentLoader.Parameter(currentFile));
		Gdx.app.debug(LOG_TAG, "Started loading of level " + currentlyLoadedSegmentName);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		assetManager.update();

		if (segments.size == 0) {
			// insert first segment
			if (nextLevelSegmentReady()) {
				appendNextSegment(firstSegmentAdditionalStartOffset);
				startLoadingNextLevelSegment();
			}
		} else {
			final LevelSegment last = segments.last();
			if (reachedRightBorder(last)) {
				Gdx.app.debug(LOG_TAG, "Should append next says " + last.getAssetName());
				if (nextLevelSegmentReady()) {
					appendNextSegment(last.getRightX());
					startLoadingNextLevelSegment();
				}
			}
		}

		removeFinishedSegments();
	}

	private boolean nextLevelSegmentReady() {
		return assetManager.isLoaded(currentlyLoadedSegmentName);
	}

	private void appendNextSegment(float additionalOffset) {
		final LevelSegment segment = getNextLevelSegment();
		segment.setX(additionalOffset + segmentStartOffset);
		addActor(segment);
		segments.addLast(segment);

		Gdx.app.debug(LOG_TAG, "Completely loaded level " + segment.getAssetName() + ". Inserting into stage");
	}

	private LevelSegment getNextLevelSegment() {
		if (!nextLevelSegmentReady()) {
			return null;
		}

		return assetManager.get(currentlyLoadedSegmentName, LevelSegment.class);
	}

	private boolean reachedRightBorder(LevelSegment segment) {
		return segment.getRightX() <= camera.viewportWidth;
	}

	private boolean reachedLeftBorder(LevelSegment segment) {
		return segment.getRightX() <= 0;
	}

	private void removeSegment(LevelSegment segment) {
		removeActor(segment);
		if (!currentlyLoadedSegmentName.equals(segment.getAssetName())) {
			assetManager.unload(segment.getAssetName());
		}
		Gdx.app.debug(LOG_TAG, "Removing segment " + segment.getAssetName());
	}

	private void removeFinishedSegments() {
		for (final Iterator<LevelSegment> iterator = segments.iterator(); iterator.hasNext();) {
			final LevelSegment segment = iterator.next();
			if (reachedLeftBorder(segment)) {
				Gdx.app.debug(LOG_TAG, "Should remove " + segment.getAssetName());
				segmentsToRemove.add(segment);
			} else {
				break;
			}
		}

		for (final LevelSegment segment : segmentsToRemove) {
			removeSegment(segment);
			segments.removeValue(segment, true);
		}
		segmentsToRemove.clear();
	}

	/**
	 * Moves the stream to the left
	 * 
	 * @param offset
	 */
	public void moveLeft(float offset) {
		for (final LevelSegment segment : segments) {
			segment.moveLeft(offset);
		}
	}

	/**
	 * Get platforms in radius of (pointX, pointY).
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param nearPlatforms
	 * @return
	 */
	public void getPlatformsNear(float centerX, float centerY, float radius, List<Platform> nearPlatforms) {
		tempCircle.radius = radius;
		tempCircle.x = centerX;
		tempCircle.y = centerY;

		nearPlatforms.clear();
		for (final LevelSegment segment : segments) {
			if (segment.getX() <= centerX + radius || centerX - radius <= segment.getRightX()) {
				for (final Platform p : segment.getPlatforms()) {
					p.retrieveAbsolutePosition(tempVector);
					if (tempCircle.contains(tempVector.x, tempVector.y)) {
						nearPlatforms.add(p);
					}
				}
			}
		}
	}

	/**
	 * Get enemies in radius of (centerX, centerY).
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param nearEnemies
	 * @return
	 */
	public void getEnemiesNear(float centerX, float centerY, float radius, List<Enemy> nearEnemies) {
		tempCircle.radius = radius;
		tempCircle.x = centerX;
		tempCircle.y = centerY;

		nearEnemies.clear();
		for (final LevelSegment segment : segments) {
			if (segment.getX() <= centerX + radius || centerX - radius <= segment.getRightX()) {
				for (final Enemy e : segment.getEnemies()) {
					e.retrieveHitbox(tempRect);
					if (tempCircle.contains(tempRect.x, tempRect.y)) {
						nearEnemies.add(e);
					}
				}
			}
		}
	}

	/**
	 * Get potions in radius of (centerX, centerY).
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param nearPotions
	 * @return
	 */
	public void getPotionsNear(float centerX, float centerY, float radius, List<Potion> nearPotions) {
		tempCircle.radius = radius;
		tempCircle.x = centerX;
		tempCircle.y = centerY;

		nearPotions.clear();
		for (final LevelSegment segment : segments) {
			if (segment.getX() <= centerX + radius || centerX - radius <= segment.getRightX()) {
				for (final Potion p : segment.getPotions()) {
					p.retrieveHitbox(tempRect);
					if (tempCircle.contains(tempRect.x, tempRect.y)) {
						nearPotions.add(p);
					}
				}
			}
		}
	}
}
