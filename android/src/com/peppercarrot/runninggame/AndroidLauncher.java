package com.peppercarrot.runninggame;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// To conserve battery disable the accelerometer and compass
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new PaCGame(), config);
	}
}
