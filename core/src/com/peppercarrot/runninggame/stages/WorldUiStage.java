package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.entities.Ability;
import com.peppercarrot.runninggame.stages.AbilityWidget.AbilityActivationListener;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Callback;
import com.peppercarrot.runninggame.utils.Constants;

public class WorldUiStage extends AbstractStage {

	private final Table uiTable;
	private final Label hintLabel;
	private final Table attackButtons;

	private final Button jumpBtnTransparent;

	private final AbilityWidget abilityWidget1;
	private final AbilityWidget abilityWidget2;
	private final AbilityWidget abilityWidget3;
	private final AbilityWidget abilityWidget4;

	private boolean hintFaded = false;
	private Callback jumpBtnCallback;

	public WorldUiStage() {
		final int uiPadding = 30; // padding of borders for ui in pixel
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiTable.pad(uiPadding);
		uiTable.setWidth(Constants.VIRTUAL_WIDTH - uiPadding * 2);
		uiTable.setHeight(Constants.VIRTUAL_HEIGHT - uiPadding * 2);

		//Jump Button
		jumpBtnTransparent = new Button(Assets.I.skin, "transparent");
		final int jumpBtnTransparentWidth = 470;
		jumpBtnTransparent.setTouchable(Touchable.enabled);
		hintLabel = new Label("press on the left side of the screen to 'jump'", Assets.I.skin, "default");
		hintLabel.setWrap(true);
		hintLabel.setTouchable(Touchable.disabled);
		jumpBtnTransparent.add(hintLabel).width(jumpBtnTransparentWidth).top();
		jumpBtnTransparent.top();
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

		// TextButton jumpButton = new TextButton ("JUMP", Assets.I.skin,
		// "default");
		// TODO: try something else to pass touch event to this button
		// jumpButton.setTouchable(Touchable.disabled);
		// jumpBtnTransparent.add(jumpButton);
		// jumpBtnTransparent.bottom().left();
		uiTable.add(jumpBtnTransparent).width(jumpBtnTransparentWidth).height(uiTable.getHeight()).expandX().left();
		// Attack Buttons
		attackButtons = new Table();
		abilityWidget1 = new AbilityWidget("Black Hole");
		attackButtons.add(abilityWidget1).expandY().right();
		attackButtons.row();
		abilityWidget2 = new AbilityWidget("CarrotCharge");
		attackButtons.add(abilityWidget2).expandY().right();
		attackButtons.row();
		abilityWidget3 = new AbilityWidget("ProjectileAttack");
		attackButtons.add(abilityWidget3).expandY().right();
		attackButtons.row();
		abilityWidget4 = new AbilityWidget("SweepAttack");
		attackButtons.add(abilityWidget4).expandY().right();
		attackButtons.row();
		
		uiTable.add(attackButtons).height(uiTable.getHeight()).expand().right();
		this.addActor(uiTable);
	}

	public void disable() {
		uiTable.setTouchable(Touchable.disabled);
	}

	public void onJumpTouched(Callback callback) {
		jumpBtnCallback = callback;
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
		abilityWidget4.setAbilityActivationListener(listener);
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

	public void setAbilitySlot4(Ability ability) {
		abilityWidget4.setAbility(ability);
	}

	public Ability getAbilitySlot4() {
		return abilityWidget4.getAbility();
	}
}
