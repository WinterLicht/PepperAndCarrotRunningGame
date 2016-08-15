package com.peppercarrot.runninggame.overworld;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.peppercarrot.runninggame.stages.AbstractStage;
import com.peppercarrot.runninggame.utils.Assets;

/**
 * Dummy UI for overworld.
 * 
 * @author momsen
 *
 */
public class OverworldStage extends AbstractStage {
	Image backgroundImage;
	Table table;

	public interface OnLevelSelectListener {
		void onLevelSelect(OverworldLevelNode level);

		void onStorySelect(OverworldStoryNode story);
	}

	private OnLevelSelectListener listener;

	public OverworldStage(OverworldLayout layout) {
		table = new Table(Assets.I.skin);
		table.setFillParent(true);
		Texture texture;
		texture = new Texture(Gdx.files.internal("world.png"), true);
		texture.setFilter(TextureFilter.MipMapLinearNearest, TextureFilter.Nearest);
		backgroundImage = new Image(texture);
		table.addActor(backgroundImage);
		createUi(layout);
		addActor(table);
	}

	private void createUi(OverworldLayout layout) {
		final Table levelSelectTable = new Table();

		final int numColumns = layout.getMaxX() - layout.getMinX() + 1;
		final int numRows = layout.getMaxY() - layout.getMinY() + 1;
		final Cell<?>[][] nodeCellMap = new Cell[numRows][numColumns];
		final Cell<?>[][] edgeCellHorizontallMap = new Cell[numRows][numColumns - 1];
		final Cell<?>[][] edgeCellVerticalMap = new Cell[numRows - 1][numColumns];
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColumns; x++) {
				nodeCellMap[y][x] = levelSelectTable.add().center().width(100).height(100);
				if (x < numColumns - 1) {
					edgeCellHorizontallMap[y][x] = levelSelectTable.add().center().width(10).height(10);
				}
			}

			levelSelectTable.row();

			if (y < numRows - 1) {
				for (int x = 0; x < numColumns; x++) {
					edgeCellVerticalMap[y][x] = levelSelectTable.add().center().width(10).height(10);
					levelSelectTable.add();
				}
				levelSelectTable.row();
			}
		}
		levelSelectTable.pad(30);

		final Map<Integer, OverworldNode> nodeMap = new HashMap<>();
		for (final OverworldNode node : layout.getNodes()) {
			nodeMap.put(node.getId(), node);
			final Cell<?> cell = nodeCellMap[node.getY()][node.getX()];

			final ImageButton selectButton = createButton(node);
			if (selectButton != null) {
				cell.setActor(selectButton);
			}
		}

		for (final OverworldEdge edge : layout.getEdges()) {
			// TODO: vorher validieren
			final OverworldNode source = nodeMap.get(edge.getSourceId());
			final OverworldNode destination = nodeMap.get(edge.getDestinationId());

			final boolean isVertically = source.getY() != destination.getY();

			if (isVertically) {
				final int x = destination.getX() - layout.getMinX();
				final int y = Math.max(destination.getY(), source.getY()) - layout.getMinY() - 1;
				final Cell<?> cell = edgeCellVerticalMap[y][x];
				cell.setActor(new Label("#", Assets.I.skin));
			} else {
				final int x = source.getX() - layout.getMinX();
				final int y = source.getY() - layout.getMinY();
				final Cell<?> cell = edgeCellHorizontallMap[y][x];
				cell.setActor(new Label("#", Assets.I.skin));
			}
		}

		final ScrollPane scrollPane = new ScrollPane(levelSelectTable);
		table.add(scrollPane).expand().center();

		// setDebugAll(true);
	}

	private ImageButton createButton(OverworldNode node) {
		if (node instanceof OverworldLevelNode) {
			final OverworldLevelNode levelNode = ((OverworldLevelNode) node);

			final ImageButton button = new ImageButton(Assets.I.skin, "button_jump");
			button.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					selectLevel(levelNode);
					return true;
				}
			});
			return button;
		} else if (node instanceof OverworldStoryNode) {
			final OverworldStoryNode storyNode = ((OverworldStoryNode) node);
			final ImageButton button = new ImageButton(Assets.I.skin, "button_exit");
			button.addListener(new InputListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					selectStory(storyNode);
					return true;
				}

			});
			return button;
		}

		return null;
	}

	protected void selectStory(OverworldStoryNode storyNode) {
		if (listener != null) {
			listener.onStorySelect(storyNode);
		}
	}

	protected void selectLevel(OverworldLevelNode levelNode) {
		if (listener != null) {
			listener.onLevelSelect(levelNode);
		}
	}

	public void render(float delta) {
		act(delta);
		draw();
	}

	public void setListener(OnLevelSelectListener listener) {
		this.listener = listener;
	}
}
