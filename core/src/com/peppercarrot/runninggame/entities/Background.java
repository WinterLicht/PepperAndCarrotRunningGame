package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An infinitely scrolling and repeating background.
 * Contains two rectangles which are connected horizontally seemless.
 * When the second rectangle is about to hit left bound of the screen,
 * the first rectangle will be applied right after the second, so by
 * further scrolling the image is displayed continuously.
 * @author WinterLicht
 *
 */
public class Background extends Actor{

	private int width; /** Width of the image in pixel. */
	private int height; /** Height of the image in pixel. */
	private final TextureRegion textureRegion; /** Repeating image. */
	private Rectangle textureRegionBounds1; /** Controlling rectangle in dimensions of the image. */
	private Rectangle textureRegionBounds2; /** Controlling rectangle in dimensions of the image. */
	private int speed = -350; /** Scroll speed in x-direction in pixel. */

	public Background() {
		//TODO: name of the texture image as a parameter?
		Texture tex = new Texture(Gdx.files.internal("testbg.png"), true);
		textureRegion = new TextureRegion(tex);
		width = tex.getWidth();
		height = tex.getHeight();
		textureRegionBounds1 = new Rectangle(- width / 2, 0, height, height);
		textureRegionBounds2 = new Rectangle(width / 2, 0, width, height);
		/*
		textureRegionBounds1 = new Rectangle(0 - Constants.VIRTUAL_WIDTH / 2, 0, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		textureRegionBounds2 = new Rectangle(Constants.VIRTUAL_WIDTH / 2, 0, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		 */
	}

		@Override
		public void act(float delta) {
			if (leftBoundsReached(delta)) {
				resetBounds();
			} else {
				updateXBounds(delta);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			super.draw(batch, parentAlpha);
			batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, width,
					height);
			batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, width,
					height);
			/*
			batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.VIRTUAL_WIDTH,
					Constants.VIRTUAL_HEIGHT);
			batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.VIRTUAL_WIDTH,
					Constants.VIRTUAL_HEIGHT);
			*/
		}

		/**
		 * Is the second rectangle about to hit left bound of the screen.
		 * @param delta timedelta
		 * @return
		 */
		private boolean leftBoundsReached(float delta) {
			return (textureRegionBounds2.x + (delta * speed)) <= 0;
		}

		/**
		 * Scroll horizontally.
		 * @param delta
		 */
		private void updateXBounds(float delta) {
			textureRegionBounds1.x += delta * speed;
			textureRegionBounds2.x += delta * speed;
		}

		/**
		 * Attach rectangles to each other horizontally.
		 */
		private void resetBounds() {
			textureRegionBounds1 = textureRegionBounds2;
			textureRegionBounds2 = new Rectangle(width, 0, width, height);
			//textureRegionBounds2 = new Rectangle(Constants.VIRTUAL_WIDTH, 0, Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT);
		}

}
