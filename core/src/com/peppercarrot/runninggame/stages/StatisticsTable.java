package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;

public class StatisticsTable extends Table {

	public StatisticsTable() {
		super();
		top();
		padTop(60);
		Table container = new Table(Assets.I.skin);
		
		container.add("Completed Levels: "+Account.I.completeLevels).left().padBottom(20);
		container.row();
		container.add("Hunted Enemies: "+Account.I.huntEnemies).left().padBottom(20);
		container.row();
		container.add("Used Skills: "+Account.I.usedSkills).left().padBottom(20);
		container.row();
		container.add("Jumps: "+Account.I.jumps).left().padBottom(20);
		container.row();
		container.add("Collected Potion: "+Account.I.collectedPotions).left().padBottom(20);
		container.row();
		container.add("Died: "+Account.I.died).left().padBottom(20);
		container.row();
		container.add("LevelsWithoutKilling: "+Account.I.levelsWithoutKilling).left().padBottom(20);
		container.row();
		container.add("LevelsWithoutHealthLost: "+Account.I.levelsWithoutHealthLost).left();
		
		ScrollPane scroll = new ScrollPane(container, Assets.I.skin);
		scroll.setScrollingDisabled(true, false);
		scroll.setFadeScrollBars(false);

		add(scroll).fill();
		row();
	}
}
