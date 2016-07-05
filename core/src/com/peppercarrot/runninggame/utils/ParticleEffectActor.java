package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Particles are not Actors! See updatePosition method.
 * 
 * @author WinterLicht
 *
 */
public class ParticleEffectActor extends Actor {
	   ParticleEffect effect;

	   public ParticleEffectActor(float x, float y, String name) {
		  ParticleEffect p = new ParticleEffect();
		  p.load(Gdx.files.internal(name), Assets.I.atlas);
	      this.effect = p;
	      this.setPosition(x, y);
	      effect.setPosition(x, y);
	   }

	   @Override
	   public void draw(Batch batch, float parentAlpha) {
		   super.draw(batch, parentAlpha);
		   effect.draw(batch);
	   }

	   @Override
	   public void act(float delta) {
	      super.act(delta);
	      effect.setPosition(this.getX(), this.getY());
	      effect.update(delta);
	      effect.start(); //need to start the particle spawning
	      }

	   /**
	    * Particles are not actors, so they are not scrolled with level segment.
	    * So this function is needed to scroll particle.
	    * 
	    * @param offset without multiplying delta (?)
	    */
	   public void updatePosition(float offset) {
		   for (ParticleEmitter e : effect.getEmitters()) {
			   e.getWind().setHigh(-offset);
			   e.getWind().setLow(-offset);
		   }
	   }

	   public ParticleEffect getEffect() {
	      return effect;
	   }
}