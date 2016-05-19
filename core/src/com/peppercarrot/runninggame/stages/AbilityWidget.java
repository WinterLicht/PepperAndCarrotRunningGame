package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.utils.Assets;

public class AbilityWidget extends Table {

	public interface AbilityActivationListener {
		void activate(Ability ability);
	}

	private final Button button;

	private final ProgressBar energy;

	private Ability ability;

	private AbilityActivationListener listener;

	public AbilityWidget() {
		button = new Button(Assets.I.skin, "default");
		button.setTouchable(Touchable.enabled);
		button.setName("" + 1);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (listener != null && ability != null) {
					listener.activate(ability);
				}

				button.setChecked(false);
			}
		});

		energy = new ProgressBar(0, 0, 1, true, Assets.I.skin, "abilityEnergy");
		energy.setValue(0);
	}

	public void setAbilityActivationListener(AbilityActivationListener listener) {
		this.listener = listener;
	}

	public void setAbility(Ability ability) {
		this.ability = ability;

		if (ability != null) {
			energy.setRange(0, ability.getMaxEnergy());
			energy.setValue(ability.getEnergy());
			add(button).width(180).height(180).left();
			add(energy).height(180).right().expandY();
		} else {
			removeActor(button);
			removeActor(energy);
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		if (ability != null) {
			energy.setValue(ability.getEnergy());
			button.setTouchable(ability.isRunning() ? Touchable.disabled : Touchable.enabled);
		}
	}

	public Ability getAbility() {
		return ability;
	}
}
