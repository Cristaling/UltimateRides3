package me.cristaling.UltimateRides.oldClasses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Carousel {

	public int bigRadius;
	public int smallRadius;
	public int maxSpeed;
	int circleNumber;
	int cartPerCircle;

	public String name;

	double speed = 0;
	private double cAngle = 0;
	public double speedIncrement = 0.5;
	public double originalIncrement = 0.5;
	public boolean accExponent = false;

	public Location pivot;
	public List<Entity> Carts = new ArrayList<Entity>();
	boolean started = false;

	public Carousel(String n, Location l, int c1, int c2, int r1, int r2, int ms) {
		this.name = n;
		this.pivot = l;
		this.circleNumber = c1;
		this.cartPerCircle = c2;
		this.bigRadius = r1;
		this.smallRadius = r2;
		this.maxSpeed = ms;
	}

	public Carousel(String n, Location l, int c1, int c2, int r1, int r2, int ms, boolean ok) {
		this.name = n;
		this.pivot = l;
		this.circleNumber = c1;
		this.cartPerCircle = c2;
		this.bigRadius = r1;
		this.smallRadius = r2;
		this.maxSpeed = ms;
		this.accExponent = ok;
	}

	public String getName() {
		return name;
	}

	public int getBigRadius() {
		return bigRadius;
	}

	public int getSmallRadius() {
		return smallRadius;
	}

	public Location getPivot() {
		return pivot;
	}

	public void Start() {
		started = true;
	}

	public void Stop() {
		started = false;
	}

	void doTick() {
		if (started && speed < maxSpeed) {
			speed += speedIncrement;
			if (accExponent) {
				speedIncrement += originalIncrement;
			}
		}
		if (!started && speed > 0) {
			speed -= speedIncrement;
			if (accExponent) {
				speedIncrement += originalIncrement;
			}
		}
		if (speed < 0) {
			speed = 0;
		}
		if (speed != 0) {
			cAngle += speed;
			if (cAngle >= 21600) {
				cAngle -= 21600;
			}
		}
		if (speed == 0 || speed >= maxSpeed) {
			speedIncrement = originalIncrement;
		}
	}

	Location getCartLoc(Entity cart) {
		if (Carts.contains(cart)) {
			if (circleNumber == 1) {
				int index = Carts.indexOf(cart);
				double circAngle = index * 21600 / cartPerCircle;
				circAngle += cAngle;
				double radAngle = circAngle * Math.PI / 10800;
				Location secPiv = new Location(cart.getWorld(), 0, pivot.getY(), 0);
				secPiv.setX(pivot.getX() + Math.sin(radAngle) * bigRadius);
				secPiv.setZ(pivot.getZ() + Math.cos(radAngle) * bigRadius);
				return secPiv;
			} else {
				int index = Carts.indexOf(cart);
				int circle = index / cartPerCircle;
				int pos = index % cartPerCircle;
				double circAngle = circle * 21600 / circleNumber;
				circAngle += cAngle;
				double radAngle = circAngle * Math.PI / 10800;
				Location secPiv = new Location(cart.getWorld(), 0, pivot.getY(), 0);
				secPiv.setX(pivot.getX() + Math.sin(radAngle) * bigRadius);
				secPiv.setZ(pivot.getZ() + Math.cos(radAngle) * bigRadius);
				circAngle = pos * 21600 / cartPerCircle;
				circAngle += 21600 - cAngle;
				radAngle = circAngle * Math.PI / 10800;
				Location post = new Location(cart.getWorld(), 0, secPiv.getY(), 0);
				post.setX(secPiv.getX() + Math.sin(radAngle) * smallRadius);
				post.setZ(secPiv.getZ() + Math.cos(radAngle) * smallRadius);
				return post;
			}
		}
		return null;
	}

}
