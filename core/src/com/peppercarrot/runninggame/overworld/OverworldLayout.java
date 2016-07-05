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

	public int getMinX() {
		if (nodes.isEmpty()) {
			return 0;
		}

		int min = nodes.get(0).getX();
		for (final OverworldNode node : nodes) {
			if (node.getX() < min) {
				min = node.getX();
			}
		}
		return min;
	}

	public int getMaxX() {
		if (nodes.isEmpty()) {
			return 0;
		}

		int max = nodes.get(0).getX();
		for (final OverworldNode node : nodes) {
			if (node.getX() > max) {
				max = node.getX();
			}
		}
		return max;

	}

	public int getMinY() {
		if (nodes.isEmpty()) {
			return 0;
		}

		int min = nodes.get(0).getY();
		for (final OverworldNode node : nodes) {
			if (node.getY() < min) {
				min = node.getY();
			}
		}
		return min;
	}

	public int getMaxY() {
		if (nodes.isEmpty()) {
			return 0;
		}

		int max = nodes.get(0).getY();
		for (final OverworldNode node : nodes) {
			if (node.getY() > max) {
				max = node.getY();
			}
		}
		return max;
	}
}
