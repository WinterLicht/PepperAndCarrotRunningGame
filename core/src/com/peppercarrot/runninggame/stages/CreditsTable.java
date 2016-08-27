package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.utils.Assets;

public class CreditsTable extends Table {

	public CreditsTable() {
		super();

		Table content = new Table(Assets.I.skin);
		ScrollPane scroll = new ScrollPane(content, Assets.I.skin);
		scroll.setScrollingDisabled(true, false);
		scroll.setFadeScrollBars(false);

		top();
		padTop(30);
		padBottom(60);
		int paddingScrollPane = 10;
		content.padLeft(paddingScrollPane);
		content.padRight(paddingScrollPane);
		Texture texture;
		texture = new Texture(Gdx.files.internal("logo_original_webcomic.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image originalLogo = new Image(texture);
		texture = new Texture(Gdx.files.internal("Pepper_And_Carrot_Running_Game.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		Image gameLogo = new Image(texture);

		Label label1 = new Label("This game is open-source and a derivative of universe of the webcomic" +
				" Pepper&Carrot created by David Revoy and licensed under CC-BY 4.0", Assets.I.skin, "default");
		label1.setWrap(true);
		/*
		Label label2 = new Label("Visit original Pepper&Carrot website. (Link in this Version is deactivated)", Assets.I.skin, "default");
		label2.setWrap(true);
		*/
		TextButton webComicLink = new TextButton("Visit original"+'\n'+"Pepper&Carrot"+'\n'+"website", Assets.I.skin);
		webComicLink.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.net.openURI("http://www.peppercarrot.com/");
				event.cancel();
	        }
	    });
	    
		TextButton gitHubLink = new TextButton("This game"+'\n'+"can be found"+'\n'+"on GitHub", Assets.I.skin);
		gitHubLink.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				Gdx.net.openURI("https://github.com/WinterLicht/PepperAndCarrotRunningGame");
				event.cancel();
	        }
	    });
		/*
		Label label3 = new Label("This game is open source and can be found on GitHub. (Link in this Version is deactivated)", Assets.I.skin, "default");
		label3.setWrap(true);
		*/
		content.add(label1).width(700).colspan(2);
		content.row();
		content.add(webComicLink).width(280).padTop(30).left();
		content.add(originalLogo).width(originalLogo.getWidth()).padTop(30).right();
		content.row();
		content.add(gitHubLink).width(280).padTop(30).left();
		content.add(gameLogo).width(gameLogo.getWidth()).padTop(30).right();
		content.row();

		add(scroll);
	}

}
