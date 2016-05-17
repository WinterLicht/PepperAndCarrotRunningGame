package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An infinitely scrolling and repeating background. The viewport has to be set
 * from a higher level, so that this entity only renders the resulting tiles.
 * 
 * TODO: If the stage consist of a single camera which is used to define the
 * background position, this could be placed as an actor within that stage.
 * 
 * @author WinterLicht
 * @author momsen
 *
 */
public class Background extends Actor {

	private final int textureWidth;

	private final int textureHeight;

	private final Texture texture;

	private final int columnsIfCentered;

	private final int rowsIfCentered;

	private final float centerOffsetY;

	private float viewportX;

	private float viewportY;

	public Background(String backgroundPicture, int virtualWidth, int virtualHeight) {
		texture = new Texture(Gdx.files.internal(backgroundPicture), true);
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		textureWidth = texture.getWidth();
		textureHeight = texture.getHeight();

		centerOffsetY = -virtualHeight / 2.0f;

		columnsIfCentered = virtualWidth / textureWidth + (virtualWidth % textureWidth > 0 ? 1 : 0);

		rowsIfCentered = virtualHeight / textureHeight + (virtualHeight % textureHeight > 0 ? 1 : 0);
	}

	public float getViewportX() {
		return viewportX;
	}

	public void setViewportX(float viewportX) {
		this.viewportX = viewportX;
	}

	public void moveViewportLeft(float offset) {
		viewportX += offset;
	}

	public float getViewportY() {
		return viewportY;
	}

	public void setViewportY(float viewportY) {
		this.viewportY = viewportY;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final float clipX = -(viewportX % textureWidth);
		final float clipY = (viewportY % textureHeight);

		final int columns = columnsIfCentered + (clipX != 0 ? 1 : 0);

		final int rowOffset = (int) (viewportY / textureHeight);
		final int rows = rowsIfCentered + (clipY != 0 ? 1 : 0);

		// Gdx.app.log("<dbg>",
		// "drawing background starting at " + rowOffset + ", " + viewportY + ",
		// " + textureHeight + ", " + rows);

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				// Base camera does not alter the x position, the clip will be
				// scrolled
				final float tileX = clipX + col * textureWidth;

				// Base camera does alter the y position, the clip will not be
				// scrolled but framed
				// TODO: why do we need the clip offset?
				final float tileY = (row + rowOffset) * textureHeight + centerOffsetY - clipY;

				batch.draw(texture, tileX, tileY, textureWidth, textureHeight);
			}
		}
	}
}
