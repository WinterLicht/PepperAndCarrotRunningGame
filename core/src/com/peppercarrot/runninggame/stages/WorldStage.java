package com.peppercarrot.runninggame.stages;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.peppercarrot.runninggame.entities.Background;
import com.peppercarrot.runninggame.entities.Enemy;
import com.peppercarrot.runninggame.entities.Potion;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.utils.Constants;
import com.peppercarrot.runninggame.world.LevelStream;
import com.peppercarrot.runninggame.world.Platform;

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

	private final Runner runner;

	private final ShapeRenderer debugCollisionShapeRenderer = new ShapeRenderer();

	private final float speed = 400.0f;

	private float speedFactor = 1.0f;

	public WorldStage(int virtualWidth, int virtualHeight, Runner runner) {
		camera = new OrthographicCamera(virtualWidth, virtualHeight);

		background = new Background("testbg.png", virtualWidth, virtualHeight);
		addActor(background);

		levelStream = new LevelStream(camera, getBatch(), virtualWidth * 1.5f, virtualWidth);
		levelStream.setY(Constants.OFFSET_TO_GROUND);
		addActor(levelStream);

		this.runner = runner;
		addActor(runner);
	}

	public LevelStream getLevelStream() {
		return levelStream;
	}

	public void move(float delta) {
		final float offset = delta * speed * speedFactor;
		background.moveViewportLeft(offset);
		background.setViewportY(runner.getY());
		levelStream.moveLeft(offset);
	}

	@Override
	public void draw() {
		getBatch().setProjectionMatrix(camera.combined);
		super.draw();

		// debugRenderCollisionBounds();
	}

	@SuppressWarnings("unused")
	private void debugRenderCollisionBounds() {
		debugCollisionShapeRenderer.setProjectionMatrix(camera.combined);

		final Vector2 position = new Vector2();
		final Rectangle rectangle = new Rectangle();

		final List<Platform> platforms = levelStream.getPlatformsNear(runner.getX());
		for (final Platform platform : platforms) {
			platform.retrieveAbsolutePosition(position);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(1, 1, 0, 1);
			debugCollisionShapeRenderer.rect(position.x, position.y, platform.getW(), platform.getH());
			debugCollisionShapeRenderer.end();
		}

		final List<Enemy> enemies = levelStream.getEnemiesNear(runner.getX());
		for (final Enemy enemy : enemies) {
			enemy.retrieveRectangle(rectangle);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(1, 0, 0, 1);
			debugCollisionShapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			debugCollisionShapeRenderer.end();
		}

		final List<Potion> potions = levelStream.getPotionsNear(runner.getX());
		for (final Potion potion : potions) {
			potion.retrieveRectangle(rectangle);

			debugCollisionShapeRenderer.begin(ShapeType.Filled);
			debugCollisionShapeRenderer.setColor(0, 1, 0, 1);
			debugCollisionShapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
			debugCollisionShapeRenderer.end();
		}

		debugCollisionShapeRenderer.begin(ShapeType.Filled);
		debugCollisionShapeRenderer.setColor(1, 1, 1, 1);
		debugCollisionShapeRenderer.rect(runner.getX(), runner.getY(), 100, 100);
		debugCollisionShapeRenderer.end();
	}

	@Override
	public void act(float delta) {
		camera.position.set(camera.viewportWidth / 2,
				runner.getY() + camera.viewportHeight / 2 - Constants.OFFSET_TO_GROUND, 0);
		camera.update();

		super.act(delta * speedFactor);

		runner.applyCollision(levelStream);
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
}
