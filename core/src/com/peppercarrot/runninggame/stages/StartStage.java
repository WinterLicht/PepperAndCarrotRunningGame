package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.utils.Account;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

/**
 * Contains shelve with ingredients and currently brewed potion.
 * 
 * @author WinterLicht
 *
 */
public class StartStage extends AbstractStage {
	Table rootTable;
	boolean goToWorldMap;
	ScrollPane shelve;
	ProgressBar potionProgress;

	//TODO later every potion can have its own amount:
	private final int INGREDIENTS_NEEDED_FOR_POTION = 10;

	public StartStage() {
		rootTable = new Table();
		rootTable.setFillParent(true);
		rootTable.setWidth(Constants.VIRTUAL_WIDTH-2*MainMenu.getInstance().buttonWidth);
		rootTable.setHeight(Constants.VIRTUAL_HEIGHT);
		rootTable.padRight(MainMenu.getInstance().buttonWidth+60);
		rootTable.padLeft(MainMenu.getInstance().buttonWidth+60);
		rootTable.top();

		Table container = new Table(Assets.I.skin);
		container.setBackground("bg_dark_brown");
		container.pad(5);
		shelve = new ScrollPane(container, Assets.I.skin);
		shelve.setScrollingDisabled(false, true);
		shelve.setVisible(false);
		//Fill the shelve
		for (String ingredient : Account.I.ingredients) {
			Image ingredientImage = new Image(new TextureRegion(Assets.I.atlas.findRegion(ingredient)));
			ingredientImage.setName(ingredient);
			ingredientImage.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y){
						if (this.getTapCount()==2) {	//Double click
							final Actor ingredient = event.getListenerActor();
							ingredient.setOrigin(Align.center);
							ingredient.setX(Constants.VIRTUAL_WIDTH/2);
							ingredient.setY(Constants.VIRTUAL_HEIGHT);
							Account.I.ingredients.remove(ingredient.getName());
							//update the brewing potion
							if (Account.I.brewedPotion.isEmpty()) {
								Account.I.brewedPotion = ingredient.getName().replace("ingredient", "potion");
								System.out.println(Account.I.brewedPotion);
							} else {
								Account.I.brewedPotionProgress += 1;
							}
							//
							rootTable.addActor(ingredient);
							//Animation
							final ParallelAction pAction = new ParallelAction();
							final SequenceAction sAction = new SequenceAction();
							//TODO:
							float caldronPosX = 0;
							float caldronPosY = 0;
							sAction.addAction(Actions.moveTo(caldronPosX, caldronPosY, 2f, Interpolation.pow2));
							sAction.addAction(Actions.run(new Runnable() {
								@Override
								public void run() {
									ingredient.clearActions();
									ingredient.setVisible(false);
									potionProgress.setValue(Account.I.brewedPotionProgress);
								}
							}));
							pAction.addAction(sAction);
							pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
							ingredient.addAction(pAction);
						}
					}
				});
			container.add(ingredientImage).width(ingredientImage.getWidth());
		}
		TextButton shelvebtn = new TextButton("ingredients", Assets.I.skin, "default");
		shelvebtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				toggleShelve();
				event.cancel();
			}
		});
		rootTable.add(shelvebtn);
		rootTable.row();
		rootTable.add(shelve).top();

		//
		potionProgress = new ProgressBar(Account.I.brewedPotionProgress, INGREDIENTS_NEEDED_FOR_POTION,
				1, true, Assets.I.skin, "default");
		potionProgress.setHeight(90);
		potionProgress.setX(250);
		potionProgress.setY(250);
		rootTable.addActor(potionProgress);
		//
		if (!Account.I.brewedPotion.isEmpty()) {
			//Set up potion display
			Image brewedPotionImage = new Image(Assets.I.atlas.findRegion(Account.I.brewedPotion));
			brewedPotionImage.setX(300);
			brewedPotionImage.setY(300);
			rootTable.addActor(brewedPotionImage);
		}

		this.addActor(rootTable);
	}

	private void toggleShelve() {
		shelve.setVisible(!shelve.isVisible());
	}

	/**
	 * 
	 * @param delta
	 */
	public void render(float delta) {
		this.act(delta);
		this.draw();
	}

	/*
	public void switchScreen(float fadeOutTime) {
		fadeOut(true, fadeOutTime, new Runnable() {
			@Override
			public void run() {
				ScreenSwitch.getInstance().setWorldScreen();
			}
		});
	}
	*/
}
