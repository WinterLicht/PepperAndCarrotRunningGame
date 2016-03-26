package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Playable character class.
 * The runner is only able to move horizontally, other entities are
 * moving towards player. Runner can jump and doublejump.
 * TODO: attacks ...
 * @author WinterLicht
 *
 */
public class Runner extends Image {
	State currState = State.RUNNING;
	int speedY = 0; /** Vertical speed in pixel. */
	int maxJumpSpeed = 24; /** Maximum speed when jumping in pixel */
	
	AnimatedImage runningAnim;
	AnimatedImage jumpingAnim;

	/**
	 * Possible states.
	 */
	enum State{
		RUNNING, FALLING, JUMPING, DOUBLEJUMPING, DYING;
	}

	public Runner(){
		super(new TextureRegion(Assets.I.atlas.findRegion("runner")));
		//Runner is always placed with some offset
		setX(Constants.OFFSET_TO_EDGE);
		setY(Constants.OFFSET_TO_GROUND);
		//Load Animations
		runningAnim = new AnimatedImage(new Animation(0.099f, Assets.I.getRegions("run"), Animation.PlayMode.LOOP));
		runningAnim.start();
		jumpingAnim = new AnimatedImage(new Animation(0.17f, Assets.I.getRegions("jump"), Animation.PlayMode.LOOP));
		jumpingAnim.start();
	}

	public void jump(){
		if (currState == State.RUNNING){
			currState = State.JUMPING;
			speedY = maxJumpSpeed;
			return;
		}
		if (currState == State.JUMPING){
			currState = State.DOUBLEJUMPING;
			speedY = maxJumpSpeed;
			return;
		}
	}

	/**
	 * Check collision between player and other entities.
	 * TODO: Collision not only between platforms!!
	 * @param vectorArray y-coordinates of all platform tops near the player
	 */
	public void checkCollision(Array<Integer> vectorArray){
		//Players position
		float posY = getY() - Constants.OFFSET_TO_GROUND;
		//Offset in pixel
		int offsetTop = 12;
		int offsetBottom = 10;
		for (int i = 0; i < vectorArray.size; i++){
			Integer tileTop = vectorArray.get(i);
			//Player lands if he is near platforms top
			if( tileTop - offsetBottom < posY && posY < tileTop + offsetTop) {
				land(tileTop);
			}
		}
	}

	public void land(float y){
		//Player lands only if his speed is small enough
		int speedOffset = 8;
		if (speedY < speedOffset){
			setY(y+Constants.OFFSET_TO_GROUND);
			speedY = 0;
			currState = State.RUNNING;
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		//Decide which animation is displayed
		switch (currState) {
		case DOUBLEJUMPING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		case DYING:
			break;
		case JUMPING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		case RUNNING:
			runningAnim.act(delta);
			setDrawable(runningAnim.getDrawable());
			break;
		case FALLING:
			jumpingAnim.act(delta);
			setDrawable(jumpingAnim.getDrawable());
			break;
		default: //Should not be reached
			break;
		}
		//Gravity is 1 pixel
		speedY -= 1;
		//Move
		float oldYPos = getY();
		setY(getY() + speedY);
		//Player can't fall under/below the ground
		if (getY() < Constants.OFFSET_TO_GROUND) {
			currState = State.RUNNING;
			setY(Constants.OFFSET_TO_GROUND);
		}
		if (getY() < oldYPos && currState == State.RUNNING) {
			//Player is falling, if his y-position is lowered
			//and he was previously running.
			currState = State.FALLING;
		}
	}

}
