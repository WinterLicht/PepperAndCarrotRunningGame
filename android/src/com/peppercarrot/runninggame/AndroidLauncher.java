package com.peppercarrot.runninggame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.peppercarrot.runninggame.PaCGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//To conserve battery disable the accelerometer and compass
		config.useAccelerometer = false;
	    config.useCompass = false;
	    PaCGame game = PaCGame.getInstance();
		initialize(game, config);
	}
}
