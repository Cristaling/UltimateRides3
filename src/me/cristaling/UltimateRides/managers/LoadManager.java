package me.cristaling.UltimateRides.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import me.cristaling.UltimateRides.Utils.ComplexStructureTemplate;
import me.cristaling.UltimateRides.Utils.FileManager.Config;
import me.cristaling.UltimateRides.UltimateRides;
import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;
import me.cristaling.UltimateRides.Utils.Serializer;
import me.cristaling.UltimateRides.builders.ComplexStructureBuilder;
import me.cristaling.UltimateRides.moveables.ArrayMove;
import me.cristaling.UltimateRides.moveables.ComplexStructureMove;
import me.cristaling.UltimateRides.moveables.CustomPath;
import me.cristaling.UltimateRides.moveables.EmptyMoveable;
import me.cristaling.UltimateRides.moveables.EntityMove;
import me.cristaling.UltimateRides.moveables.Ride;
import me.cristaling.UltimateRides.moveables.RotorMove;

public class LoadManager {

	UltimateRides plugin;
	
	public LoadManager(UltimateRides plugin) {
		this.plugin = plugin;
	}
	
	public void saveRides(){
		saveStructureTemplates();
		List<String> points = new ArrayList<String>();
		for(String name:plugin.rideMaster.rides.keySet()){
			points.add(name);
		}
		plugin.fileMaster.getConfig("Rides.yml").set("Rides", points);
		plugin.fileMaster.getConfig("Rides.yml").set("TemplateNumber", ComplexStructureBuilder.templates.size());
		plugin.fileMaster.saveConfig("Rides.yml");
		for(Ride ride:plugin.rideMaster.rides.values()){
			ride.stop();
			Config saveFile = plugin.fileMaster.getConfig("Rides/"+ride.getName()+".yml");
			saveFile.set("World", ride.getWorld().getName());
			saveFile.set("Origin", Serializer.vectorToString(ride.getOrigin()));
			saveFile.set("ChildrenNumber", ride.getChildren().size());
			for(Moveable child:ride.getChildren()){
				saveElement(saveFile, "Children."+String.valueOf(ride.getChildren().indexOf(child)), child);
			}
			saveFile.save();
		}
	}
	
	public void saveStructureTemplates(){
		for(ComplexStructureTemplate template:ComplexStructureBuilder.templates){
			Config saveFile = plugin.fileMaster.getConfig("StructureTemplates/"+ComplexStructureBuilder.templates.indexOf(template)+".yml");
			saveFile.set("BlockNumber", template.data.size());
			int i = 0;
			for(Entry<Vector, String> entry:template.data.entrySet()){
				saveFile.set("Block."+i+".Vector", Serializer.vectorToString(entry.getKey()));
				saveFile.set("Block."+i+".Info", entry.getValue());
				i++;
			}
			saveFile.save();
		}
	}
	
	public void loadStructureTemplates(int nr){
		for(int i=0;i<nr;i++){
			Config loadFile = plugin.fileMaster.getConfig("StructureTemplates/"+i+".yml");
			ComplexStructureTemplate template = new ComplexStructureTemplate();
			int n = loadFile.get().getInt("BlockNumber");
			for(int j=0;j<n;j++){
				Vector key = Serializer.stringToVector(loadFile.get().getString("Block."+j+".Vector"));
				String info = loadFile.get().getString("Block."+j+".Info");
				template.data.put(key, info);
			}
			ComplexStructureBuilder.templates.add(template);
		}
	}
	
	public void saveElement(Config saveFile, String masterPath, Moveable element){
		if(element.getType().equals(MoveableType.STRUCTURE)){
			ComplexStructureMove mover = (ComplexStructureMove) element;
			saveFile.set(masterPath + ".MoveType", "structure");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
			saveFile.set(masterPath + ".Yaw", mover.getYaw());
			saveFile.set(masterPath + ".Pitch", mover.getPitch());
			saveFile.set(masterPath + ".TemplateID", mover.getTemplateID());
			for(ArmorStand ar:mover.getModel().keySet()){
				ar.remove();
			}
		}
		if(element.getType().equals(MoveableType.ENTITY)){
			EntityMove mover = (EntityMove) element;
			saveFile.set(masterPath + ".MoveType", "entity");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
			saveFile.set(masterPath + ".Type", mover.getEntity().getType().toString());
			plugin.entityMaster.kill(mover.getEntity());
		}
		if(element.getType().equals(MoveableType.ARRAY)){
			ArrayMove mover = (ArrayMove) element;
			saveFile.set(masterPath + ".MoveType", "array");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
			saveFile.set(masterPath + ".Rows", mover.getRows());
			saveFile.set(masterPath + ".Cols", mover.getCols());
			saveFile.set(masterPath + ".Width", mover.getWidth());
			saveFile.set(masterPath + ".Lenght", mover.getLenght());
		}
		if(element.getType().equals(MoveableType.ROTOR)){
			RotorMove mover = (RotorMove) element;
			saveFile.set(masterPath + ".MoveType", "rotor");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
			saveFile.set(masterPath + ".Radius", mover.getRadius());
			saveFile.set(masterPath + ".Speed", mover.getSpeed());
			saveFile.set(masterPath + ".Angle", mover.getAngle());
			saveFile.set(masterPath + ".ClockWise", mover.isClockWise());
		}
		if(element.getType().equals(MoveableType.EMPTY)){
			EmptyMoveable mover = (EmptyMoveable) element;
			saveFile.set(masterPath + ".MoveType", "empty");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
		}
		if(element.getType().equals(MoveableType.PATH)){
			CustomPath mover = (CustomPath) element;
			saveFile.set(masterPath + ".MoveType", "path");
			saveFile.set(masterPath + ".Origin", Serializer.vectorToString(mover.getOrigin()));
			saveFile.set(masterPath + ".Speed", mover.getSpeed());
			List<String> points = new ArrayList<String>();
			for(Vector vector:mover.getPreVector()){
				points.add(Serializer.vectorToString(vector));
			}
			saveFile.set(masterPath + ".Points", points);
			for(Moveable child:element.getChildren()){
				saveFile.set(masterPath + ".ChildrenProgress." + String.valueOf(element.getChildren().indexOf(child)), mover.getChildrenMap().get(child));
			}
		}
		saveFile.set(masterPath + ".ChildrenNumber", element.getChildren().size());
		for(Moveable child:element.getChildren()){
			saveElement(saveFile, masterPath + ".Children." + String.valueOf(element.getChildren().indexOf(child)), child);
		}
	}
	
	public void loadRides(){
		int nr = plugin.fileMaster.getConfig("Rides.yml").get().getInt("TemplateNumber");
		loadStructureTemplates(nr);
		List<String> points = plugin.fileMaster.getConfig("Rides.yml").get().getStringList("Rides");
		for(String rideName:points){
			Config loadFile = plugin.fileMaster.getConfig("Rides/"+rideName+".yml");
			World world = Bukkit.getWorld(loadFile.get().getString("World"));
			Ride ride = new Ride(rideName, Serializer.stringToVector(loadFile.get().getString("Origin")), world);
			int childrenNumber = loadFile.get().getInt("ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				ride.addChild(loadElement(loadFile, "Children."+String.valueOf(i), world));
			}
			plugin.rideMaster.rides.put(ride.getName(), ride);
		}
	}
	
	public Moveable loadElement(Config loadFile, String masterPath, World world){
		String moveType = loadFile.get().getString(masterPath + ".MoveType");
		if(moveType.equalsIgnoreCase(MoveableType.STRUCTURE.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			int templateID = loadFile.get().getInt(masterPath + ".TemplateID");
			ComplexStructureMove mover = ComplexStructureBuilder.spawnComplexStructure(origin.toLocation(world), ComplexStructureBuilder.getTemplate(templateID));
			mover.setYaw(loadFile.get().getDouble(masterPath + ".Yaw"));
			mover.setPitch(loadFile.get().getDouble(masterPath + ".Pitch"));
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world));
			}
			return mover;
		}
		if(moveType.equalsIgnoreCase(MoveableType.ENTITY.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			EntityType type = EntityType.valueOf(loadFile.get().getString(masterPath + ".Type"));
			Entity entity = world.spawnEntity(origin.toLocation(world), type);
			plugin.entityMaster.registerEntity(entity);
			EntityMove mover = new EntityMove(origin, entity);
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world));
			}
			return mover;
		}
		if(moveType.equalsIgnoreCase(MoveableType.ROTOR.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			int radius = loadFile.get().getInt(masterPath + ".Radius");
			int angle = loadFile.get().getInt(masterPath + ".Angle");
			int speed = loadFile.get().getInt(masterPath + ".Speed");
			boolean clockWise = loadFile.get().getBoolean(masterPath + ".ClockWise");
			RotorMove mover = new RotorMove(origin, radius, clockWise);
			mover.setAngle(angle);
			mover.setSpeed(speed);
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world));
			}
			return mover;
		}
		if(moveType.equalsIgnoreCase(MoveableType.ARRAY.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			int rows = loadFile.get().getInt(masterPath + ".Rows");
			int cols = loadFile.get().getInt(masterPath + ".Cols");
			double width = loadFile.get().getDouble(masterPath + ".Width");
			double lenght = loadFile.get().getDouble(masterPath + ".Lenght");
			ArrayMove mover = new ArrayMove(origin, rows, cols, width, lenght);
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world));
			}
			return mover;
		}
		if(moveType.equalsIgnoreCase(MoveableType.PATH.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			List<String> points = loadFile.get().getStringList(masterPath + ".Points");
			double speed = loadFile.get().getDouble(masterPath + ".Speed");
			List<Vector> preVector = new ArrayList<Vector>();
			for(String vec:points){
				preVector.add(Serializer.stringToVector(vec));
			}
			CustomPath mover = new CustomPath(origin, preVector);
			mover.setSpeed(speed);
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world), loadFile.get().getDouble(masterPath + ".ChildrenProgress." + String.valueOf(i)));
			}
			return mover;
		}
		if(moveType.equalsIgnoreCase(MoveableType.EMPTY.toString())){
			Vector origin = Serializer.stringToVector(loadFile.get().getString(masterPath + ".Origin"));
			EmptyMoveable mover = new EmptyMoveable(origin);
			int childrenNumber = loadFile.get().getInt(masterPath + ".ChildrenNumber");
			for(int i=0;i<childrenNumber;i++){
				mover.addChild(loadElement(loadFile, masterPath + ".Children."+String.valueOf(i), world));
			}
			return mover;
		}
		return null;
	}
	
}
