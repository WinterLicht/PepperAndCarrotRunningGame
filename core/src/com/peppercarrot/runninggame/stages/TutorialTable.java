package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.utils.Assets;

public class TutorialTable extends Table {
	ScrollPane scroll;
	
	public TutorialTable() {
		super(Assets.I.skin);
		
		Table container = new Table();
		scroll = new ScrollPane(container, Assets.I.skin);

		Texture texture;
		//TODO !!!
		texture = new Texture(Gdx.files.internal("screenshot.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image tutorial = new Image(texture);
		container.add(tutorial).width(1280).height(720);

		scroll.setScrollingDisabled(false, false);
		scroll.setFadeScrollBars(false);

		add(scroll).expand().fill();
		row();
	}

}
