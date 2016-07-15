package me.cristaling.UltimateRides.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

public abstract class Moveable {

	private Vector origin;
	private MoveableType type;

	private List<Moveable> children = new ArrayList<Moveable>();

	public abstract void moveTo(Vector vector);

	public abstract void runTick(boolean started);

	public abstract void addChild(Moveable child);

	public Vector getOrigin() {
		return origin.clone();
	}

	public void setOrigin(Vector origin) {
		this.origin = origin.clone();
	}

	public MoveableType getType() {
		return type;
	}

	public void setType(MoveableType type) {
		this.type = type;
	}

	public List<Moveable> getChildren() {
		return children;
	}

	public void setChildren(List<Moveable> children) {
		this.children = children;
	}

}
