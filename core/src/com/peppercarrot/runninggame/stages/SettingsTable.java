package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;

public class SettingsTable extends Table {
	TextButton resetButton;

	public SettingsTable() {
		super();
		top();
		padTop(30);
		resetButton = new TextButton ("RESET ACCOUNT", Assets.I.skin);
		resetButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Account.I.reset();
				resetButton.setText("GAME RESETED");
				resetButton.setDisabled(true);
				event.cancel();
			}
		});
		Label message = new Label("This operation resets the game and deletes all your  progress!",
				Assets.I.skin, "default");
		message.setWrap(true);

		add(message).width(700);
		row();
		add(resetButton).width(300).height(60).padTop(100);
	}
}
