package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.peppercarrot.runninggame.entities.Pepper;
import com.peppercarrot.runninggame.entities.Runner;
import com.peppercarrot.runninggame.entities.Runner.State;
import com.peppercarrot.runninggame.utils.Assets;

public class CharacterTable extends Table {
	ButtonGroup<TextButton> tabs;
	Table charDescription;
	Table skills;
	Table animations;
	ScrollPane info;
	Runner runner;

	public CharacterTable() {
		super(Assets.I.skin);

		int charCellWidth = 340;
		top();
		padLeft(charCellWidth);
		padTop(30);
		padBottom(60);

		runner = new Pepper("pepper");
		runner.setState(State.IDLE);
		runner.noGravity = true;
		runner.setY(140);
		runner.setX(110);
		addActor(runner);
		Label characterName = new Label("Pepper", Assets.I.skin, "title");
		characterName.setX(115);
		characterName.setY(504);
		addActor(characterName);

		tabs = new ButtonGroup<TextButton>();
		int buttonHeight = 56;
		int paddingScrollPane = 10;
		int descriptionWidth = 460;

		charDescription = new Table();
		Label descriptionText = new Label("Being a witch of Chaosah, Pepper is able to summon creatures from other worlds and can distort time." +
				" Unfortunately, the magic of Chaosah is unpredictable and she gets in various adventures because of it." +
				" Pepper loves to study magic and brew potions.", Assets.I.skin, "default");
		descriptionText.setWrap(true);
		charDescription.padLeft(paddingScrollPane);
		charDescription.padRight(paddingScrollPane);
		charDescription.add(descriptionText).width(descriptionWidth);

		info = new ScrollPane(charDescription, Assets.I.skin);
		info.setScrollingDisabled(true, false);
		info.setFadeScrollBars(false);

		float skillScale = 0.7f;
		skills = new Table();
		skills.padLeft(paddingScrollPane);
		skills.padRight(paddingScrollPane);
		for (int n = 1; n < 4; n++) {
			String skillNameStrg = " ";
			String skillDescription = " ";
			switch (n) {
			case 1:
				skillNameStrg = "Carrot Charge";
				skillDescription = "Carrot attacks up to three next visible enemies and then retrns to Pepper. " +
						"If there are no enemies in sight by activation time, skill-energy will be lost.";
				break;
			case 2:
				skillNameStrg = "Time Distortion";
				skillDescription = "Pepper controls time for a short duration." +
						" She moves faster and takes no damage from enemies. " +
						"Enemies are destroyed on collision.";
				break;
			case 3:
				skillNameStrg = "Black Hole";
				skillDescription = "All visible enemies and potions are wiped away from the screen.";
				break;
			default:
				break;
			}
			Label skillName = new Label(skillNameStrg, Assets.I.skin, "default");
			skills.add(skillName).left();
			Image skillImage = new Image(Assets.I.skin, "button_skill"+n);
			skillImage.setColor(Assets.I.skin.getColor("black"));
			skills.add(skillImage).width(skillImage.getWidth()*skillScale).height(skillImage.getHeight()*skillScale).padBottom(17).padTop(30).center();
			skills.row();
			Label skillsText = new Label(skillDescription, Assets.I.skin, "default");
			skillsText.setWrap(true);
			skills.add(skillsText).colspan(2).width(descriptionWidth);
			skills.row();
		}
		
		setUpAnimationInfo();

		TextButton infobtn = new TextButton("info", Assets.I.skin, "tabs");
		infobtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				info.setWidget(charDescription);
				event.cancel();
			}
		});
		infobtn.setHeight(buttonHeight);
		tabs.add(infobtn);
		add(infobtn).width(descriptionWidth/3).height(buttonHeight);
		TextButton skillsbtn = new TextButton("skills", Assets.I.skin, "tabs");
		skillsbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				info.setWidget(skills);
				event.cancel();
			}
		});
		skillsbtn.setHeight(buttonHeight);
		tabs.add(skillsbtn);
		add(skillsbtn).width(descriptionWidth/3).height(buttonHeight);
		TextButton animbtn = new TextButton("animations", Assets.I.skin, "tabs");
		animbtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				info.setWidget(animations);
				event.cancel();
			}
		});
		animbtn.setHeight(buttonHeight);
		tabs.add(animbtn);
		add(animbtn).width(descriptionWidth/3).height(buttonHeight);
		row();
		add(info).padTop(24).colspan(3);
	}

	private void setUpAnimationInfo() {
		animations = new Table();
		int buttonHeight = 56;
		int buttonWidth = 480; // descriptionWidth + paddingScrollPane*2
		animations.center();
		ButtonGroup<TextButton> animationBtns = new ButtonGroup<TextButton>();
		TextButton idle = new TextButton("idle", Assets.I.skin, "transparent-bg");
		TextButton run = new TextButton("run", Assets.I.skin, "transparent-bg");
		TextButton jump = new TextButton("jump", Assets.I.skin, "transparent-bg");
		TextButton doublejump = new TextButton("doublejump", Assets.I.skin, "transparent-bg");
		TextButton fall = new TextButton("fall", Assets.I.skin, "transparent-bg");
		TextButton attack = new TextButton("attack", Assets.I.skin, "transparent-bg");
		TextButton hit = new TextButton("hit", Assets.I.skin, "transparent-bg");
		animationBtns.add(idle,run,jump,doublejump,fall,attack,hit);
		animations.add(idle).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(run).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(jump).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(doublejump).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(fall).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(attack).height(buttonHeight).width(buttonWidth);
		animations.row();
		animations.add(hit).height(buttonHeight).width(buttonWidth);
		runner.setScaleFactor(1f);
		idle.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.setState(Runner.State.IDLE);
				event.cancel();
			}
		});
		run.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.setState(Runner.State.RUNNING);
				event.cancel();
			}
		});
		jump.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.setState(Runner.State.JUMPING);
				event.cancel();
			}
		});
		doublejump.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.setState(Runner.State.DOUBLEJUMPING);
				event.cancel();
			}
		});
		fall.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.setState(Runner.State.FALLING);
				event.cancel();
			}
		});
		attack.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = false;
				runner.attackingAnim.setPlayMode(Animation.PlayMode.LOOP);
				runner.setState(Runner.State.ATTACK_RUNNING);
				event.cancel();
			}
		});
		hit.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				runner.stunned = true;
				event.cancel();
			}
		});
	}
}
