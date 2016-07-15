package me.cristaling.UltimateRides.managers;

import java.util.HashMap;
import org.bukkit.entity.Player;

import me.cristaling.UltimateRides.UltimateRides;
import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.builders.ComplexStructureBuilder;
import me.cristaling.UltimateRides.builders.CustomPathBuilder;
import me.cristaling.UltimateRides.moveables.Ride;

public class RideManager {

	public HashMap<String, Ride> rides = new HashMap<String, Ride>();
	public HashMap<Player, ComplexStructureBuilder> creatingStructure = new HashMap<Player, ComplexStructureBuilder>();
	public HashMap<Player, CustomPathBuilder> creatingPath = new HashMap<Player, CustomPathBuilder>();
	public HashMap<Player, Moveable> selectedElement = new HashMap<Player, Moveable>();
	
	UltimateRides plugin;
	
	public RideManager(UltimateRides plugin) {
		this.plugin = plugin;
	}
	
	/*public void saveRide(Player player){
		if(!creatingRide.containsKey(player)){
			return;
		}
		Ride ride = creatingRide.get(player);
		creatingRide.remove(player);
		rides.put(ride.getName(), ride);
	}*/
	
}
