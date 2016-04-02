package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.utils.AnimatedImage;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Moves all game entities to the top left corner of the screen.
 * @author WinterLicht
 *
 */
public class BlackHole extends Ability {
	Array<Enemy> enemies;
	Array<Potion> potions;

	public BlackHole(Runner r, Level l) {
		super(r, l);
		durationMax = 1f;
		currentDuration = durationMax;
		enemies = new Array<Enemy>();
		potions = new Array<Potion>();
		//Button
		button = new Button(Assets.I.skin, "default");
		button.setTouchable(Touchable.enabled);
		button.setName(""+2);
		button.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				activate();
				((Button) event.getListenerActor()).setChecked(false);
				event.cancel();
			}
		});
		energy = new ProgressBar(0, 3, 1, true, Assets.I.skin, "abilityEnergy");
		energy.setValue(0);
		table.add(button).width(180).height(180).left();
		table.add(energy).height(180).right().expandY();
		//Animation of attack
		effect = new AnimatedImage(new Animation(durationMax/8, Assets.I.getRegions("black-hole"), Animation.PlayMode.LOOP_PINGPONG));
		effect.setVisible(false);
		effect.stop();
	}

	@Override
	public void update(float delta) {
		if (currentDuration >= durationMax) {
			//Effect ending
			for (Enemy enemy : enemies) {
				enemy.die();
				enemy.setVisible(false);
			}
			for (Potion potion : potions) {
				potion.collected();
			}
			effect.stop();
			effect.setVisible(false);
		} else {
			//Effect active
			currentDuration += delta;
		}
	}

	@Override
	protected void execute() {
		effect.setX((Constants.VIRTUAL_WIDTH*3)/4);
		effect.setY(Constants.VIRTUAL_HEIGHT);
		effect.setVisible(true);
		effect.start();
		enemies = level.getEnemiesInRadius(Constants.VIRTUAL_WIDTH);
		potions = level.getPotionsInRadius(Constants.VIRTUAL_WIDTH);
		for (Enemy enemy : enemies) {
			ParallelAction pAction = new ParallelAction();
			pAction.addAction(Actions.moveTo(effect.getX(), effect.getY(), durationMax, Interpolation.pow2));
			pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
			enemy.addAction(pAction);
		}
		for (Potion potion : potions) {
			ParallelAction pAction = new ParallelAction();
			pAction.addAction(Actions.moveTo(effect.getX(), effect.getY(), durationMax, Interpolation.pow2));
			pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.3f)));
			potion.addAction(pAction);
		}
	}

}
