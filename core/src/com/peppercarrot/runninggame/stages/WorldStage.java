package com.peppercarrot.runninggame.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.Background;
import com.peppercarrot.runninggame.world.LevelSegment;
import com.peppercarrot.runninggame.world.LevelSegment.Platform;
import com.peppercarrot.runninggame.world.LevelStream;
import com.peppercarrot.runninggame.world.collision.IEnemyCollisionAwareActor;
import com.peppercarrot.runninggame.world.collision.IPlatformCollisionAwareActor;
import com.peppercarrot.runninggame.world.collision.IPotionCollisionAwareActor;

/**
 * Contains game entities.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class WorldStage extends AbstractStage {
	private final OrthographicCamera camera;

	private final LevelStream levelStream;

	private final Background background;

	public final Runner runner;

	private final ShapeRenderer debugCollisionShapeRenderer = new ShapeRenderer();

	private final float speed = 400.0f;

	private float speedFactor = 1.0f;

	private final List<IEnemyCollisionAwareActor> enemyCollisionAwareActors = new ArrayList<IEnemyCollisionAwareActor>();

	private final List<IPotionCollisionAwareActor> potionAwareActors = new ArrayList<IPotionCollisionAwareActor>();

	private final List<IPlatformCollisionAwareActor> platformAwareActors = new ArrayList<IPlatformCollisionAwareActor>();

	private final Vector2 tempPlatformPosition = new Vector2();

	private final Rectangle tempActorRectangle = new Rectangle();

	private final Rectangle tempTargetRectangle = new Rectangle();

	private final List<Enemy> tempEnemies = new ArrayList<Enemy>();

	private final List<Platform> tempPlatforms = new ArrayList<Platform>();

	private final List<Potion> tempPotions = new ArrayList<Potion>();

	public WorldStage(int virtualWidth, int virtualHeight, Runner runner, List<String> levelSegments) {
		camera = new OrthographicCamera(virtualWidth, virtualHeight);
		camera.setToOrtho(false, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		setViewport(new FitViewport(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, camera));

		background = new Background("testbg.png", virtualWidth, virtualHeight);
		addActor(background);

		levelStream = new LevelStream(camera, getBatch(), 0, virtualWidth, getRoot(), runner, levelSegments);
		levelStream.setY(Constants.OFFSET_TO_GROUND);
		addActor(levelStream);
		//Count segments
		levelStream.countTilesOfSegments();

		this.runner = runner;
		addActor(runner);

		enemyCollisionAwareActors.add(runner);
		potionAwareActors.add(runner);
		platformAwareActors.add(runner);
	}

	public LevelStream getLevelStream() {
		return levelStream;
	}

	public void move(float delta) {
		final float offset = delta * speed * speedFactor;
		background.moveViewportLeft(offset);
		background.setViewportY(runner.getY() - Constants.OFFSET_TO_GROUND);
		levelStream.moveLeft(offset);
	}

	@Override
	public void draw() {
		getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2,
				runner.getY() + camera.viewportHeight / 2 - Constants.OFFSET_TO_GROUND, 0);

		super.draw();

		// debugRenderCollisionBounds();
	}

	@SuppressWarnings("unused")
	private void debugRenderCollisionBounds() {
		debugCollisionShapeRenderer.setProjectionMatrix(camera.combined);

		levelStream.getPlatformsNear(runner.getX(), runner.getY(), runner.getPlatformCollisionWidth(), tempPlatforms);
		for (final LevelSegment.Platform platform : tempPlatforms) {
			platform.retrieveAbsolutePosition(tempPlatformPosition);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(1, 1, 0, 1);
			debugCollisionShapeRenderer.rect(tempPlatformPosition.x, tempPlatformPosition.y, platform.getW(),
					platform.getH());
			debugCollisionShapeRenderer.end();
		}

		runner.retrieveHitbox(tempActorRectangle);
		levelStream.getEnemiesNear(runner.getX(), runner.getY(), tempActorRectangle.width * 2, tempEnemies);
		for (final Enemy enemy : tempEnemies) {
			enemy.retrieveHitbox(tempTargetRectangle);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(1, 0, 0, 1);
			debugCollisionShapeRenderer.rect(tempTargetRectangle.x, tempTargetRectangle.y, tempTargetRectangle.width,
					tempTargetRectangle.height);
			debugCollisionShapeRenderer.end();
		}

		levelStream.getPotionsNear(runner.getX(), runner.getY(), tempActorRectangle.width * 2, tempPotions);
		for (final Potion potion : tempPotions) {
			potion.retrieveHitbox(tempTargetRectangle);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(0, 1, 0, 1);
			debugCollisionShapeRenderer.rect(tempTargetRectangle.x, tempTargetRectangle.y, tempTargetRectangle.width,
					tempTargetRectangle.height);
			debugCollisionShapeRenderer.end();
		}

		debugCollisionShapeRenderer.begin(ShapeType.Filled);
		debugCollisionShapeRenderer.setColor(1, 1, 1, 1);
		debugCollisionShapeRenderer.rect(runner.getX(), runner.getY(), 100, 100);
		debugCollisionShapeRenderer.end();
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		processCollisions();
	}

	private void processCollisions() {
		processPlatforms();

		processEnemies();

		processPotions();
	}

	private void processPlatforms() {
		if (!platformAwareActors.isEmpty()) {

			for (final IPlatformCollisionAwareActor actor : platformAwareActors) {
				final float x = actor.getPlatformCollisionX();
				final float y = actor.getPlatformCollisionY();
				final float actorsW = actor.getPlatformCollisionWidth();

				levelStream.getPlatformsNear(x, y, actorsW, tempPlatforms);
				for (final Platform platform : tempPlatforms) {
					platform.retrieveAbsolutePosition(tempPlatformPosition);
					// Check for collision
					if (x < tempPlatformPosition.x + platform.getW() || x + actorsW > tempPlatformPosition.x) {
						// First if platform intersects actors in x direction...
						final float platformTop = tempPlatformPosition.y + platform.getH();

						// Offset to the platforms top
						final float offsetTop = 8;
						final float offsetBottom = 20;
						if (platformTop - offsetBottom < y && y < platformTop + offsetTop) {
							// ...then if players bottom (y) is inside given
							// offset-area
							if (actor.onHitPlatform(platform, platformTop)) {
								break;
							}
						}
					}
				}
			}
		}
	}

	private void processEnemies() {
		if (!enemyCollisionAwareActors.isEmpty()) {
			for (final IEnemyCollisionAwareActor actor : enemyCollisionAwareActors) {
				actor.retrieveHitbox(tempActorRectangle);

				levelStream.getEnemiesNear(tempActorRectangle.x, tempActorRectangle.y, tempActorRectangle.width * 2,
						tempEnemies);
				for (final Enemy enemy : tempEnemies) {
					enemy.retrieveHitbox(tempTargetRectangle);

					if (tempActorRectangle.overlaps(tempTargetRectangle)) {
						if (actor.onHitEnemy(enemy)) {
							break;
						}
					}
				}
			}
		}
	}

	private void processPotions() {
		if (!potionAwareActors.isEmpty()) {
			for (final IPotionCollisionAwareActor actor : potionAwareActors) {
				actor.retrieveHitbox(tempActorRectangle);

				levelStream.getPotionsNear(tempActorRectangle.x, tempActorRectangle.y, tempActorRectangle.width * 2,
						tempPotions);
				for (final Potion potion : tempPotions) {
					if (potion.isVisible()) {
						potion.retrieveHitbox(tempTargetRectangle);

						if (tempActorRectangle.overlaps(tempTargetRectangle)) {
							if (actor.onHitPotion(potion)) {
								break;
							}
						}
					}
				}
			}
		}
	}

	public void start() {
		levelStream.start();
	}

	public float getSpeedFactor() {
		return speedFactor;
	}

	public void setSpeedFactor(float speedFactor) {
		this.speedFactor = speedFactor;
	}

	public void addEnemyAwareActor(IEnemyCollisionAwareActor actor) {
		enemyCollisionAwareActors.add(actor);
	}

	public void removeEnemyAwareActor(IEnemyCollisionAwareActor actor) {
		enemyCollisionAwareActors.remove(actor);
	}

	public void addPotionAwareActor(IPotionCollisionAwareActor actor) {
		potionAwareActors.add(actor);
	}

	public void removePotionAwareActor(IPotionCollisionAwareActor actor) {
		potionAwareActors.remove(actor);
	}
	
	public Queue<LevelSegment> getLevelSegments() {
		return levelStream.getLevelSegments();
	}

}
