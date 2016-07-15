package me.cristaling.UltimateRides.moveables;

import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class ComplexRotorMove extends Moveable {

	private int radius;

	private int speed = 120;
	private int cAngle = 0;
	private boolean clockWise;
	// Vector direction = new Vector(0, 0, 1);

	public ComplexRotorMove(Vector origin, int radius, boolean clockWise) {
		setOrigin(origin);
		setType(MoveableType.ROTOR);
		this.setRadius(radius);
		this.setClockWise(clockWise);
	}

	public Vector getChildLocation(Moveable child) {
		if (getChildren().contains(child)) {
			int index = getChildren().indexOf(child);
			double circAngle = index * 21600 / getChildren().size();
			circAngle += getAngle();
			circAngle *= Math.PI;
			circAngle /= 10800;
			return new Vector(getOrigin().getX() + Math.sin(circAngle) * getRadius(), getOrigin().getY(),
					getOrigin().getZ() + Math.cos(circAngle) * getRadius());
		}
		return null;
	}

	@Override
	public void moveTo(Vector vector) {
		/*
		 * Vector newDir =
		 * vector.clone().subtract(getOrigin()).setY(0).normalize(); int
		 * offSetAngle1 = (int)Math.round(Math.acos(newDir.getZ()) * 10800 /
		 * Math.PI); if(newDir.getX() < 0){ offSetAngle1 = 360 - offSetAngle1; }
		 * int offSetAngle2 = (int)Math.round(Math.acos(direction.getZ()) *
		 * 10800 / Math.PI); if(direction.getX() < 0){ offSetAngle2 = 360 -
		 * offSetAngle2; } cAngle += offSetAngle1 - offSetAngle2; direction =
		 * newDir;
		 */
		setOrigin(vector);
	}

	@Override
	public void runTick(boolean started) {
		if (started) {
			if (isClockWise()) {
				setAngle(getAngle() - getSpeed());
				while (getAngle() < 0) {
					setAngle(getAngle() + 21600);
				}
			} else {
				setAngle(getAngle() + getSpeed());
				while (getAngle() >= 21600) {
					setAngle(getAngle() - 21600);
				}
			}
		}
		for (Moveable child : getChildren()) {
			if (started) {
				child.moveTo(getChildLocation(child));
			}
			child.runTick(started);
		}
	}

	@Override
	public void addChild(Moveable child) {
		getChildren().add(child);
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public boolean isClockWise() {
		return clockWise;
	}

	public void setClockWise(boolean clockWise) {
		this.clockWise = clockWise;
	}

	public int getAngle() {
		return cAngle;
	}

	public void setAngle(int cAngle) {
		this.cAngle = cAngle;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
