package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
public class Background extends Actor {

	private final int textureWidth;

	private final int textureHeight;

	private final Texture texture;
	
	private final Texture groundTexture;

	private final int columnsIfCentered;

	private final int rowsIfCentered;

	private float viewportX;

	private float viewportY;
	
	private float diffX = 0; //difference of speed for parallax

	public Background(String backgroundPicture, int virtualWidth, int virtualHeight) {
		texture = new Texture(Gdx.files.internal(backgroundPicture), true);
		texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		textureWidth = texture.getWidth();
		textureHeight = texture.getHeight();

		columnsIfCentered = virtualWidth / textureWidth + (virtualWidth % textureWidth > 0 ? 1 : 0);
		rowsIfCentered = virtualHeight / textureHeight + (virtualHeight % textureHeight > 0 ? 1 : 0);

		groundTexture = new Texture(Gdx.files.internal("ground.png"));
		groundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
	}

	public float getViewportX() {
		return viewportX;
	}

	public void setViewportX(float viewportX) {
		this.viewportX = viewportX;
	}

	public void moveViewportLeft(float offset) {
		viewportX += (offset-diffX);
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

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				// Base camera does not alter the x and y position, the clip
				// will be scrolled
				final float tileX = clipX + col * textureWidth;
				final float tileY = (rowOffset + row) * textureHeight;
				batch.draw(texture, tileX, tileY, textureWidth, textureHeight);	
			}
		}
		batch.draw(groundTexture,
				0, 0,
				(int)(viewportX+diffX), 0,
				Constants.VIRTUAL_WIDTH, groundTexture.getHeight());
	}
}
