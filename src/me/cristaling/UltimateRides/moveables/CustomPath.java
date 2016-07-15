package me.cristaling.UltimateRides.moveables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class CustomPath extends Moveable {

	private List<Vector> preVector = new ArrayList<Vector>();
	private HashMap<Moveable, Double> childrenMap = new HashMap<Moveable, Double>();
	Vector[] pathPoints;

	private double speed = 0.25;
	int levelOfDetail = 20;
	boolean stopAtTheEnd = false;

	public CustomPath(Vector vector, List<Vector> vectorList) {
		setOrigin(vector);
		setPreVector(vectorList);
		setType(MoveableType.PATH);
		generatePath();
	}

	public int getPathLenght() {
		return getPreVector().size() * levelOfDetail;
	}

	public Vector getPathPosition(Double double1) {
		double percent = double1 / (getPreVector().size() * levelOfDetail);
		return Interp(pathPoints, percent).add(getOrigin());
	}

	public void generatePath() {
		Vector[] suppliedPath = getPreVector().toArray(new Vector[getPreVector().size()]);
		Vector[] finalPath;
		finalPath = new Vector[suppliedPath.length + 2];
		copyArray(suppliedPath, 0, finalPath, 1, suppliedPath.length);
		finalPath[0] = finalPath[1].clone().add(finalPath[1].clone().subtract(finalPath[2]));
		finalPath[finalPath.length - 1] = finalPath[finalPath.length - 2].clone()
				.add(finalPath[finalPath.length - 2].clone().subtract(finalPath[finalPath.length - 3]));
		// TODO Finish This
		// is this a closed, continuous loop? yes? well then so let's make a
		// continuous Catmull-Rom spline!
		/*
		 * if (vector3s[1] == vector3s[vector3s.length - 2]) {
		 * Bukkit.broadcastMessage("This strange thingy"); Vector[]
		 * tmpLoopSpline = new Vector[vector3s.length]; copyArray(vector3s,
		 * tmpLoopSpline, vector3s.length); tmpLoopSpline[0] =
		 * tmpLoopSpline[tmpLoopSpline.length - 3];
		 * tmpLoopSpline[tmpLoopSpline.length - 1] = tmpLoopSpline[2]; vector3s
		 * = new Vector[tmpLoopSpline.length]; copyArray(tmpLoopSpline,
		 * vector3s, tmpLoopSpline.length); }
		 */
		pathPoints = finalPath;
	}

	private static Vector Interp(Vector[] pts, double t) {
		int numSections = pts.length - 3;
		int currPt = (int) Math.min(Math.floor(t * (double) numSections), numSections - 1);
		double u = t * (double) numSections - (double) currPt;

		Vector a = pts[currPt];
		Vector b = pts[currPt + 1];
		Vector c = pts[currPt + 2];
		Vector d = pts[currPt + 3];

		return a.clone().multiply(-1).add(b.clone().multiply(3f)).subtract(c.clone().multiply(3f)).add(d)
				.multiply(u * u * u)
				.add(a.clone().multiply(2f).subtract(b.clone().multiply(5f)).add(c.clone().multiply(4f)).subtract(d)
						.multiply(u * u))
				.add(a.clone().multiply(-1).add(c).multiply(u)).add(b.clone().multiply(2f)).multiply(0.5f);
	}

	private void copyArray(Vector[] source, int a, Vector[] dest, int b, int lenght) {
		for (int i = a; i < lenght; i++) {
			dest[b + i] = source[i];
		}
	}

	public void addChild(Moveable child) {
		getChildren().add(child);
		getChildrenMap().put(child, 0.0);
	}

	public void addChild(Moveable child, double progress) {
		while (progress > getPathLenght()) {
			progress -= getPathLenght();
		}
		getChildren().add(child);
		getChildrenMap().put(child, progress);
	}

	@Override
	public void moveTo(Vector vector) {
		setOrigin(vector);
	}

	@Override
	public void runTick(boolean started) {
		if (started) {
			for (Moveable child : getChildrenMap().keySet()) {
				double progress = getChildrenMap().get(child) + getSpeed();
				if (progress > getPathLenght()) {
					if (stopAtTheEnd) {
						progress = getPathLenght();
					} else {
						progress -= getPathLenght();
					}
				}
				getChildrenMap().put(child, progress);
			}
		}
		for (Entry<Moveable, Double> child : getChildrenMap().entrySet()) {
			if (started) {
				child.getKey().moveTo(getPathPosition(child.getValue()));
			}
			child.getKey().runTick(started);
		}
	}

	public List<Vector> getPreVector() {
		return preVector;
	}

	public void setPreVector(List<Vector> preVector) {
		this.preVector = preVector;
	}

	public HashMap<Moveable, Double> getChildrenMap() {
		return childrenMap;
	}

	public void setChildrenMap(HashMap<Moveable, Double> childrenMap) {
		this.childrenMap = childrenMap;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
}