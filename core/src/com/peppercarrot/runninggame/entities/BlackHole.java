package com.peppercarrot.runninggame.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nGame.utils.scene2d.AnimatedImage;
import com.peppercarrot.runninggame.stages.WorldStage;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Moves all game entities to the given point on the screen. Enemies are
 * instantly destroyed when in area of effect.
 * 
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {

	public static class Effect extends Image {
		float duration;
		TextureRegionDrawable blackhole1;
		TextureRegionDrawable blackhole2;

		public Effect(float duration) {
			/*
			super(new AnimatedDrawable(
					new Animation(duration/3, Assets.I.getRegions("blackhole"), Animation.PlayMode.LOOP)));
			setOrigin(Align.center);
			addAction(Actions.forever(Actions.rotateBy(-360f, 2.6f)));
			*/
			super(new TextureRegion(Assets.I.getRegions("blackhole").get(0)));
			this.duration = duration;
			blackhole1 = new TextureRegionDrawable(new TextureRegion(Assets.I.getRegions("blackhole").get(0)));
			blackhole2 = new TextureRegionDrawable(new TextureRegion(Assets.I.getRegions("blackhole").get(1)));
			setOrigin(Align.center);
		}

		public void execute(){
			setVisible(true);
			Color color = getColor();
			setColor(color.r,color.g,color.b,0);
			setDrawable(blackhole1);
			this.clearActions();
			//Action
			SequenceAction seqA = new SequenceAction();
			seqA.addAction(Actions.fadeIn(0.1f));
			seqA.addAction(Actions.fadeOut(0.2f));
			seqA.addAction(Actions.run(new Runnable() {
		        @Override
		        public void run() {
		        	setDrawable(blackhole2);
		        	clearActions();
		        	SequenceAction seqA = new SequenceAction();
					seqA.addAction(Actions.fadeIn(0.5f));
					seqA.addAction(Actions.fadeOut(duration-0.5f-0.2f-0.3f));
		        	ParallelAction parA = new ParallelAction();
					parA.addAction(Actions.forever(Actions.rotateBy(-360f, 2.6f)));
		        	parA.addAction(seqA);
		        	addAction(parA);
		        }
		    }));
			ParallelAction parA = new ParallelAction();
			parA.addAction(Actions.forever(Actions.rotateBy(360f, 2.4f)));
			parA.addAction(seqA);
			this.addAction(parA);
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			Color color = getColor();
			super.draw(batch, parentAlpha);
			//Reset Alpha after drawing
			batch.setColor(color.r, color.g, color.b, 1f);
		}
	}

	private final Effect effect;

	private final Group affectedEnemies = new Group();
	private final Group affectedPotions = new Group();

	/**
	 * Effect radius
	 */
	private final float RADIUS = 700;

	/**
	 * where on the screen effect appears
	 */
	private final float OFFSET_X = (Constants.VIRTUAL_WIDTH * 3) / 4;
	private final float OFFSET_Y = (Constants.VIRTUAL_HEIGHT * 4) / 5 - Constants.OFFSET_TO_GROUND;

	private final List<Enemy> tempAffectedEnemies = new ArrayList<Enemy>();
	private final List<Potion> tempAffectedPotions = new ArrayList<Potion>();
	private final Rectangle tempRect = new Rectangle();

	public BlackHole(Runner runner, int maxEnergy, float duration) {
		super(runner, maxEnergy, duration);
		effect = new Effect(getDuration());
		effect.setVisible(false);
	}

	@Override
	protected void execute(WorldStage worldStage) {
		final float effectXPosition = OFFSET_X;
		final float effectYPosition = OFFSET_Y + worldStage.runner.getY();

		// Position centered
		effect.setX(effectXPosition - effect.getWidth() / 2);
		effect.setY(effectYPosition - effect.getHeight() / 2);
		effect.execute();

		worldStage.getLevelStream().getEnemiesNear(effectXPosition, effectYPosition, RADIUS, tempAffectedEnemies);
		for (final Enemy enemy : tempAffectedEnemies) {
			if (enemy.isAlive() && !enemy.indestructible) {
				Account.I.huntEnemies += 1;
				enemy.die();
				enemy.setVisible(false);

				final AnimatedImage animation = enemy.dyingAnim;
				animation.setVisible(true);

				// Set image on initial enemy position
				enemy.retrieveHitbox(tempRect);
				animation.setX(tempRect.x);
				animation.setY(tempRect.y);

				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
				animation.addAction(pAction);

				affectedEnemies.addActor(animation);
			}
		}
		worldStage.getLevelStream().getPotionsNear(effectXPosition, effectYPosition, RADIUS, tempAffectedPotions);
		for (final Potion potion : tempAffectedPotions) {
			if (potion.isVisible()) {
				potion.collected();

				final Image potionImage = new Image(potion.image.getDrawable());
				potionImage.setVisible(true);

				// Set image on initial potion position
				potion.retrieveHitbox(tempRect);
				potionImage.setX(tempRect.x);
				potionImage.setY(tempRect.y);

				final ParallelAction pAction = new ParallelAction();
				pAction.addAction(Actions.moveTo(effectXPosition, effectYPosition, getDuration(), Interpolation.pow2));
				pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
				potionImage.addAction(pAction);

				affectedPotions.addActor(potionImage);
			}
		}
		worldStage.addActor(affectedPotions);
		worldStage.addActor(affectedEnemies);
		worldStage.addActor(effect);
	}

	@Override
	protected void internalUpdate(float delta) {
	}

	@Override
	protected void finish() {
		affectedEnemies.remove();
		affectedPotions.remove();
		affectedPotions.clear();
		affectedEnemies.clear();
		effect.getParent().removeActor(effect);
		effect.setVisible(false);
	}
}
