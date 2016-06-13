package com.peppercarrot.runninggame.stages;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.peppercarrot.runninggame.screens.DefaultScreenConfiguration;

public abstract class AbstractStage extends Stage {
	public AbstractStage() {
		super(DefaultScreenConfiguration.getInstance().getViewport());
	}

	public void fadeOut(boolean initiallyResetAlpha, float fadeOutTime, Runnable onFinish) {
		final Group root = getRoot();

		if (initiallyResetAlpha) {
			root.getColor().a = 1;
		}

		final SequenceAction sequenceAction = new SequenceAction();
		sequenceAction.addAction(Actions.fadeOut(fadeOutTime));
		if (onFinish != null) {
			sequenceAction.addAction(Actions.run(onFinish));
		}

		root.addAction(sequenceAction);
	}
}
