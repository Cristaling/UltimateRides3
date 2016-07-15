package me.cristaling.UltimateRides.moveables;

import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class ArrayMove extends Moveable {

	//List<Moveable> children = new ArrayList<Moveable>();
	int cAngle = 0;
	
	Vector direction = new Vector(1,0,0);
	Vector leftVec;

	private int rows;

	private int cols;
	private double width;

	private double lenght;

	public ArrayMove(Vector origin, int rows, int cols, double width, double lenght) {
		setOrigin(origin);
		this.setRows(rows);
		this.setCols(cols);
		this.setWidth(width);
		this.setLenght(lenght);
		setType(MoveableType.ARRAY);
	}

	@Override
	public void moveTo(Vector vector) {
		Vector newDir = vector.clone().subtract(getOrigin()).setY(0).normalize();
		int offSetAngle1 = (int)Math.round(Math.acos(newDir.getZ()) * 10800 / Math.PI);
		if(newDir.getX() < 0){
			offSetAngle1 = 21600 - offSetAngle1;
		}
		int offSetAngle2 = (int)Math.round(Math.acos(direction.getZ()) * 10800 / Math.PI);
		if(direction.getX() < 0){
			offSetAngle2 = 21600 - offSetAngle2;
		}
		cAngle += offSetAngle1 - offSetAngle2;
		direction = newDir;
		setOrigin(vector);
	}

	public Vector getChildLocation(Moveable child) {
		if (getChildren().contains(child)) {
			int index = getChildren().indexOf(child);
			double row = (double)(index / getCols()) - (((double) getRows()) / 2);
			double col = (double)(index % getCols()) - (((double) getCols()) / 2);
			col *= getWidth();
			row *= getLenght();
			double radius = Math.sqrt(col * col + row * row);
			double circAngle = Math.acos(col / radius);
			circAngle *= 10800;
			circAngle /= Math.PI;
			if(row < 0){
				circAngle = 21600 - circAngle;
			}
			circAngle += cAngle;
			circAngle *= Math.PI;
			circAngle /= 10800;
			return new Vector(getOrigin().getX() + Math.sin(circAngle) * radius, getOrigin().getY(),
					getOrigin().getZ() + Math.cos(circAngle) * radius);
		}
		return null;
	}
	
	@Override
	public void runTick(boolean started) {
		for(Moveable child:getChildren()){
			child.moveTo(getChildLocation(child));
			child.runTick(started);
		}
	}

	@Override
	public void addChild(Moveable child) {
		getChildren().add(child);
	}
	
	public ArrayMove clone(){
		return new ArrayMove(getOrigin().clone(), getRows(), getCols(), getWidth(), getLenght());
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLenght() {
		return lenght;
	}

	public void setLenght(double lenght) {
		this.lenght = lenght;
	}
	
}
