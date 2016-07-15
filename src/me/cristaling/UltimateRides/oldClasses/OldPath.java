package me.cristaling.UltimateRides.oldClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class OldPath {

	List<Location> points = new ArrayList<Location>();
	HashMap<Location, Vector> normals = new HashMap<Location, Vector>();
	HashMap<Location, Double> stamps = new HashMap<Location, Double>();
	
	public void generateNormals() {
		if (points.size() < 2) {
			return;
		}
		normals.clear();
		normals.put(points.get(0), points.get(1).toVector().subtract(points.get(0).toVector()).normalize());
		for (int i = 1; i < points.size() - 1; i++) {
			normals.put(points.get(i), points.get(i + 1).toVector().subtract(points.get(i).toVector()).normalize()
					.subtract(points.get(i - 1).toVector().subtract(points.get(i).toVector()).normalize()));
		}
		normals.put(points.get(points.size()-1), points.get(points.size() - 2).toVector().subtract(points.get(points.size() - 1).toVector()).normalize());
	}

	public void generateDistanceStamps() {
		if(points.size() < 2){
			return;
		}
		stamps.clear();
		stamps.put(points.get(0), 0.0);
		for (int i = 0; i < points.size() - 1; i++) {
			points.get(i);
			points.get(i+1);
			
		}
	}

}
