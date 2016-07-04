package com.peppercarrot.runninggame.overworld;

public class OverworldEdge {
	private int sourceId;

	private int destinationId;

	private OverworldCondition condition;

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public int getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(int destinationId) {
		this.destinationId = destinationId;
	}

	public OverworldCondition getCondition() {
		return condition;
	}

	public void setCondition(OverworldCondition condition) {
		this.condition = condition;
	}
}