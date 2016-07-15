package me.cristaling.UltimateRides.builders;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.moveables.CustomPath;

public class CustomPathBuilder {

	List<Vector> vectorList = new ArrayList<Vector>();
	Vector origin;

	public CustomPathBuilder(Vector vector) {
		this.origin = vector;
	}

	public void addPoint(Vector vector) {
		vectorList.add(vector.subtract(origin));
	}

	public void removePoint() {
		if (!vectorList.isEmpty()) {
			vectorList.remove(vectorList.size() - 1);
		}
	}

	public CustomPath build() {
		return new CustomPath(origin.clone(), vectorList);
	}

}
