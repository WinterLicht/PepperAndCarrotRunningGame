package com.peppercarrot.runninggame.overworld;

public abstract class OverworldNode {
	private String name;

	private int id;

	private int x;

	private int y;

	private OverworldCondition condition;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public OverworldCondition getCondition() {
		return condition;
	}

	public void setCondition(OverworldCondition condition) {
		this.condition = condition;
	}
}