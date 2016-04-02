package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Enemy.
 * @author WinterLicht
 *
 */
public class Enemy extends Image {
	public State currState = State.IDLE;
	//Animations
	AnimatedImage idleAnim;
	AnimatedImage dyingAnim;
	
	enum State{
		IDLE,
		DYING;
	}

	public Enemy(String name){
		super(new TextureRegion(Assets.I.atlas.findRegion(name+"-idle")));
		setName("enemy");
		//Load Animations
		idleAnim = new AnimatedImage(new Animation(0.099f, Assets.I.getRegions(name+"-idle"), Animation.PlayMode.LOOP));
		idleAnim.setOrigin(Align.center);
		idleAnim.start();
		dyingAnim = new AnimatedImage(new Animation(0.01f, Assets.I.getRegions(name+"-death"), Animation.PlayMode.NORMAL));
		dyingAnim.setVisible(false);
		dyingAnim.setOrigin(Align.center);
	}

	/**
	 * Sets also image invisible.
	 */
	public void die() {
		idleAnim.stop();
		dyingAnim.start();
		dyingAnim.setVisible(true);
		currState = State.DYING;
	}

	public boolean isAlive(){
		return (currState != State.DYING);
	}

	public Rectangle getHitBox(){
		Rectangle enemyRect = new Rectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
		return enemyRect;
	}

	@Override
	public void act(float delta){
		super.act(delta);
		switch (currState) {
		case DYING:
			dyingAnim.act(delta);
			setDrawable(dyingAnim.getDrawable());
			if (dyingAnim.animation.isAnimationFinished(delta)){
				System.out.println("enemy death animation finished");
			}
			break;
		case IDLE:
			idleAnim.act(delta);
			setDrawable(idleAnim.getDrawable());
			break;
		default:
			break;
		}
	}
}
