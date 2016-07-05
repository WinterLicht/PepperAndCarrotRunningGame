package com.peppercarrot.runninggame.overworld;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class OverworldLayoutLoader extends AsynchronousAssetLoader<OverworldLayout, OverworldLayoutLoader.Parameter> {
	private final Json json = new Json();

	public OverworldLayoutLoader() {
		this(new InternalFileHandleResolver());
	}

	public OverworldLayoutLoader(FileHandleResolver resolver) {
		super(resolver);

		json.addClassTag("level", OverworldLevelNode.class);
		json.addClassTag("story", OverworldStoryNode.class);
		json.addClassTag("empty", OverworldEmptyNode.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameter parameter) {
		return new Array<AssetDescriptor>();
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
	}

	@Override
	public OverworldLayout loadSync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
		final OverworldLayout layout = json.fromJson(OverworldLayout.class, file);
		return layout;
	}

	public static class Parameter extends AssetLoaderParameters<OverworldLayout> {
		public String filename;
	}

}
