package me.cristaling.UltimateRides.moveables;

import org.bukkit.World;
import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class Ride extends Moveable{

	//Location origin;
	private String name;

	private World world;
	
	private boolean started = false;

	public Ride(String name, Vector origin, World world) {
		this.setWorld(world);
		setOrigin(origin);
		setType(MoveableType.RIDE);
		this.setName(name);
	}

	public void start() {
		setStarted(true);
	}

	public void stop() {
		setStarted(false);
	}

	public void addMoveable(Moveable child) {
		getChildren().add(child);
	}

	@Override
	public void runTick(boolean started) {
		for (Moveable child : getChildren()) {
			child.runTick(isStarted());
		}
	}

	@Override
	public void moveTo(Vector vector) {
		setOrigin(vector);
	}

	@Override
	public void addChild(Moveable child) {
		getChildren().add(child);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

}
