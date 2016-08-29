package me.cristaling.UltimateRides.moveables;

import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class EmptyMoveable extends Moveable{

	public EmptyMoveable(Vector origin) {
		setOrigin(origin);
		setType(MoveableType.EMPTY);
	}

	@Override
	public void moveTo(Vector vector) {
		setOrigin(vector);
	}

	@Override
	public void runTick(boolean started) {
		for (Moveable child : getChildren()) {
			if (started) {
				child.moveTo(getOrigin());
			}
			child.runTick(started);
		}
	}

	@Override
	public void addChild(Moveable child) {
		getChildren().add(child);
	}

}
