package me.cristaling.UltimateRides.moveables;

import java.util.HashMap;

import org.bukkit.entity.ArmorStand;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class ComplexStructureMove extends Moveable {

	private int templateID;
	private double yaw = 0;
	private double pitch = 0;
	private HashMap<ArmorStand, Vector> model = new HashMap<ArmorStand, Vector>();

	public ComplexStructureMove(int templateID, Vector origin) {
		this.setTemplateID(templateID);
		setOrigin(origin);
		setType(MoveableType.STRUCTURE);
	}

	public void updateStructure() {
		for (ArmorStand as : getModel().keySet()) {
			Vector relative = getModel().get(as);
			Vector toBe = relative.clone();
			double pitchRadius = Math.sqrt(toBe.getY() * toBe.getY() + toBe.getZ() * toBe.getZ());
			double pitchAngle = getPitch();
			if (pitchRadius != 0) {
				pitchAngle = Math.asin(toBe.getY() / pitchRadius);
				if (toBe.getZ() < 0) {
					pitchAngle = Math.PI - pitchAngle;
				}
				pitchAngle += getPitch();
				toBe.setY(Math.sin(pitchAngle) * pitchRadius);
				toBe.setZ(Math.cos(pitchAngle) * pitchRadius);
			}
			double yawRadius = Math.sqrt(toBe.getX() * toBe.getX() + toBe.getZ() * toBe.getZ());
			double yawAngle = getYaw();
			if (yawRadius != 0) {
				yawAngle = Math.asin(toBe.getX() / yawRadius);
				if (toBe.getZ() < 0) {
					yawAngle = Math.PI - yawAngle;
				}
				yawAngle += getYaw();
				toBe.setX(Math.sin(yawAngle) * yawRadius);
				toBe.setZ(Math.cos(yawAngle) * yawRadius);
			}
			toBe.add(getOrigin());
			toBe.subtract(as.getLocation().toVector());
			as.setHeadPose(new EulerAngle(-getPitch(), -getYaw(), 0));
			as.setVelocity(toBe);
		}
	}

	@Override
	public void moveTo(Vector vector) {
		vector.subtract(new Vector(0, 1.5, 0));
		Vector newDir = vector.clone().subtract(getOrigin()).normalize();
		setPitch(Math.asin(newDir.getY()));
		/*
		 * if (newDir.getZ() < 0) { setPitch(Math.PI - getPitch()); }
		 */
		setYaw(Math.asin(newDir.getX()));
		if (newDir.getZ() < 0) {
			setYaw(Math.PI - getYaw());
		}
		setOrigin(vector);
	}

	@Override
	public void runTick(boolean started) {
		updateStructure();
	}

	@Override
	public void addChild(Moveable child) {

	}

	public HashMap<ArmorStand, Vector> getModel() {
		return model;
	}

	public void setModel(HashMap<ArmorStand, Vector> model) {
		this.model = model;
	}

	public double getYaw() {
		return yaw;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public double getPitch() {
		return pitch;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}

	public int getTemplateID() {
		return templateID;
	}

	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}

}
