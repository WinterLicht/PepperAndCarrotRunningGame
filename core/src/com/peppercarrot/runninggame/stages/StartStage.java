package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.peppercarrot.runninggame.entities.Pepper;
import com.peppercarrot.runninggame.entities.Runner.State;
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
	Image brewedPotionImage;
	ProgressBar potionProgress;
	Image ghost;
	Label hint;

	private final int CAULDRON_POS_X = 933;
	private final int CAULDRON_POS_Y = 127;
	//TODO later every potion can have its own amount!
	private final int INGREDIENTS_NEEDED_FOR_POTION = 15;

	public StartStage() {
		rootTable = new Table();
		rootTable.setFillParent(true);
		int paddingToMainMenu = 60;
		rootTable.padRight(MainMenu.getInstance().buttonWidth+paddingToMainMenu);
		rootTable.padLeft(MainMenu.getInstance().buttonWidth+paddingToMainMenu);
		rootTable.top();
		rootTable.addActor(Assets.I.bgTopTexture);
		//Add Pepper
		Pepper runner = new Pepper("pepper");
		runner.setState(State.IDLE);
		runner.noGravity = true;
		runner.setScaleFactor(1f);
		runner.setY(101);
		runner.setX(496);
		runner.idleAnim.flipHorizontally();
		rootTable.addActor(runner);
		//
		Table container = new Table(Assets.I.skin);
		//container.setBackground("bg_dark_brown");
		shelve = new ScrollPane(container, Assets.I.skin);
		shelve.setScrollingDisabled(false, true);
		shelve.setFadeScrollBars(false);
		shelve.setOverscroll(false, false);
		//shelve.setVisible(false);
		//Fill the shelve
		for (String ingredient : Account.I.ingredients) {
			Image ingredientActor = new Image(new TextureRegion(Assets.I.atlas.findRegion(ingredient)));
			ingredientActor.setName(ingredient);
			ingredientActor.addListener(new ClickListener() {
					public void clicked(InputEvent event, float x, float y){
						if (this.getTapCount()==2) { //Double click
							if (!potionIsReady()) {
								final Actor ingredient = event.getListenerActor();
								ingredient.setOrigin(Align.center);
								ingredient.setX(Constants.VIRTUAL_WIDTH/2);
								ingredient.setY(Constants.VIRTUAL_HEIGHT);
								Account.I.ingredients.remove(ingredient.getName());

								rootTable.addActor(ingredient);
								//play animation
								final ParallelAction pAction = new ParallelAction();
								final SequenceAction sAction = new SequenceAction();
								sAction.addAction(Actions.moveTo(CAULDRON_POS_X, CAULDRON_POS_Y, 0.8f,
										Interpolation.pow2));
								sAction.addAction(Actions.run(new Runnable() {
									@Override
									public void run() {
										ingredient.clearActions();
										ingredient.setVisible(false);
										updateBrewing(ingredient.getName());
									}
								}));
								pAction.addAction(sAction);
								pAction.addAction(Actions.forever(Actions.rotateBy(360f, 0.8f)));
								ingredient.addAction(pAction);
							}
						}
					}
				});
			container.add(ingredientActor).width(ingredientActor.getWidth()).padLeft(3).padRight(3);
		}
		TextButton shelvebtn = new TextButton("ingredients", Assets.I.skin, "default");
		shelvebtn.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				toggleShelve();
				event.cancel();
			}
		});
		//rootTable.add(shelvebtn);
		//rootTable.row();
		hint = new Label(" ", Assets.I.skin, "default-white");
		updateHint();
		rootTable.add(shelve).expandX().height(110).top();
		rootTable.row();
		rootTable.add(hint).bottom().expandY().padBottom(30);

		if (!Account.I.brewedPotion.isEmpty()) {
			setUpBrewingVisualisation();
			if (potionIsReady()) {
				setUpReadyPotion();
			}
		}

		setUpGhostImage();

		this.addActor(rootTable);
	}

	private boolean potionIsReady() {
		return Account.I.brewedPotionProgress >= INGREDIENTS_NEEDED_FOR_POTION;
	}

	private void updateHint() {
		if (Account.I.ingredients.isEmpty()) {
			hint.setText("Gather some ingredients first. (Go to Pepper's basement)");
		}
		if (Account.I.ingredients.size() > 0) {
			hint.setText("Double-click on ingredients to put into the cauldron.");
		}
		if (potionIsReady()) {
			hint.setText("Potion is ready! You can use it on the ghost.");
		}
	}

	private void setUpGhostImage() {
		String name = "ghost_";
		if (Account.I.ghostID == 0) {
			ghost = new Image(new TextureRegion(Assets.I.atlas.findRegion(name+"basic")));
		} else {
			ghost = new Image(new TextureRegion(Assets.I.atlas.findRegion(name+"sour-"+Account.I.ghostID)));
		}
		ghost.setX(259);
		ghost.setY(261);
		rootTable.addActor(ghost);
	}

	private void upgradeGhost(String potionName) {
		int potionID = Integer.parseInt(potionName.replace("potion_sour-", ""));
		Account.I.ghostID = potionID;
		rootTable.removeActor(ghost);
		setUpGhostImage();
	}

	/**
	 * @param ingredientName
	 */
	private void updateBrewing(String ingredientName) {
		updateHint();
		if (Account.I.brewedPotion.isEmpty()) {
			//start brew a new potion
			Account.I.brewedPotion = ingredientName.replace("ingredient", "potion");
			setUpBrewingVisualisation();
		} else {
			Account.I.brewedPotionProgress += 1;
			if (potionIsReady()) {
				setUpReadyPotion();
			}
			potionProgress.setValue(Account.I.brewedPotionProgress);
		}
	}

	/**
	 * Only to use if there is already one potion brewing
	 */
	private void setUpBrewingVisualisation() {
		potionProgress = new ProgressBar(0, INGREDIENTS_NEEDED_FOR_POTION,
				1, true, Assets.I.skin, "default");
		potionProgress.setValue(Account.I.brewedPotionProgress);
		potionProgress.setHeight(90);
		potionProgress.setX(CAULDRON_POS_X+100);
		potionProgress.setY(CAULDRON_POS_Y);
		rootTable.addActor(potionProgress);
		//Set up potion display
		brewedPotionImage = new Image(Assets.I.atlas.findRegion(Account.I.brewedPotion));
		brewedPotionImage.setName(Account.I.brewedPotion);
		brewedPotionImage.setVisible(true);
		brewedPotionImage.setX(CAULDRON_POS_X);
		brewedPotionImage.setY(CAULDRON_POS_Y);
		rootTable.addActor(brewedPotionImage);
	}

	/**
	 * if a potion is ready
	 */
	private void setUpReadyPotion() {
		updateHint();
		brewedPotionImage.clearActions();
		brewedPotionImage.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y){
				if (this.getTapCount()==2) {	//Double click
					//set shelve untouchable
					shelve.setTouchable(Touchable.disabled);
					//play animation
					final SequenceAction sAction = new SequenceAction();
					sAction.addAction(Actions.moveTo(300, 300, 0.8f, Interpolation.pow2));
					sAction.addAction(Actions.run(new Runnable() {
						@Override
						public void run() {
							brewedPotionImage.clearActions();
							brewedPotionImage.setVisible(false);
							upgradeGhost(brewedPotionImage.getName());
							//reset brewing potion in account
							Account.I.brewedPotion = "";
							Account.I.brewedPotionProgress = 0;
							//set shelve touchable again
							shelve.setTouchable(Touchable.enabled);
						}
					}));
					brewedPotionImage.addAction(sAction);
				}
			}
		});
	}

	private void toggleShelve() {
		shelve.setVisible(!shelve.isVisible());
	}

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
