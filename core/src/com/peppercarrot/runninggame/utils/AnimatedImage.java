package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Actor that can be animated.
 * @author WinterLicht
 *
 */
public class AnimatedImage extends Image {

	public Animation animation = null;
	private float frameTime = 0;
	private boolean running = false;
	private boolean looping = false;

	public AnimatedImage(Animation animation){
		super(animation.getKeyFrame(0));
		this.animation = animation;
	}

	@Override
	public void act(float delta){
		super.act(delta);
		if(this.running && !animation.isAnimationFinished(delta)){
			frameTime+=delta;
			((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(frameTime, this.looping));
		}
	}

	public void start(){
		this.running = true;
		this.frameTime = 0;
	}

	public void stop(){
		this.running = false;
		this.frameTime = 0;
	}

	public void loop(){
		this.looping = true;
	}

}
