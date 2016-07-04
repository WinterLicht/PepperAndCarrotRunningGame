package com.peppercarrot.runninggame.overworld;

import java.util.List;

public class OverworldCondition {

	private List<Integer> conditionalCompletedNodeIds;

	private List<String> additionalConditions;

	public List<Integer> getConditionalCompletedNodeIds() {
		return conditionalCompletedNodeIds;
	}

	public void setConditionalCompletedNodeIds(List<Integer> conditionalCompletedNodeIds) {
		this.conditionalCompletedNodeIds = conditionalCompletedNodeIds;
	}

	public List<String> getAdditionalConditions() {
		return additionalConditions;
	}

	public void setAdditionalConditions(List<String> additionalConditions) {
		this.additionalConditions = additionalConditions;
	}
}
