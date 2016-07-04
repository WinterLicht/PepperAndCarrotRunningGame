package com.peppercarrot.runninggame.world;

public class LevelLayout {
	public static class LevelLayoutPart {
		private boolean shuffle;

		private int loopCount;

		private String[] tmxMaps;

		public boolean isShuffle() {
			return shuffle;
		}

		public void setShuffle(boolean shuffle) {
			this.shuffle = shuffle;
		}

		public int getLoopCount() {
			return loopCount;
		}

		public void setLoopCount(int loopCount) {
			this.loopCount = loopCount;
		}

		public String[] getTmxMaps() {
			return tmxMaps;
		}

		public void setTmxMaps(String[] tmxMaps) {
			this.tmxMaps = tmxMaps;
		}
	}

	private LevelLayoutPart entrancePart;

	private LevelLayoutPart loopPart;

	private LevelLayoutPart exitPart;

	public LevelLayoutPart getEntrancePart() {
		return entrancePart;
	}

	public void setEntrancePart(LevelLayoutPart entrancePart) {
		this.entrancePart = entrancePart;
	}

	public LevelLayoutPart getLoopPart() {
		return loopPart;
	}

	public void setLoopPart(LevelLayoutPart loopPart) {
		this.loopPart = loopPart;
	}

	public LevelLayoutPart getExitPart() {
		return exitPart;
	}

	public void setExitPart(LevelLayoutPart exitPart) {
		this.exitPart = exitPart;
	}
}
