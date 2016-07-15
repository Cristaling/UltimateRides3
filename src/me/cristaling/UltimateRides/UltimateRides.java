package me.cristaling.UltimateRides;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.cristaling.UltimateRides.Utils.FileManager;
import me.cristaling.UltimateRides.managers.LoadManager;
import me.cristaling.UltimateRides.managers.RideManager;
import me.cristaling.UltimateRides.moveables.Ride;

public class UltimateRides extends JavaPlugin {

	public RideManager rideMaster = new RideManager(this);
	public EntityHandler entityMaster = new EntityHandler(this);
	public FileManager fileMaster = new FileManager(this);
	LoadManager loadMaster = new LoadManager(this);

	public static UltimateRides instance;
	
	@Override
	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(entityMaster, this);
		getCommand("uride").setExecutor(new CommandHandler(this));
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				loadMaster.loadRides();
				caruselTimer();
			}
		}, 40);
	}

	public static UltimateRides getAPI(){
		return instance;
	}
	
	@Override
	public void onDisable() {
		if(!rideMaster.rides.isEmpty()){
			loadMaster.saveRides();
		}
	}

	public void caruselTimer() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Ride ride : rideMaster.rides.values()) {
					ride.runTick(true);
				}
			}
		}, 1, 1);
	}

}
