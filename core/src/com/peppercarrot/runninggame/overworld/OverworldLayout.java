package com.peppercarrot.runninggame.overworld;

import java.util.List;

public class OverworldLayout {
	private List<OverworldNode> nodes;

	private List<OverworldEdge> edges;

	public List<OverworldNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<OverworldNode> nodes) {
		this.nodes = nodes;
	}

	public List<OverworldEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<OverworldEdge> edges) {
		this.edges = edges;
	}
}
