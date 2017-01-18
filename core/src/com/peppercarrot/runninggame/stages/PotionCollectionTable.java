package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.utils.Assets;

public class PotionCollectionTable extends Table {

	public PotionCollectionTable() {
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

		for (int n = 1; n < 11; n++) {
			String potionNameStrg = " ";
			String potionDescription = " ";
			String potionImageFileName = " ";
			switch (n) {
			case 1:
				potionNameStrg = "Orange Potion";
				potionImageFileName = "potion_orange";
				potionDescription = "Charges orange skill.";
				break;
			case 2:
				potionNameStrg = "Green Potion";
				potionImageFileName = "potion_green";
				potionDescription = "Charges green skill.";
				break;
			case 3:
				potionNameStrg = "Blue Potion";
				potionImageFileName = "potion_blue";
				potionDescription = "Charges blue skill.";
				break;
			case 4:
				potionNameStrg = "Health Potion";
				potionImageFileName = "potion_pink";
				potionDescription = "Refills one heart.";
				break;
			case 5:
				potionNameStrg = "Lemonade Potion";
				potionImageFileName = "potion_sour-1";
				potionDescription = "Can be brewed by using a citron. Tastes pretty sour.";
				break;
			case 6:
				potionNameStrg = "Strange Vegetable Juice Potion";
				potionImageFileName = "potion_sour-2";
				potionDescription = "Can be brewed by using a yellow paprika. Tastes surprisingly refreshing.";
				break;
			case 7:
				potionNameStrg = "Sauerkraut Potion";
				potionImageFileName = "potion_sour-3";
				potionDescription = "Can be brewed by using a golden cabbage. Pickled cabbage, sour-grade variable.";
				break;
			//TODO: later for ingredients own tab!
			case 8:
				potionNameStrg = "Citron";
				potionImageFileName = "ingredient_sour-1";
				potionDescription = "Use to brew a lemonade potion.";
				break;
			case 9:
				potionNameStrg = "Yellow Paprika";
				potionImageFileName = "ingredient_sour-2";
				potionDescription = "Use to brew a strange vegetable juice potion.";
				break;
			case 10:
				potionNameStrg = "Golden Cabbage";
				potionImageFileName = "ingredient_sour-3";
				potionDescription = "Use to brew a sauerkraut potion.";
				break;	
			default:
				break;
			}
			Label potionName = new Label(potionNameStrg, Assets.I.skin, "default");
			content.add(potionName).left();
			Image potionImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(potionImageFileName)));
			//potionImage.setColor(Assets.I.skin.getColor("black")); //Can be used to lock-unlock
			content.add(potionImage).width(potionImage.getWidth()).height(potionImage.getHeight()).padBottom(17).padTop(30).center();
			content.row();
			Label skillsText = new Label(potionDescription, Assets.I.skin, "default");
			skillsText.setWrap(true);
			content.add(skillsText).colspan(2).width(600).padRight(100);
			content.row();
		}
		add(scroll);
	}
}
