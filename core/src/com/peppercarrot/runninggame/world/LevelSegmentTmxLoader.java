package com.peppercarrot.runninggame.world;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class LevelSegmentTmxLoader extends TmxMapLoader {
	@SuppressWarnings("rawtypes")
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle tmxFile, Parameters parameter) {
		final Array<AssetDescriptor> dependencies = super.getDependencies(fileName, tmxFile, parameter);

		// TODO: define layer names and properties which map to enemy types and
		// potions to be loaded

		return dependencies;
	}
}
