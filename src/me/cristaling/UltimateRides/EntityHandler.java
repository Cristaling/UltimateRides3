package me.cristaling.UltimateRides;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;
import me.cristaling.UltimateRides.moveables.ComplexStructureMove;
import me.cristaling.UltimateRides.moveables.EntityMove;

public class EntityHandler implements Listener {

	UltimateRides plugin;

	List<Entity> registered = new ArrayList<Entity>();
	List<Entity> stopExit = new ArrayList<Entity>();

	public EntityHandler(UltimateRides plugin) {
		this.plugin = plugin;
	}

	public void registerEntity(Entity entity) {
		registered.add(entity);
	}

	public void clearRegistered() {
		for (Entity entity : registered) {
			entity.remove();
		}
	}

	@EventHandler
	public void onMinecartCollision(VehicleEntityCollisionEvent event) {
		Entity vehicle = event.getVehicle();
		if(registered.contains(vehicle)){
				event.setCancelled(true);
				event.setCollisionCancelled(true);
		}
	}

	@EventHandler
	public void onDmg(EntityDamageByEntityEvent event) {
		if(registered.contains(event.getEntity())){
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onVeh(VehicleDestroyEvent event) {
		Entity vehicle = event.getVehicle();
		if(registered.contains(vehicle)){
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onVeh(VehicleExitEvent event) {
		Entity vehicle = event.getVehicle();
		if(stopExit.contains(vehicle)){
				event.setCancelled(true);
		}
	}

	public void kill(Entity entity) {
		if(registered.contains(entity)){
			registered.remove(entity);
		}
		entity.remove();
	}

	public void setExit(Moveable ride, boolean value) {
		if(ride.getType().equals(MoveableType.ENTITY)){
			EntityMove mover = (EntityMove) ride;
			if(value && !stopExit.contains(mover.getEntity())){
				stopExit.add(mover.getEntity());
			}
			if(!value && stopExit.contains(mover.getEntity())){
				stopExit.remove(mover.getEntity());
			}
		}
		for(Moveable child:ride.getChildren()){
			setExit(child, value);
		}
	}
	
	public void killRide(Moveable ride) {
		if(ride.getType().equals(MoveableType.STRUCTURE)){
			ComplexStructureMove mover = (ComplexStructureMove) ride;
			for(ArmorStand ar:mover.getModel().keySet()){
				ar.remove();
			}
		}
		if(ride.getType().equals(MoveableType.ENTITY)){
			EntityMove mover = (EntityMove) ride;
			registered.remove(mover.getEntity());
			mover.getEntity().remove();
		}
		for(Moveable child:ride.getChildren()){
			killRide(child);
		}
	}

}
