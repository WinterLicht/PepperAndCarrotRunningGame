package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.Callback;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class WorldUiStage extends AbstractStage {
	Table uiTable;
	Label hintLabel;
	Table attackButtons;
	private final Button jumpBtnTransparent;
	private final Runner runner;

	public WorldUiStage(Runner runner) {
		this.runner = runner;
		final int uiPadding = 30; // padding of borders for ui in pixel
		uiTable = new Table();
		uiTable.setFillParent(true);
		uiTable.pad(uiPadding);
		uiTable.setWidth(Constants.VIRTUAL_WIDTH - uiPadding * 2);
		uiTable.setHeight(Constants.VIRTUAL_HEIGHT - uiPadding * 2);

		jumpBtnTransparent = new Button(Assets.I.skin, "transparent");
		final int jumpBtnTransparentWidth = 470;
		jumpBtnTransparent.setTouchable(Touchable.enabled);
		hintLabel = new Label("press on the left side of the screen to 'jump'", Assets.I.skin, "default");
		hintLabel.setWrap(true);
		hintLabel.setTouchable(Touchable.disabled);
		jumpBtnTransparent.add(hintLabel).width(jumpBtnTransparentWidth).top();
		jumpBtnTransparent.top();

		// TextButton jumpButton = new TextButton ("JUMP", Assets.I.skin,
		// "default");
		// TODO: try something else to pass touch event to this button
		// jumpButton.setTouchable(Touchable.disabled);
		// jumpBtnTransparent.add(jumpButton);
		// jumpBtnTransparent.bottom().left();
		uiTable.add(jumpBtnTransparent).width(jumpBtnTransparentWidth).height(uiTable.getHeight()).expandX().left();
		// Attack Buttons
		attackButtons = new Table();
		attackButtons.add(runner.ability3.table).expandY().right().top();
		attackButtons.row();
		attackButtons.add(runner.ability2.table).expandY().right().center();
		attackButtons.row();
		attackButtons.add(runner.ability1.table).expandY().right().bottom();
		attackButtons.row();

		uiTable.add(attackButtons).height(uiTable.getHeight()).expand().right();
		this.addActor(uiTable);
	}

	public void disable() {
		uiTable.setTouchable(Touchable.disabled);
	}

	public void onJumpTouched(Callback callback) {
		jumpBtnTransparent.addListener(new InputListener() {
			private boolean hintFaded = false;

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (!hintFaded) {
					hintLabel.addAction(Actions.fadeOut(0.48f));
					callback.invoke();
					hintFaded = true;
				}
				runner.jump();
				return true;
			}
		});
	}
}
