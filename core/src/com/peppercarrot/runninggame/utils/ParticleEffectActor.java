package com.peppercarrot.runninggame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Needs particle emitter with attached option.
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
		if (effect.isComplete()) {
			effect.start();
		}
	}

	public ParticleEffect getEffect() {
		return effect;
	}
}