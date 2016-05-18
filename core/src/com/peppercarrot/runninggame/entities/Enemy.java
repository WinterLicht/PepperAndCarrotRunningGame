package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;

/**
 * Enemy.
 * 
 * @author WinterLicht
 *
 */
public class Enemy extends Image {
	public State currState = State.IDLE;
	// Animations
	AnimatedImage idleAnim;
	AnimatedImage dyingAnim;

	enum State {
		IDLE, DYING;
	}

	public Enemy(String name) {
		super(new TextureRegion(Assets.I.atlas.findRegion(name + "-idle")));
		setName("enemy");
		// Load Animations
		idleAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.099f, Assets.I.getRegions(name + "-idle"), Animation.PlayMode.LOOP)));
		idleAnim.setOrigin(Align.center);
		dyingAnim = new AnimatedImage(new AnimatedDrawable(
				new Animation(0.07f, Assets.I.getRegions(name + "-death"), Animation.PlayMode.NORMAL)));
		dyingAnim.setVisible(false);
		dyingAnim.setOrigin(Align.center);
	}

	/**
	 * Sets also image invisible.
	 */
	public void die() {
		dyingAnim.setVisible(true);
		dyingAnim.reset();
		currState = State.DYING;
	}

	public boolean isAlive() {
		return (currState != State.DYING);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		switch (currState) {
		case DYING:
			dyingAnim.act(delta);
			setDrawable(dyingAnim.getDrawable());
			// FIXME: why isAnimationFinished always false??
			/*
			 * if (dyingAnim.getAnimatedDrawable().getAnimation().
			 * isAnimationFinished(delta)){ System.out.println(
			 * "enemy death animation finished"); }
			 */
			break;
		case IDLE:
			idleAnim.act(delta);
			setDrawable(idleAnim.getDrawable());
			break;
		default:
			break;
		}
	}

	public void retrieveHitbox(Rectangle rectangle) {
		CollisionUtil.retrieveHitbox(this, rectangle);
	}
}
