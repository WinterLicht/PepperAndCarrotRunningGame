package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * This effect manipulates horizontal scroll speed of the level.
 * First in a given effect duration scroll speed of the level is
 * decreasing and then increasing again, so level is at his old scroll
 * speed as before, when the effect ends. 
 * @author WinterLicht
 *
 */
public class TimeDistortion extends Ability {
	float speedDistortion = 4;
	float oldLevelSpeed;

	public TimeDistortion(Runner r, Level l) {
		super(r, l);
		durationMax = 8f;
		currentDuration = durationMax;
		oldLevelSpeed = l.scrollSpeed;
		//Button
		button = new Button(Assets.I.skin, "default");
		button.setTouchable(Touchable.enabled);
		button.setName(""+3);
		button.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				activate();
				((Button) event.getListenerActor()).setChecked(false);
				event.cancel();
			}
		});
		energy = new ProgressBar(0, 6, 1, true, Assets.I.skin, "abilityEnergy");
		energy.setValue(0);
		table.add(button).width(180).height(180).left();
		table.add(energy).height(180).right().expandY();
	}

	@Override
	public void update(float delta) {
		if (currentDuration >= durationMax) {
			//Effect ending
			level.scrollSpeed = oldLevelSpeed;
		} else {
			//Effect active
			//Calculate current scrollspeed
			currentDuration += delta;
			float scrollSpeed;
			if (currentDuration < durationMax/2) {
				//scrollSpeed changing from 0 to speedDistortion
				scrollSpeed = speedDistortion*currentDuration * 2 / durationMax;
			} else {
				//scrollSpeed changing frm speedDistortion to 0
				scrollSpeed = -((speedDistortion*currentDuration * 2 / durationMax)-speedDistortion*2);
			}
			level.scrollSpeed = oldLevelSpeed - scrollSpeed;
		}
	}

	@Override
	protected void execute() {
		oldLevelSpeed = level.scrollSpeed;
	}

}
