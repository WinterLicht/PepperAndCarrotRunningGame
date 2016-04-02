package com.peppercarrot.runninggame.entities;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.utils.Assets;

public class TimeDistortion extends Ability {

	public TimeDistortion(Runner r, Level l) {
		super(r, l);
		//Button
		button = new Button(Assets.I.skin, "default");
		button.setTouchable(Touchable.enabled);
		button.setName(""+3);
		button.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				System.out.println("Attack Button " + event.getListenerActor().getName() + " touched.");
				runner.activateAbility(Integer.parseInt(event.getListenerActor().getName()));
				((Button) event.getListenerActor()).setChecked(false);
				event.cancel();
			}
		});
		energy = new ProgressBar(0, 3, 1, true, Assets.I.skin, "abilityEnergy");
		energy.setValue(0);
		table.add(button).width(180).height(180).left();
		table.add(energy).height(180).right().expandY();
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

}
