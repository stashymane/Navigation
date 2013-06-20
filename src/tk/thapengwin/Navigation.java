package tk.thapengwin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Navigation extends JavaPlugin {
	Map<String, String> newPoint = new HashMap<String, String>();
	Map<String, Integer> newPos = new HashMap<String, Integer>();
	public Map<String, String> setting = new HashMap<String, String>();
	public String prefix = ChatColor.GREEN + "[bNavi] " + ChatColor.RESET;
	public String cprefix = "[bNavi] ";
	public void onEnable(){
		loadConfiguration();
		new NavListener(this);
        System.out.println(cprefix + getDescription().getFullName() + " is Enabled!");
        System.out.println(cprefix + "By " + getDescription().getAuthors());
    }
    public void onDisable() {
    	saveConfig();
        System.out.println(cprefix + "Disabled!");
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if (!(sender instanceof Player)) {
			sender.sendMessage(cprefix + "This command can only be run by a player.");
			return true;
		} else {
			Player p = (Player)sender;
		if (cmd.getName().equalsIgnoreCase("bnavigation") || cmd.getName().equalsIgnoreCase("bnavi") || cmd.getName().equalsIgnoreCase("navigation") || cmd.getName().equalsIgnoreCase("navigate") || cmd.getName().equalsIgnoreCase("nav") || cmd.getName().equalsIgnoreCase("where")){
		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")){
			sendMsg(p, 0, "-=" + ChatColor.GREEN + getDescription().getFullName() + " Help" + ChatColor.RESET + "=-");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav [help]" + ChatColor.RESET + " Shows help.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav create <id>" + ChatColor.RESET + " Creates a new nav point.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav set [setting]" + ChatColor.RESET + " Lists available settings.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav check" + ChatColor.RESET + " Checks if you set all values for your new point.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav save" + ChatColor.RESET + " Saves nav point settings.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav del <id>" + ChatColor.RESET + " Deletes a navigation point.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav enable <id>" + ChatColor.RESET + " Enables a navigation point.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav disable <id>" + ChatColor.RESET + " Disables a navigation point.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav reload" + ChatColor.RESET + " Reloads the config.");
			sendMsg(p, 0, ChatColor.GREEN + "  /nav version" + ChatColor.RESET + " Shows you the plugin version");
			sendMsg(p, 0, ChatColor.GREEN + "-=" + ChatColor.GREEN + "End" + ChatColor.RESET + "=-");
		} else if (args[0].equalsIgnoreCase("reload")){
				reloadConfig();
				sendMsg(p, 1, "Configuration reloaded!");
		} else if (args[0].equalsIgnoreCase("del") && args.length == 2){
			if (getConfig().getConfigurationSection("points." + args[1]) != null){
				for (String s : getConfig().getStringList("points." + args[1]))
					getConfig().set(s, null);
				sendMsg(p, 1, "Point deleted!");
			} else
				sendMsg(p, 3, "Point does not exist!");
		} else if (args[0].equalsIgnoreCase("save") && args.length == 1){
			if (newPoint.containsKey("id") && newPoint.containsKey("name") && newPoint.containsKey("desc") && newPoint.containsKey("world") && newPoint.containsKey("item") && newPos.containsKey("X") && newPos.containsKey("Z")){
				String id = newPoint.get("id");
				getConfig().set("points." + id + ".name", newPoint.get("name"));
				getConfig().set("points." + id + ".desc", newPoint.get("desc"));
				getConfig().set("points." + id + ".world", newPoint.get("world"));
				getConfig().set("points." + id + ".item", newPoint.get("item"));
				getConfig().set("points." + id + ".X", newPos.get("X"));
				getConfig().set("points." + id + ".Z", newPos.get("Z"));
				List<String> converted=new ArrayList<String>();
				for(Object o:(List<?>)getConfig().getList("points.enabled"))
				    converted.add((String)o);
				converted.add(id);
				getConfig().set("points.enabled",converted);
				saveConfig();
				newPoint.clear();
				sendMsg(p, 1, "Successfully saved your point!");
			} else
				sendMsg(p, 3, "You have not set all the values for your point! /nav check");
		} else if (args[0].equalsIgnoreCase("version") && args.length == 1)
			sendMsg(p, 1, getDescription().getFullName() + " Version " + getDescription().getVersion() + " by " + getDescription().getAuthors().toString());
		else if (args[0].equalsIgnoreCase("check") && args.length == 1){
			if (newPoint.containsKey("id") && newPoint.containsKey("name") && newPoint.containsKey("desc") && newPoint.containsKey("world") && newPoint.containsKey("item") && newPoint.containsKey("X") && newPoint.containsKey("Z"))
				sendMsg(p, 1, "All values set! You can now save your point - /nav save");
			else if (!newPoint.containsKey("id"))
				sendMsg(p, 3, "Point is not created.");
			else {
			sendMsg(p, 0, "-=" + ChatColor.GREEN + "All values (Green = Set, Red = Not Set)" + ChatColor.RESET + "=-");
			if (newPoint.containsKey("name")) sendMsg(p, 0, ChatColor.GREEN + "  Name"); else sendMsg(p, 0, ChatColor.RED + "  Name");
			if (newPoint.containsKey("desc")) sendMsg(p, 0, ChatColor.GREEN + "  Description"); else sendMsg(p, 0, ChatColor.RED + "  Description");
			if (newPoint.containsKey("item")) sendMsg(p, 0, ChatColor.GREEN + "  Item"); else sendMsg(p, 0, ChatColor.RED + "  Item");
			if (newPoint.containsKey("world") && newPoint.containsKey("X") && newPoint.containsKey("Y")) sendMsg(p, 0, ChatColor.GREEN + "  Position"); else sendMsg(p, 0, ChatColor.RED + "  Position");
			sendMsg(p, 0, "-=" + ChatColor.GREEN + "End" + ChatColor.RESET + "=-");
			}
		} else if (args[0].equalsIgnoreCase("create") && args.length == 2){
			newPoint.clear();
			newPoint.put("id", args[1]);
			sendMsg(p, 1, "Successfully created point. Now set the values for it! /nav set");
		} else if (args[0].equalsIgnoreCase("set")){
			if (args.length == 1){
				sendMsg(p, 0, "-=" + ChatColor.GREEN + "Available settings" + ChatColor.RESET + "=-");
				sendMsg(p, 0, ChatColor.GREEN + "  name" + ChatColor.RESET + " Sets the full name of the point.");
				sendMsg(p, 0, ChatColor.GREEN + "  desc" + ChatColor.RESET + " Sets the full description of the point.");
				sendMsg(p, 0, ChatColor.GREEN + "  item" + ChatColor.RESET + " Sets the item/block that will represent the point in the menu to your held item/block.");
				sendMsg(p, 0, ChatColor.GREEN + "  pos" + ChatColor.RESET + " Sets the point's position to your current location.");
				sendMsg(p, 0, "-=" + ChatColor.GREEN + "End" + ChatColor.RESET + "=-");
			} else if (args.length == 2){
				if (args[1].equalsIgnoreCase("name")){
					if (newPoint.containsKey("id")){
					setting.put("name", p.getName());
					sendMsg(p, 1, "Please enter your point name in chat.");
					} else
						sendMsg(p, 3, "You are not setting a point!");
				} else if (args[1].equalsIgnoreCase("desc")){
					if (newPoint.containsKey("id")){
						setting.put("desc", p.getName());
						sendMsg(p, 1, "Please enter your point description in chat.");
						} else
							sendMsg(p, 3, "You are not setting a point!");
				} else if (args[1].equalsIgnoreCase("item")){
					if (newPoint.containsKey("id")){
					String item = p.getItemInHand().getType().toString();
					newPoint.put("item", item);
					sendMsg(p, 1, "Successfully set your item to the point's item!");
					} else
						sendMsg(p, 3, "Point does not exist!");
				} else if (args[1].equalsIgnoreCase("pos")){
					if (newPoint.containsKey("id")){
						Double Xa = p.getLocation().getX();
						Double Za = p.getLocation().getZ();
						Double Xb = (double) Xa.intValue();
						Double Zb = (double) Za.intValue();
						int X = Integer.parseInt(Xb.toString().replaceAll(".0", ""));
						int Z = Integer.parseInt(Zb.toString().replaceAll(".0", ""));
						String world = p.getWorld().getName().toString();
						newPoint.put("world", world);
						newPos.put("X", X);
						newPos.put("Z", Z);
				    sendMsg(p, 1, "Successfully set your current location to the point's location!");
					}
				}
			}
		} else if (args[0].equalsIgnoreCase("tp") && args.length == 2){
			if (getConfig().getString("points." + args[1] + ".name") != null){
				sendMsg(p, 1, "Teleporting to " + args[1]);
				Location loc = new Location(Bukkit.getWorld(getConfig().getString("points." + args[1] + ".world")), getConfig().getInt("points." + args[1] + ".X"), Bukkit.getWorld(getConfig().getString("points." + args[1] + ".world")).getHighestBlockYAt(0, 0), getConfig().getInt("points." + args[1] + ".Z"));
				p.teleport(loc);
				} else {
				sendMsg(p, 3, "Point does not exist.");
			}
		} else if (args[0].equalsIgnoreCase("enable") && args.length == 2){
			if (getConfig().getString("points." + args[1] + ".name") != null){
			List<String> converted=new ArrayList<String>();
			for(Object o:(List<?>)getConfig().getList("points.enabled"))
			    converted.add((String)o);
			converted.add(args[1]);
			getConfig().set("points.enabled",converted);
			sendMsg(p, 1, "Point successfully enabled!");
			} else {
				sendMsg(p, 3, "Point does not exist!");
			}
		} else if (args[0].equalsIgnoreCase("disable") && args.length == 2){
			if (getConfig().getList("points.enabled").contains(args[1])){
				getConfig().getList("points.enabled").remove(args[1]);
				saveConfig();
				sendMsg(p, 1, "Successfully disabled point!");
			} else
				sendMsg(p, 3, "Point not enabled.");
		} else
			sendMsg(p, 3, "Invalid arguments!");
		} }
    	return true;
    }
    @SuppressWarnings("unused")
	public void loadConfiguration(){
    	File configFile = new File(getDataFolder(), "config.yml");
    	if (configFile != null){
    	if (getConfig().getInt("config-version") != 1) resetConfiguration();
    	} else saveDefaultConfig();
    }
    public void resetConfiguration(){
    	File configFile = new File(getDataFolder(), "config.yml");
    	configFile.delete();
    	saveDefaultConfig();
    	reloadConfig();
    }
    public void sendMsg(Player p, int level, String msg){
		if (level == 1)
			p.sendMessage(prefix + msg);
		else if (level == 2)
			p.sendMessage(prefix + ChatColor.YELLOW + msg);
		else if (level == 3)
			p.sendMessage(prefix + ChatColor.RED + msg);
		else if (level == 0){
			p.sendMessage(msg);
		}
	}
}