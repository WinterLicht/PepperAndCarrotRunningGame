package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.peppercarrot.runninggame.PaCGame;
import com.peppercarrot.runninggame.screens.WorldScreen;
import com.peppercarrot.runninggame.utils.Assets;
import com.peppercarrot.runninggame.utils.Constants;

public class StartStage extends Stage {
	Table rootTable;
	boolean goToWorldMap;

	public StartStage(Viewport viewport){
		super(viewport);
		this.rootTable = new Table(Assets.I.skin);
		rootTable.setFillParent(true);
		rootTable.setWidth(Constants.VIRTUAL_WIDTH);
		rootTable.setHeight(Constants.VIRTUAL_HEIGHT);
		rootTable.setBackground("button-down");
		rootTable.setTouchable(Touchable.enabled);
		rootTable.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				goToWorldMap = true;
				event.cancel();
				return true;
			}
		});
		rootTable.debug();
		rootTable.top().right();
		this.addActor(rootTable);
	}

	/**
	 * 
	 * @param delta
	 */
	public void render(float delta){
		this.act(delta);
		this.draw();
		if(goToWorldMap){
	    	switchScreen(0.25f);
	    }
	}
	
	/**
	 * small animation of the backgroundpicture that takes fadeOutTime long.
	 * @param fadeOutTime
	 */
	public void switchScreen(float fadeOutTime){
	    getRoot().getColor().a = 1;
	    SequenceAction sequenceAction = new SequenceAction();
	    sequenceAction.addAction( Actions.fadeOut(fadeOutTime));
	    sequenceAction.addAction( Actions.run(new Runnable() {
	        @Override
	        public void run() {
	            PaCGame.getInstance().setScreen(new WorldScreen(getViewport()));
	        }
	    }));
	    getRoot().addAction(sequenceAction);
	    /*
	    backgroundImage.getColor().a = 1;
	    SequenceAction sequenceAction2 = new SequenceAction();
	    sequenceAction2.addAction( Actions.fadeOut(fadeOutTime) );
	    backgroundImage.addAction(sequenceAction2);
	    */
	}
}
