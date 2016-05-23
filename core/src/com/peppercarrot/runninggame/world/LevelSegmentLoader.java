package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.peppercarrot.runninggame.world.LevelSegmentLoader.Parameter;

public class LevelSegmentLoader extends AsynchronousAssetLoader<LevelSegment, Parameter> {

	private final OrthographicCamera camera;

	private final BatchTiledMapRenderer renderer;

	public static class Parameter extends AssetLoaderParameters<LevelSegment> {

		private final String tmxFile;

		public Parameter(String tmxFile) {
			this.tmxFile = tmxFile;

		}

		public String getTmxFile() {
			return tmxFile;
		}
	}

	public LevelSegmentLoader(OrthographicCamera camera, BatchTiledMapRenderer renderer) {
		super(new InternalFileHandleResolver());
		this.camera = camera;
		this.renderer = renderer;
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
	}

	@Override
	public LevelSegment loadSync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
		return new LevelSegment(camera, fileName, manager.get(parameter.getTmxFile(), TiledMap.class), renderer);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameter parameter) {
		final Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		dependencies.add(new AssetDescriptor<>(parameter.getTmxFile(), TiledMap.class));
		return dependencies;
	}

}
