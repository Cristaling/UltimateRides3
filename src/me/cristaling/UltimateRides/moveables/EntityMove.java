package me.cristaling.UltimateRides.moveables;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;

public class EntityMove extends Moveable{

	private Entity entity;
	
	public EntityMove(Vector origin, Entity entity) {
		setOrigin(origin);
		this.setEntity(entity);
		setType(MoveableType.ENTITY);
	}
	
	@Override
	public void moveTo(Vector vector){
		setOrigin(vector);
	}

	@Override
	public void runTick(boolean started) {
		getEntity().setVelocity(getOrigin().clone().subtract(getEntity().getLocation().toVector()));
	}

	@Override
	public void addChild(Moveable child) {
		
	}
	
	public EntityMove clone(){
		return new EntityMove(getOrigin().clone(), getEntity().getWorld().spawnEntity(getEntity().getLocation(), getEntity().getType()));
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
}
