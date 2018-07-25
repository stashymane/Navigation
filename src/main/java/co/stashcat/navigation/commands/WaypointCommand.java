package co.stashcat.navigation.commands;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.WaypointManager;
import co.stashcat.navigation.types.Waypoint;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WaypointCommand implements CommandExecutor {
    Map<CommandSender, Waypoint> editing = new HashMap<>();
    Main pl;

    public WaypointCommand(Main p) {
        pl = p;
        p.getCommand("waypoint").setExecutor(this);
    }

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            displayHelp(s, label);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
            displayVariables(s);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("new")) {

            editing.put(s, new Waypoint(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), 10, true));
            Main.sendMsg(s, "&aCreated new waypoint.");
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
            Waypoint w = WaypointManager.getWaypoint(args[1]);
            if (w != null) {
                editing.put(s, w);
                Main.sendMsg(s, "&aEditing waypoint %s.", w.getName());
            } else
                Main.sendMsg(s, "&cWaypoint %s not found.", args[1]);
            return true;
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
            if (!editing.containsKey(s)) {
                Main.sendMsg(s, "&cYou are not editing a waypoint.");
                return true;
            }
            String variable = args[1];
            String value = StringUtils.join(args, " ").replace(args[0] + " " + args[1] + " ", "");
            if (args.length == 3 && variable.equalsIgnoreCase("id")) {
                try {
                    editing.get(s).setId(args[2]);
                } catch (IllegalArgumentException e) {
                    Main.sendMsg(s, "&cIDs can only be alphanumeric.");
                    return true;
                }
            } else if (args.length >= 3 && variable.equalsIgnoreCase("name"))
                editing.get(s).setName(value);
            else if (args.length >= 3 && variable.equalsIgnoreCase("desc"))
                editing.get(s).setDescription(value);
            else if (args.length == 3 && variable.equalsIgnoreCase("radius"))
                try {
                    editing.get(s).setDestinationRadius(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    Main.sendMsg(s, "&cRadius must be a number.");
                    return true;
                }
            else if (args.length == 3 && variable.equalsIgnoreCase("ignoreheight"))
                try {
                    boolean ignore = value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("y");
                    editing.get(s).setIgnoreHeight(ignore);
                    value = Boolean.toString(ignore);
                } catch (IllegalArgumentException e) {
                    Main.sendMsg(s, "&cIgnoreHeight must be either true or false.");
                }
            else if (args.length == 2 && variable.equalsIgnoreCase("item")) {
                if (!(s instanceof Player)) {
                    Main.sendMsg(s, "&cItem can only be set by players.");
                    return true;
                }
                Player p = (Player) s;
                ItemStack held = p.getItemOnCursor();
                editing.get(s).setItem(held);
                value = held.getType().toString();
            } else if (variable.equalsIgnoreCase("location")) {
                if (!(s instanceof Player)) {
                    Main.sendMsg(s, "&cShorthand location can only be set by players.");
                    return true;
                }
                Player p = (Player) s;
                Location l = p.getLocation();
                editing.get(s).setLocation(l);
                value = String.format("%d, %d, %d in world %s", l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName());
            } else if ((args.length == 5 || args.length == 6) && variable.equalsIgnoreCase("location")) {
                if (args.length == 5 && !(s instanceof Player)) {
                    Main.sendMsg(s, "&cOnly players can emit the world name.");
                    return true;
                }
                int x = Integer.parseInt(args[2]);
                int y = Integer.parseInt(args[3]);
                int z = Integer.parseInt(args[4]);
                World world;
                if (args.length == 5)
                    world = ((Player) s).getWorld();
                else
                    world = Bukkit.getWorld(args[5]);
                if (world == null) {
                    Main.sendMsg(s, "&cWorld %s could not be found.", args[5]);
                    return true;
                }
                Location l = new Location(world, x, y, z);
                editing.get(s).setLocation(l);
                value = String.format("%d, %d, %d in %s", x, y, z, world.getName());
            } else {
                Main.sendMsg(s, "&cVariable &b%s&c not found.", variable);
                return true;
            }
            Main.sendMsg(s, "&aVariable &b%s&a successfully set to &b%s", variable, value);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
            if (editing.containsKey(s)) {
                Waypoint w = editing.get(s);
                w.save();
                Main.sendMsg(s, "&aSuccessfully saved waypoint &b%s&a.", w.getName());
                editing.remove(s);
                return true;
            } else {
                Main.sendMsg(s, "&cYou are not editing a waypoint.");
                return true;
            }
        }
        Main.sendMsg(s, "&cInvalid arguments.");
        return false;
    }

    private void displayHelp(CommandSender s, String label) {
        Main.sendMsg(s, "  &aNavigation waypoint editor");
        Main.sendMsg(s, "-----");
        sendCommandInfo(s, label, "new", "Creates new waypoint");
        sendCommandInfo(s, label, "edit <id>", "Edits specified waypoint");
        sendCommandInfo(s, label, "set <variable> (value)", "Sets defined variable");
        sendCommandInfo(s, label, "set item", "Sets the waypoint item to your currently held item");
        sendCommandInfo(s, label, "set location", "Sets the location to your current");
        sendCommandInfo(s, label, "set location <x> <y> <z>", "Sets the location to the specified coordinates");
        sendCommandInfo(s, label, "save", "Saves your current waypoint");
        Main.sendMsg(s, "For a list of variables, type &a/%s set", label);
    }

    private void displayVariables(CommandSender s) {
        Main.sendMsg(s, "  &aWaypoint variables");
        Main.sendMsg(s, "-----");
        sendVarInfo(s, "id", "<id>", "Alphanumeric waypoint ID to be used in commands");
        sendVarInfo(s, "name", "<name>", "Waypoint name");
        sendVarInfo(s, "desc", "<description>", "Waypoint description");
        sendVarInfo(s, "item", "Sets waypoint item to your currently held item");
        sendVarInfo(s, "location", "Sets waypoint location to your current");
        sendVarInfo(s, "location", "<x> <y> <z> [world]", "Sets waypoint location to the specified coordinates");
        sendVarInfo(s, "radius", "<radius>", "Waypoint arrival radius");
        sendVarInfo(s, "ignoreheight", "<true/false>", "If height is ignored when calculating distance to waypoint");
    }

    private void sendVarInfo(CommandSender s, String var, String values, String description) {
        sendVarInfo(s, var + ChatColor.translateAlternateColorCodes('&', String.format(" &b%s&r", values)), description);
    }

    private void sendVarInfo(CommandSender s, String var, String description) {
        Main.sendMsg(s, "&a%s&r - %s", var, description);
    }

    private void sendCommandInfo(CommandSender s, String command, String args, String description) {
        Main.sendMsg(s, "/%s &a%s&r - %s", command, args, description);
    }
}
