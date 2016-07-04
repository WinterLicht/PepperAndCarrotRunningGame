package com.peppercarrot.runninggame.story;

import java.util.List;

public class Storyboard {
	public static class StoryPanel {
		private String atlasName;

		private String regionName;

		private float duration;

		public String getAtlasName() {
			return atlasName;
		}

		public void setAtlasName(String atlasName) {
			this.atlasName = atlasName;
		}

		public String getRegionName() {
			return regionName;
		}

		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}

		public float getDuration() {
			return duration;
		}

		public void setDuration(float duration) {
			this.duration = duration;
		}
	}

	private List<StoryPanel> panels;

	public List<StoryPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<StoryPanel> panels) {
		this.panels = panels;
	}
}
