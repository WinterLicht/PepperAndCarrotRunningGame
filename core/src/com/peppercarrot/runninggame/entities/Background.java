package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.peppercarrot.runninggame.utils.Constants;

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
public class Background {

	private final int textureWidth;

	private final int textureHeight;

	private final Texture texture;

	private final int columnsIfCentered;

	private final int rowsIfCentered;

	private final float centerOffsetY;

	public Background(String backgroundPicture) {
		texture = new Texture(Gdx.files.internal(backgroundPicture), true);
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		textureWidth = texture.getWidth();
		textureHeight = texture.getHeight();

		centerOffsetY = -Constants.VIRTUAL_HEIGHT / 2.0f;

		columnsIfCentered = Constants.VIRTUAL_WIDTH / textureWidth
				+ (Constants.VIRTUAL_WIDTH % textureWidth > 0 ? 1 : 0);

		rowsIfCentered = Constants.VIRTUAL_HEIGHT / textureHeight
				+ (Constants.VIRTUAL_HEIGHT % textureHeight > 0 ? 1 : 0);
	}

	/**
	 * Renders a viewport clip of the background starting at a specific
	 * position.
	 * 
	 * @param batch
	 *            Sprite batch to render. Begin and End have to be called
	 *            elsewhere.
	 * @param x
	 *            position of the viewport clip to render
	 * @param y
	 *            position of the viewport clip to render
	 */
	public void draw(Batch batch, float x, float y) {
		float clipX = -(x % textureWidth);
		float clipY = (y % textureHeight);

		int columns = columnsIfCentered + (clipX != 0 ? 1 : 0);

		int rowOffset = (int) (y / textureHeight);
		int rows = rowsIfCentered + (clipY != 0 ? 1 : 0);

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				// Base camera does not alter the x position, the clip will be
				// scrolled
				float tileX = clipX + col * textureWidth;

				// Base camera does alter the y position, the clip will not be
				// scrolled but framed
				float tileY = (row + rowOffset) * textureHeight + centerOffsetY;

				batch.draw(texture, tileX, tileY, textureWidth, textureHeight);
			}
		}
	}
}
