package co.stashcat.navigation.commands;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.WaypointManager;
import co.stashcat.navigation.types.Waypoint;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
            displayVariables(s, label);
            return true;
        } else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("review")) {
            if (args.length == 2 || editing.containsKey(s)) {
                Waypoint w;
                if (args.length == 2) {
                    w = WaypointManager.getWaypoint(args[1]);
                    if (w == null) {
                        Main.sendMsg(s, "&cWaypoint &b%s&c not found.", args[1]);
                        return true;
                    }
                }
                w = editing.get(s);
                sendWaypointInfo(s, w);
            } else {
                Main.sendMsg(s, "&cYou are not editing a waypoint.");
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
            if (!editing.containsKey(s)) {
                Main.sendMsg(s, "&cYou are not editing a waypoint.");
                return true;
            }
            Main.sendMsg(s, "&aCancelled editing &b%s&a.", editing.get(s));
            editing.remove(s);
            return true;
        } else if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("delete")) {
            if (args.length == 1 && !editing.containsKey(s)) {
                Main.sendMsg(s, "&cYou are not editing a waypoint.");
                return true;
            }
            Waypoint w;
            if (args.length == 1)
                w = editing.get(s);
            else {
                w = WaypointManager.getWaypoint(args[1]);
                if (w == null) {
                    Main.sendMsg(s, "&cWaypoint &b%s&c does not exist.", args[1]);
                    return true;
                }
            }
            w.delete();
            Main.sendMsg(s, "&aWaypoint successfully deleted.");
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
                ItemStack held = p.getInventory().getItemInMainHand();
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
                Main.sendMsg(s, "&cInvalid arguments.", variable);
                return false;
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
        Main.sendMsg(s, "  -----");
        Main.sendMsg(s, "  &aNavigation waypoint editor");
        Main.sendMsg(s, "  -----");
        sendCommandInfo(s, label, "new", "Creates new waypoint");
        sendCommandInfo(s, label, "edit <id>", "Edits specified waypoint");
        sendCommandInfo(s, label, "review (id)", "Views all waypoint variables");
        sendCommandInfo(s, label, "set <variable> (value)", "Sets defined variable");
        sendCommandInfo(s, label, "set item", "Sets the waypoint item to your currently held item");
        sendCommandInfo(s, label, "set location", "Sets the location to your current");
        sendCommandInfo(s, label, "set location <x> <y> <z>", "Sets the location to the specified coordinates");
        sendCommandInfo(s, label, "save", "Saves your current waypoint");
        sendCommandInfo(s, label, "cancel", "Stops editing your current waypoint");
        sendCommandInfo(s, label, "delete (id)", "Deletes waypoint");
        Main.sendMsg(s, "For a list of variables, type &a/%s set", label);
    }

    private void displayVariables(CommandSender s, String label) {
        Main.sendMsg(s, "  -----");
        Main.sendMsg(s, "  &aWaypoint variables");
        Main.sendMsg(s, "  -----");
        sendVarInfo(s, label, "id", "<id>", "Alphanumeric waypoint ID to be used in commands");
        sendVarInfo(s, label, "name", "<name>", "Waypoint name");
        sendVarInfo(s, label, "desc", "<description>", "Waypoint description");
        sendVarInfo(s, label, "item", null, "Sets waypoint item to your currently held item");
        sendVarInfo(s, label, "location", null, "Sets waypoint location to your current");
        sendVarInfo(s, label, "location", "<x> <y> <z> [world]", "Sets waypoint location to the specified coordinates");
        sendVarInfo(s, label, "radius", "<radius>", "Waypoint arrival radius");
        sendVarInfo(s, label, "ignoreheight", "<true/false>", "If height is ignored when calculating distance to waypoint");
    }

    private void sendVarInfo(CommandSender s, String label, String var, String values, String description) {
        if (Main.isSpigot()) {
            TextComponent t = new TextComponent("/" + label + " set ");
            String combined = var;
            if (values != null)
                combined += " " + values;
            TextComponent vt = new TextComponent(combined);
            vt.setColor(ChatColor.GREEN);
            t.addExtra(vt);
            t.addExtra(" - " + description);
            t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/%s set %s", label, combined)));
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to edit command").create()));
            s.spigot().sendMessage(t);
        } else {
            if (!values.equals(""))
                values = " " + values;
            else
                values = "";
            Main.sendMsg(s, "&a%s&b%s&r - %s", var, values, description);
        }
    }

    private void sendCommandInfo(CommandSender s, String label, String args, String description) {
        if (Main.isSpigot()) {
            TextComponent t = new TextComponent("/" + label + " ");
            TextComponent at = new TextComponent(args);
            at.setColor(ChatColor.GREEN);
            t.addExtra(at);
            t.addExtra(" - " + description);
            t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + label + " " + args));
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to edit command").create()));
            s.spigot().sendMessage(t);
        } else
            Main.sendMsg(s, "/%s &a%s&r - %s", label, args, description);
    }

    private void sendWaypointInfo(CommandSender s, Waypoint w) {
        sendWaypointInfo(s, "id", w.getId());
        sendWaypointInfo(s, "name", w.getName());
        sendWaypointInfo(s, "desc", w.getDesc());
        sendWaypointInfo(s, "radius", w.getDestinationRadius());
        sendWaypointInfo(s, "location", w.getLocation());
        sendWaypointInfo(s, "item", w.getItem());
        sendWaypointInfo(s, "ignoreheight", w.isHeightIgnored());
    }

    private void sendWaypointInfo(CommandSender s, String var, Object val) {
        if (Main.isSpigot()) {
            String value;
            if (val == null)
                value = "NOT SET";
            else
                value = val.toString();
            TextComponent t = new TextComponent(var + ": ");
            t.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/waypoint set %s", var)));
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to edit").create()));
            TextComponent tv = new TextComponent(value);
            tv.setColor(ChatColor.GREEN);
            t.addExtra(tv);
            s.spigot().sendMessage(t);
        } else {
            Main.sendMsg(s, "%s: &a%s", var, val.toString());
        }
    }
}
