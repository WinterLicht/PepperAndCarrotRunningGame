package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
	   ParticleEffect effect;

	   public ParticleEffectActor(float x, float y/*ParticleEffect effect*/) {
		  ParticleEffect p = new ParticleEffect();
		  //TODO: name of emitter as parameter
		  p.load(Gdx.files.internal("poison-clouds.p"), Assets.I.atlas); //files.internal loads from the "assets" folder
	      this.effect = p;
	      this.setPosition(x, y);
	      effect.setPosition(x, y);
	   }

	   @Override
	   public void draw(Batch batch, float parentAlpha) {
		   super.draw(batch, parentAlpha);
		   effect.draw(batch); //define behavior when stage calls Actor.draw()
	   }

	   @Override
	   public void act(float delta) {
	      super.act(delta);
	      effect.setPosition(this.getX(), this.getY()); //set to whatever x/y you prefer
	      effect.update(delta); //update it
	      effect.start(); //need to start the particle spawning
	      }

	   public ParticleEffect getEffect() {
	      return effect;
	   }
}