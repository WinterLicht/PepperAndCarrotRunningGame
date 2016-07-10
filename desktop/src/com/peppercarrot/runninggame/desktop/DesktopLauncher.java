package com.peppercarrot.runninggame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.utils.Constants;

public class DesktopLauncher {
	public static void main(String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pepper and Carrot Running Game";
		config.width = Constants.VIRTUAL_WIDTH;
		config.height = Constants.VIRTUAL_HEIGHT;
		new LwjglApplication(new PaCGame(), config);

		// TODO: to generate a texture atlas
		//TexturePacker.process("skin/", "skin/", "skin");
	}
}
