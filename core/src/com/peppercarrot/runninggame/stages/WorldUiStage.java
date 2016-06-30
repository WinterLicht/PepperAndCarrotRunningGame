package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.stages.AbilityWidget.AbilityActivationListener;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Callback;
import com.peppercarrot.runninggame.utils.Constants;

public class WorldUiStage extends AbstractStage {

	private final Table uiTable;
	private final Label hintLabel;

	private final Button jumpBtnTransparent;

	public final AbilityWidget abilityWidget1;
	public final AbilityWidget abilityWidget2;
	public final AbilityWidget abilityWidget3;
	public final AbilityWidget abilityWidget0;

	private boolean hintFaded = false;
	private Callback jumpBtnCallback;
	private Callback exitBtnCallback;

	Table skillsBtns;

	public WorldUiStage(Runner r) {
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiTable.setWidth(Constants.VIRTUAL_WIDTH);
		uiTable.setHeight(Constants.VIRTUAL_HEIGHT);

		// Jump Button
		jumpBtnTransparent = new Button(Assets.I.skin, "transparent");
		final ImageButton jumpBtn = new ImageButton(Assets.I.skin, "button_jump");
		final ImageButton exitBtn = new ImageButton(Assets.I.skin, "button_exit");
		exitBtn.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (exitBtnCallback != null) {
					exitBtnCallback.invoke();
				}
				return true;
			}
		});

		final int jumpBtnTransparentWidth = 470;
		jumpBtnTransparent.setTouchable(Touchable.enabled);
		hintLabel = new Label("press on the left side of the screen to 'jump'", Assets.I.skin, "default");
		hintLabel.setWrap(true);
		hintLabel.setTouchable(Touchable.disabled);
		jumpBtnTransparent.add(exitBtn).top().left();
		jumpBtnTransparent.add(hintLabel).top();
		jumpBtnTransparent.row();
		jumpBtnTransparent.add(r.health).height(uiTable.getHeight() - exitBtn.getHeight() - jumpBtn.getHeight()).left();
		jumpBtnTransparent.row();
		jumpBtnTransparent.add(jumpBtn).bottom().left();
		jumpBtnTransparent.top().left();
		jumpBtnTransparent.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				hideHint();
				if (jumpBtnCallback != null) {
					jumpBtnCallback.invoke();
				}
				return true;
			}
		});
		uiTable.add(jumpBtnTransparent).width(jumpBtnTransparentWidth).height(uiTable.getHeight()).expandX().left();

		// Attack Buttons
		skillsBtns = new Table();
		abilityWidget1 = new AbilityWidget(1);
		abilityWidget2 = new AbilityWidget(2);
		abilityWidget3 = new AbilityWidget(3);
		abilityWidget0 = new AbilityWidget(0);
		skillsBtns.addActor(abilityWidget1);
		skillsBtns.addActor(abilityWidget2);
		skillsBtns.addActor(abilityWidget3);
		skillsBtns.addActor(abilityWidget0);
		skillsBtns.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (abilityWidget1.isInsideSector(x, y))
					abilityWidget1.listener.activate(abilityWidget1.ability);
				if (abilityWidget2.isInsideSector(x, y))
					abilityWidget2.listener.activate(abilityWidget2.ability);
				if (abilityWidget3.isInsideSector(x, y))
					abilityWidget3.listener.activate(abilityWidget3.ability);
				if (abilityWidget0.isInsideBoundaries(new Vector2(x - AbilityWidget.width, y), 0, 200))
					abilityWidget0.listener.activate(abilityWidget0.ability);
				return true;
			}
		});
		uiTable.add(skillsBtns).right().bottom().padRight(AbilityWidget.width);
		this.addActor(uiTable);

		skillsBtns.debug();
	}

	public void disable() {
		uiTable.setTouchable(Touchable.disabled);
	}

	public void onJumpTouched(Callback callback) {
		jumpBtnCallback = callback;
	}

	public void onExitTouched(Callback callback) {
		exitBtnCallback = callback;
	}

	public void hideHint() {
		if (!hintFaded) {
			hintLabel.addAction(Actions.fadeOut(0.48f));
			hintFaded = true;
		}
	}

	public void onActivateAbility(AbilityActivationListener listener) {
		abilityWidget1.setAbilityActivationListener(listener);
		abilityWidget2.setAbilityActivationListener(listener);
		abilityWidget3.setAbilityActivationListener(listener);
		abilityWidget0.setAbilityActivationListener(listener);
	}

	public void setAbilitySlot1(Ability ability) {
		abilityWidget1.setAbility(ability);
	}

	public Ability getAbilitySlot1() {
		return abilityWidget1.getAbility();
	}

	public void setAbilitySlot2(Ability ability) {
		abilityWidget2.setAbility(ability);
	}

	public Ability getAbilitySlot2() {
		return abilityWidget2.getAbility();
	}

	public void setAbilitySlot3(Ability ability) {
		abilityWidget3.setAbility(ability);
	}

	public Ability getAbilitySlot3() {
		return abilityWidget3.getAbility();
	}

	public void setAbilitySlot0(Ability ability) {
		abilityWidget0.setAbility(ability);
	}

	public Ability getAbilitySlot0() {
		return abilityWidget0.getAbility();
	}
}
