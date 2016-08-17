package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedDrawable;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.CollisionUtil;

/**
 * Simple (rectangular) enemy. With no movement or attacks. Does damage on collision with player.
 * 
 * @author WinterLicht
 *
 */
public class Enemy extends Image {
	public State currState = State.IDLE;
	public int damage;
	// Animations can be null !!
	AnimatedImage idleAnim;
	AnimatedImage dyingAnim;
	public boolean indestructible;
	public boolean alreadyCollidedWPlayer = false; //Used in Runner.onhitenemy

	enum State {
		IDLE, DYING;
	}

	public Enemy(String name, int damage, float x, float y, boolean indestructible, TextureRegion tRegion) {
		super(tRegion);
		this.indestructible = indestructible;
		this.damage = damage;
		setName(name);
		if (name.isEmpty()) {
			//No Animation, only the texture Region!
			idleAnim = null;
			dyingAnim = null;
		} else {
			idleAnim = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.14f, Assets.I.getRegions(name + "-idle"), Animation.PlayMode.LOOP)));
			idleAnim.setOrigin(Align.center);
			dyingAnim = new AnimatedImage(new AnimatedDrawable(
					new Animation(0.07f, Assets.I.getRegions(name + "-death"), Animation.PlayMode.NORMAL)));
			dyingAnim.setVisible(false);
			dyingAnim.setOrigin(Align.center);
		}
		this.setOrigin(Align.center);
		this.setX(x - this.getWidth() / 2);
		this.setY(y - this.getHeight() / 2);
	}

	public Enemy(String name, int damage, float x, float y, boolean indestructible) {
		this(name, damage, x, y, indestructible, new TextureRegion(Assets.I.atlas.findRegion(name + "-idle")));
	}

	public Enemy(String name, int damage, float x, float y) {
		this(name, damage, x, y, false); //destructible per default
		}

	/**
	 * Sets also image invisible.
	 */
	public void die() {
		Account.I.pacifist = false;
		if (dyingAnim == null) {
			this.setVisible(false);
		} else {
			dyingAnim.setVisible(true);
			dyingAnim.reset();
			currState = State.DYING;
		}
	}

	public boolean isAlive() {
		return (currState != State.DYING);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		switch (currState) {
		case DYING:
			if (dyingAnim != null) {
				dyingAnim.act(delta);
				setDrawable(dyingAnim.getDrawable());
				// FIXME: why isAnimationFinished always false??
				/*
				 * if (dyingAnim.getAnimatedDrawable().getAnimation().
				 * isAnimationFinished(delta)){ System.out.println(
				 * "enemy death animation finished"); }
				 */
			}
			break;
		case IDLE:
			if (idleAnim != null) {
				idleAnim.act(delta);
				setDrawable(idleAnim.getDrawable());
			}
			break;
		default:
			break;
		}
	}

	public void retrieveHitbox(Rectangle rectangle) {
		CollisionUtil.retrieveHitbox(this, rectangle);
	}
}
