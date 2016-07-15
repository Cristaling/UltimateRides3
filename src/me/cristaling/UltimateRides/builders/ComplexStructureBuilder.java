package me.cristaling.UltimateRides.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.ComplexStructureTemplate;
import me.cristaling.UltimateRides.moveables.ComplexStructureMove;

public class ComplexStructureBuilder {

	public static List<ComplexStructureTemplate> templates = new ArrayList<ComplexStructureTemplate>();
	
	Location origin = null;
	Location point1 = null;
	Location point2 = null;
	
	public void setOrigin(Location location){
		origin = location;
	}
	
	public void setPoint1(Location location){
		point1 = location;
	}
	
	public void setPoint2(Location location){
		point2 = location;
	}
	
	public int build(){
		if(origin != null && point1 != null && point2 != null){
			templates.add(ComplexStructureBuilder.createComplexStructureTemplate(origin, point1, point2));
			return templates.size() - 1;
		}
		return -1;
	}
	
	public static ComplexStructureTemplate getTemplate(int ID){
		if(ID < 0 || ID >= templates.size()){
			return null;
		}
		return templates.get(ID);
	}
	
	@SuppressWarnings("deprecation")
	public static ComplexStructureMove spawnComplexStructure(Location position, ComplexStructureTemplate template){
		ComplexStructureMove result = new ComplexStructureMove(templates.indexOf(template), position.toVector());
		for(Entry<Vector, String> entry:template.data.entrySet()){
			String info[] = entry.getValue().split("-");
			ArmorStand as = (ArmorStand) position.getWorld().spawnEntity(position.clone().add(entry.getKey()), EntityType.ARMOR_STAND);
			as.setVisible(false);
			ItemStack head = new ItemStack(Material.getMaterial(info[0]), 1, (short)0, Byte.parseByte(info[1]));
			as.setHelmet(head);
			result.getModel().put(as, entry.getKey());
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static ComplexStructureTemplate createComplexStructureTemplate(Location origin, Location point1, Location point2){
		ComplexStructureTemplate result = new ComplexStructureTemplate();
		for(int x = Math.min(point1.getBlockX(), point2.getBlockX()); x<=Math.max(point1.getBlockX(), point2.getBlockX());x++){
			for(int y = Math.min(point1.getBlockY(), point2.getBlockY()); y<=Math.max(point1.getBlockY(), point2.getBlockY());y++){
				for(int z = Math.min(point1.getBlockZ(), point2.getBlockZ()); z<=Math.max(point1.getBlockZ(), point2.getBlockZ());z++){
					Block cube = point1.getWorld().getBlockAt(x, y, z);
					if(!cube.getType().equals(Material.AIR)){
						String info = cube.getType().name() + "-" + String.valueOf(cube.getData());
						Bukkit.broadcastMessage(info);
						result.data.put((new Vector(x,y,z)).subtract(origin.toVector()).multiply(0.625), info);
					}
				}
			}
		}
		return result;
	}
	
}
