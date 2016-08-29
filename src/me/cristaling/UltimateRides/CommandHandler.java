package me.cristaling.UltimateRides;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.cristaling.UltimateRides.Utils.Moveable;
import me.cristaling.UltimateRides.Utils.MoveableType;
import me.cristaling.UltimateRides.builders.ComplexStructureBuilder;
import me.cristaling.UltimateRides.builders.CustomPathBuilder;
import me.cristaling.UltimateRides.moveables.ArrayMove;
import me.cristaling.UltimateRides.moveables.CustomPath;
import me.cristaling.UltimateRides.moveables.EmptyMoveable;
import me.cristaling.UltimateRides.moveables.EntityMove;
import me.cristaling.UltimateRides.moveables.Ride;
import me.cristaling.UltimateRides.moveables.RotorMove;

public class CommandHandler implements CommandExecutor {

	UltimateRides plugin;

	public CommandHandler(UltimateRides plugin) {
		this.plugin = plugin;
	}

	List<ArmorStand> showList = new ArrayList<ArmorStand>();

	@Override
	public boolean onCommand(CommandSender senderOfCommand, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("uride")) {
			if (senderOfCommand instanceof Player) {

				Player send = (Player) senderOfCommand;

				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("about")) {
						send.sendMessage(ChatColor.DARK_GREEN + "Plugin developed by cristaling");
						send.sendMessage(ChatColor.DARK_GREEN + "Licence For One Server Only");
						send.sendMessage(ChatColor.DARK_GREEN + "Skype - " + ChatColor.GOLD + "cristaling1");
						return true;
					}
					if (args[0].equalsIgnoreCase("ride")) {
						if (args.length == 2 && args[1].equalsIgnoreCase("list")) {
							if (!send.hasPermission("uride.list")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (plugin.rideMaster.rides.isEmpty()) {
								send.sendMessage(ChatColor.RED + "There's no ride to list");
								return true;
							}
							send.sendMessage(ChatColor.DARK_GREEN + "Rides:");
							for (String name : plugin.rideMaster.rides.keySet()) {
								send.sendMessage(ChatColor.GOLD + name);
							}
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("create")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There's already a ride with the given name");
								return true;
							}
							Ride ride = new Ride(args[2], send.getLocation().toVector(), send.getLocation().getWorld());
							plugin.rideMaster.rides.put(args[2], ride);
							plugin.rideMaster.selectedElement.put(send, ride);
							// plugin.rideMaster.creatingRide.put(send, ride);
							send.sendMessage(ChatColor.GOLD + "Ride created successfuly");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("show")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There is no ride with the given name");
								return true;
							}
							Ride ride = plugin.rideMaster.rides.get(args[2]);
							send.sendMessage(ChatColor.GOLD + "Showing ride" + ChatColor.DARK_GREEN + " " + args[2]);
							displayElementInChat(send, ride, 0, 0);
							// plugin.rideMaster.creatingRide.put(send, ride);
							send.sendMessage(ChatColor.GOLD + "Ride displayed successfuly");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("start")) {
							if (!send.hasPermission("uride.start")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There is no ride with the given name");
								return true;
							}
							plugin.rideMaster.rides.get(args[2]).start();
							send.sendMessage(ChatColor.GOLD + "Ride started successfuly");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("stop")) {
							if (!send.hasPermission("uride.stop")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There is no ride with the given name");
								return true;
							}
							plugin.rideMaster.rides.get(args[2]).stop();
							send.sendMessage(ChatColor.GOLD + "Ride stopped successfuly");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("delete")) {
							if (!send.hasPermission("uride.delete")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There is no ride with the given name");
								return true;
							}
							plugin.entityMaster.killRide(plugin.rideMaster.rides.get(args[2]));
							plugin.rideMaster.rides.remove(args[2]);
							send.sendMessage(ChatColor.GOLD + "Ride deleted successfuly");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("path")) {
						if (args.length == 2 && args[1].equalsIgnoreCase("create")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (plugin.rideMaster.creatingPath.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are already creating a path");
								return true;
							}
							plugin.rideMaster.creatingPath.put(send,
									new CustomPathBuilder(send.getLocation().toVector()));
							send.sendMessage(ChatColor.GOLD + "Path creation started successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("add")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingPath.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a path");
								return true;
							}
							plugin.rideMaster.creatingPath.get(send).addPoint(send.getLocation().toVector());
							send.sendMessage(ChatColor.GOLD + "Point added successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("remove")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingPath.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a path");
								return true;
							}
							plugin.rideMaster.creatingPath.get(send).removePoint();
							send.sendMessage(ChatColor.GOLD + "Point removed successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("abort")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingPath.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a path");
								return true;
							}
							plugin.rideMaster.creatingPath.remove(send);
							send.sendMessage(ChatColor.GOLD + "Path creation aborted successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("build")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingPath.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a path");
								return true;
							}
							if (!plugin.rideMaster.selectedElement.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are don't have an element selected");
								return true;
							}
							plugin.rideMaster.selectedElement.get(send)
									.addChild(plugin.rideMaster.creatingPath.get(send).build());
							// plugin.rideMaster.creatingPath.remove(send);
							send.sendMessage(ChatColor.GOLD + "Path created successfuly");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("structure")) {
						if (args.length == 2 && args[1].equalsIgnoreCase("create")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are already creating a structure");
								return true;
							}
							plugin.rideMaster.creatingStructure.put(send, new ComplexStructureBuilder());
							send.sendMessage(ChatColor.GOLD + "Structure creation started successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("point1")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a structure");
								return true;
							}
							plugin.rideMaster.creatingStructure.get(send).setPoint1(send.getLocation());
							send.sendMessage(ChatColor.GOLD + "Point set successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("point2")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a structure");
								return true;
							}
							plugin.rideMaster.creatingStructure.get(send).setPoint2(send.getLocation());
							send.sendMessage(ChatColor.GOLD + "Point set successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("origin")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a structure");
								return true;
							}
							plugin.rideMaster.creatingStructure.get(send).setOrigin(send.getLocation());
							send.sendMessage(ChatColor.GOLD + "Origin set successfuly");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("build")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a structure");
								return true;
							}
							int result = plugin.rideMaster.creatingStructure.get(send).build();
							if (result == -1) {
								send.sendMessage(ChatColor.RED + "Structure build failed");
							} else {
								send.sendMessage(ChatColor.GOLD + "Structure built successfuly with ID " + result);
							}
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("abort")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.creatingStructure.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are not creating a structure");
								return true;
							}
							plugin.rideMaster.creatingStructure.remove(send);
							send.sendMessage(ChatColor.GOLD + "Structure creation aborted successfuly");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("element")) {
						if (args.length == 3 && args[1].equalsIgnoreCase("select")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								send.sendMessage(ChatColor.RED + "There is no ride with the given name");
								return true;
							}
							plugin.rideMaster.selectedElement.put(send, plugin.rideMaster.rides.get(args[2]));
							send.sendMessage(ChatColor.GOLD + "Ride successfuly selected");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("exit")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.selectedElement.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are don't have an element selected");
								return true;
							}
							try {
								boolean a = Boolean.parseBoolean(args[2]);
								plugin.entityMaster.setExit(plugin.rideMaster.selectedElement.get(send), a);
							} catch (Exception e) {
								send.sendMessage(ChatColor.RED + "Expecting a boolean");
								return true;
							}
							send.sendMessage(ChatColor.GOLD + "Exit successfuly set");
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("speed")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.selectedElement.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are don't have an element selected");
								return true;
							}
							if (plugin.rideMaster.selectedElement.get(send) instanceof CustomPath) {
								try {
									double a = Double.parseDouble(args[2]);
									CustomPath path = (CustomPath) plugin.rideMaster.selectedElement.get(send);
									path.setSpeed(a);
									send.sendMessage(ChatColor.GOLD + "Speed succesfuly changed");
									return true;
								} catch (Exception e) {
									send.sendMessage(ChatColor.RED + "The speed should be a double ex: 1.25");
									return true;
								}
							}
							if (plugin.rideMaster.selectedElement.get(send) instanceof RotorMove) {
								try {
									int a = Integer.parseInt(args[3]);
									RotorMove rotor = (RotorMove) plugin.rideMaster.selectedElement.get(send);
									rotor.setSpeed(a);
									send.sendMessage(ChatColor.GOLD + "Speed succesfuly changed");
									return true;
								} catch (Exception e) {
									send.sendMessage(
											ChatColor.RED + "The speed should be an integer (angle minutes) ex: 120");
									return true;
								}
							}
							send.sendMessage(ChatColor.RED + "This element doesn't support speed");
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("show")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							if (!plugin.rideMaster.selectedElement.containsKey(send)) {
								send.sendMessage(ChatColor.RED + "You are don't have an element selected");
								return true;
							}
							if (!(plugin.rideMaster.selectedElement.get(send) instanceof CustomPath)) {
								send.sendMessage(ChatColor.RED + "You are don't have a path selected");
								return true;
							}
							send.sendMessage(ChatColor.DARK_GREEN + "Showing Path");
							CustomPath path = (CustomPath) plugin.rideMaster.selectedElement.get(send);
							for (int i = 0; i <= path.getPathLenght(); i++) {
								ArmorStand entity = (ArmorStand) send.getWorld().spawnEntity(
										path.getPathPosition((double) i).toLocation(send.getWorld()),
										EntityType.ARMOR_STAND);
								entity.setGravity(false);
								showList.add(entity);
							}
							return true;
						}
						if (args.length == 2 && args[1].equalsIgnoreCase("unshow")) {
							if (!send.hasPermission("uride.create")) {
								send.sendMessage(ChatColor.RED + "You don't have permission to do that");
								return true;
							}
							send.sendMessage(ChatColor.DARK_GREEN + "Unshowing Path");
							for (ArmorStand ar : showList) {
								ar.remove();
							}
							showList.clear();
							return true;
						}
						if (args.length >= 2 && args[1].equalsIgnoreCase("child")) {
							if (args.length == 3 && args[2].equalsIgnoreCase("list")) {
								if (!send.hasPermission("uride.create")) {
									send.sendMessage(ChatColor.RED + "You don't have permission to do that");
									return true;
								}
								if (!plugin.rideMaster.selectedElement.containsKey(send)) {
									send.sendMessage(ChatColor.RED + "You are don't have an element selected");
									return true;
								}
								send.sendMessage(ChatColor.DARK_GREEN + "Selected Element's Children:");
								List<Moveable> children = plugin.rideMaster.selectedElement.get(send).getChildren();
								for (Moveable child : children) {
									send.sendMessage(ChatColor.GOLD + "" + children.indexOf(child) + " - "
											+ child.getType().toString());
								}
								return true;
							}
							if (args.length == 4 && args[2].equalsIgnoreCase("select")) {
								if (!send.hasPermission("uride.create")) {
									send.sendMessage(ChatColor.RED + "You don't have permission to do that");
									return true;
								}
								if (!plugin.rideMaster.selectedElement.containsKey(send)) {
									send.sendMessage(ChatColor.RED + "You are don't have an element selected");
									return true;
								}
								try {
									int ID = Integer.parseInt(args[3]);
									List<Moveable> children = plugin.rideMaster.selectedElement.get(send).getChildren();
									if (ID >= children.size() || ID < 0) {
										send.sendMessage(ChatColor.RED + "There is no child with the given ID");
										return true;
									}
									plugin.rideMaster.selectedElement.put(send, children.get(ID));
									send.sendMessage(ChatColor.GOLD + "Child selected successfuly");
									return true;
								} catch (Exception e) {
									send.sendMessage(ChatColor.RED + "Invalid Command");
									return true;
								}
							}
							if (args.length == 4 && args[2].equalsIgnoreCase("remove")) {
								if (!send.hasPermission("uride.create")) {
									send.sendMessage(ChatColor.RED + "You don't have permission to do that");
									return true;
								}
								if (!plugin.rideMaster.selectedElement.containsKey(send)) {
									send.sendMessage(ChatColor.RED + "You are don't have an element selected");
									return true;
								}
								try {
									int ID = Integer.parseInt(args[3]);
									List<Moveable> children = plugin.rideMaster.selectedElement.get(send).getChildren();
									if (ID >= children.size() || ID < 0) {
										send.sendMessage(ChatColor.RED + "There is no child with the given ID");
										return true;
									}
									Moveable mover = children.get(ID);
									plugin.rideMaster.selectedElement.get(send).getChildren().remove(mover);
									plugin.entityMaster.killRide(mover);
									send.sendMessage(ChatColor.GOLD + "Child removed successfuly");
									return true;
								} catch (Exception e) {
									send.sendMessage(ChatColor.RED + "Invalid Command");
									return true;
								}
							}
							if (args.length >= 4 && args[2].equalsIgnoreCase("add")) {
								if (!send.hasPermission("uride.create")) {
									send.sendMessage(ChatColor.RED + "You don't have permission to do that");
									return true;
								}
								if (!plugin.rideMaster.selectedElement.containsKey(send)) {
									send.sendMessage(ChatColor.RED + "You are don't have an element selected");
									return true;
								}
								if (args.length == 5 && args[3].equalsIgnoreCase(MoveableType.STRUCTURE.toString())) {
									try {
										int a = Integer.parseInt(args[4]);
										Moveable parent = plugin.rideMaster.selectedElement.get(send);
										parent.addChild(ComplexStructureBuilder.spawnComplexStructure(
												parent.getOrigin().toLocation(send.getWorld()),
												ComplexStructureBuilder.getTemplate(a)));
										send.sendMessage(ChatColor.GOLD + "Structure added successfuly");
										return true;
									} catch (Exception e) {
										// e.printStackTrace();
										send.sendMessage(ChatColor.RED + "Invalid Command");
										return true;
									}
								}
								if (args.length == 6 && args[3].equalsIgnoreCase(MoveableType.ROTOR.toString())) {
									try {
										int a = Integer.parseInt(args[4]);
										boolean b = Boolean.parseBoolean(args[5]);
										Moveable parent = plugin.rideMaster.selectedElement.get(send);
										parent.addChild(new RotorMove(parent.getOrigin(), a, b));
										send.sendMessage(ChatColor.GOLD + "Rotor added successfuly");
										return true;
									} catch (Exception e) {
										// e.printStackTrace();
										send.sendMessage(ChatColor.RED + "Invalid Command");
										return true;
									}
								}
								if (args.length == 5 && args[3].equalsIgnoreCase(MoveableType.ENTITY.toString())) {
									try {
										EntityType type = EntityType.valueOf(args[4]);
										Moveable parent = plugin.rideMaster.selectedElement.get(send);
										Entity entity = send.getWorld()
												.spawnEntity(parent.getOrigin().toLocation(send.getWorld()), type);
										plugin.entityMaster.registerEntity(entity);
										parent.addChild(new EntityMove(parent.getOrigin(), entity));
										send.sendMessage(ChatColor.GOLD + "Entity added successfuly");
										return true;
									} catch (Exception e) {
										// e.printStackTrace();
										send.sendMessage(ChatColor.RED + "Invalid Command");
										return true;
									}
								}
								if (args.length == 8 && args[3].equalsIgnoreCase(MoveableType.ARRAY.toString())) {
									try {
										int a = Integer.parseInt(args[4]);
										int b = Integer.parseInt(args[5]);
										double c = Double.parseDouble(args[6]);
										double d = Double.parseDouble(args[7]);
										Moveable parent = plugin.rideMaster.selectedElement.get(send);
										parent.addChild(new ArrayMove(parent.getOrigin(), a, b, c, d));
										send.sendMessage(ChatColor.GOLD + "Array added successfuly");
										return true;
									} catch (Exception e) {
										// e.printStackTrace();
										send.sendMessage(ChatColor.RED + "Invalid Command");
										return true;
									}
								}
								if (args.length == 4 && args[3].equalsIgnoreCase(MoveableType.EMPTY.toString())) {
									try {
										Moveable parent = plugin.rideMaster.selectedElement.get(send);
										parent.addChild(new EmptyMoveable(parent.getOrigin()));
										send.sendMessage(ChatColor.GOLD + "EmptyMoveable added successfuly");
										return true;
									} catch (Exception e) {
										send.sendMessage(ChatColor.RED + "Invalid Command");
										return true;
									}
								}
							}
						}
					}
				}
				if (send.hasPermission("uride.start")) {
					send.sendMessage(ChatColor.GOLD + "/uride ride start <Name>");
				}
				if (send.hasPermission("uride.stop")) {
					send.sendMessage(ChatColor.GOLD + "/uride ride stop <Name>");
				}
				if (send.hasPermission("uride.list")) {
					send.sendMessage(ChatColor.GOLD + "/uride ride list");
				}
				send.sendMessage(ChatColor.GOLD + "/uride about");
				if (send.hasPermission("uride.create")) {
					send.sendMessage(ChatColor.GOLD + "/uride ride create <Name>");
					send.sendMessage(ChatColor.GOLD + "/uride ride delete <Name>");
					send.sendMessage(ChatColor.GOLD + "/uride ride show <Name>");
					send.sendMessage(ChatColor.GOLD + "/uride path create");
					send.sendMessage(ChatColor.GOLD + "/uride path add");
					send.sendMessage(ChatColor.GOLD + "/uride path remove");
					send.sendMessage(ChatColor.GOLD + "/uride path build");
					send.sendMessage(ChatColor.GOLD + "/uride path abort");
					send.sendMessage(ChatColor.GOLD + "/uride structure create");
					send.sendMessage(ChatColor.GOLD + "/uride structure point1");
					send.sendMessage(ChatColor.GOLD + "/uride structure point2");
					send.sendMessage(ChatColor.GOLD + "/uride structure origin");
					send.sendMessage(ChatColor.GOLD + "/uride structure build");
					send.sendMessage(ChatColor.GOLD + "/uride structure abort");
					send.sendMessage(ChatColor.GOLD + "/uride element select <Name>");
					send.sendMessage(ChatColor.GOLD + "/uride element exit <boolean>");
					send.sendMessage(ChatColor.GOLD + "/uride element show (only paths)");
					send.sendMessage(ChatColor.GOLD + "/uride element unshow");
					send.sendMessage(ChatColor.GOLD + "/uride element speed <integer> (rotors)");
					send.sendMessage(ChatColor.GOLD + "/uride element speed <double> (paths)");
					send.sendMessage(ChatColor.GOLD + "/uride element child list");
					send.sendMessage(ChatColor.GOLD + "/uride element child select <ID>");
					send.sendMessage(ChatColor.GOLD + "/uride element child remove <ID>");
					send.sendMessage(ChatColor.GOLD + "/uride element child add rotor <radius> <clockwise>");
					send.sendMessage(
							ChatColor.GOLD + "/uride element child add array <rows> <colomns> <width> <lenght>");
					send.sendMessage(ChatColor.GOLD + "/uride element child add entity <type>");
				}
				return true;
			}
			if (senderOfCommand instanceof BlockCommandSender) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("ride")) {
						if (args.length == 3 && args[1].equalsIgnoreCase("start")) {
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								return true;
							}
							plugin.rideMaster.rides.get(args[2]).start();
							return true;
						}
						if (args.length == 3 && args[1].equalsIgnoreCase("stop")) {
							if (!plugin.rideMaster.rides.containsKey(args[2])) {
								return true;
							}
							plugin.rideMaster.rides.get(args[2]).stop();
							return true;
						}
					}
				}
			}
			return true;
		}
		return true;
	}

	public void displayElementInChat(Player send, Moveable element, int layer, int ID) {
		String pre = "";
		String toShow = "";
		if (layer == 0) {
			toShow = element.getType().toString();
		} else {
			for (int i = 1; i <= layer; i++) {
				pre += "| ";
			}
			toShow = pre + "|[" + ChatColor.DARK_GREEN + ID + ChatColor.GOLD + "] " + element.getType().toString();
			if(plugin.rideMaster.selectedElement.containsKey(send)){
				if(element == plugin.rideMaster.selectedElement.get(send)){
					toShow = pre + "|[" + ChatColor.DARK_GREEN + ID + ChatColor.GOLD + "] " + ChatColor.DARK_GREEN + element.getType().toString();
				}
			}
		}
		if (!element.getChildren().isEmpty()) {
			toShow += ":";
		}
		send.sendMessage(ChatColor.GOLD + toShow);
		for (Moveable child : element.getChildren()) {
			displayElementInChat(send, child, layer + 1, element.getChildren().indexOf(child));
		}
		/*if (!element.getChildren().isEmpty()) {
			send.sendMessage(ChatColor.GOLD + pre + "|===");
		}*/
	}

}
