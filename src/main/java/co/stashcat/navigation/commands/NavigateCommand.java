package co.stashcat.navigation.commands;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.Navigator;
import co.stashcat.navigation.WaypointManager;
import co.stashcat.navigation.types.Waypoint;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.HashSet;

public class NavigateCommand implements CommandExecutor {
    Main pl;

    public NavigateCommand(Main pl) {
        this.pl = pl;
        pl.getCommand("navigate").setExecutor(this);
        pl.getCommand("navigate").setTabCompleter(new NavigateTabCompleter());
    }

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            Main.sendMsg(s, "&cThis command can only be used by players.");
            return true;
        }
        Player p = (Player) s;
        if (args.length == 1) {
            Waypoint w = WaypointManager.getWaypoint(args[0]);
            if (w != null) {
                try {
                    Navigator.navigate(p, w);
                } catch (NoPermissionException e) {
                    Main.sendMsg(s, "&cPermission required to navigate to %s.", w.getId());
                    return true;
                }
                Main.sendMsg(p, "&aNavigating to %s...", w.getName());
                Main.sendMsg(p, "Type /%s to stop.", label);
            } else {
                Main.sendMsg(p, "&cWaypoint %s does not exist.", args[0]);
            }
            return true;
        } else if (args.length == 2 || args.length == 3 && s.hasPermission("navigation.coordinates")) {
            int x;
            int y = 0;
            int z;
            try {
                x = Integer.parseInt(args[0]);
                if (args.length == 3) {
                    y = Integer.parseInt(args[1]);
                    z = Integer.parseInt(args[2]);
                } else {
                    z = Integer.parseInt(args[1]);
                }
            } catch (NumberFormatException e) {
                Main.sendMsg(p, "&cCoordinates contain a non-numeric character.");
                return true;
            }
            Waypoint w = new Waypoint(new Location(p.getWorld(), x, y, z), 5, args.length != 3);
            try {
                Navigator.navigate(p, w);
            } catch (NoPermissionException e) {
                Main.sendMsg(s, "&cPermission required to navigate to %s.", w.getId());
                return true;
            }
            Main.sendMsg(p, "&aNavigating to %d, %d, %d...", x, y, z);
            Main.sendMsg(p, "Type /%s to stop.", label);
            return true;
        } else if (args.length == 0 && Navigator.isNavigating(p)) {
            Navigator.stopNavigation(p);
            Main.sendMsg(p, "&aNavigation stopped.");
            return true;
        } else if (args.length == 0 && !Navigator.isNavigating(p)) {
            Collection<Waypoint> allWaypoints = WaypointManager.getWaypointList();
            Collection<Waypoint> waypointList;
            if (s.hasPermission("navigation.list.all")) {
                waypointList = allWaypoints;
            } else {
                waypointList = new HashSet<>();
                for (Waypoint w : allWaypoints) {
                    if (w.hasPermission(s) && ((Player) s).getWorld().getName().equalsIgnoreCase(w.getLocation().getWorld().getName()))
                        waypointList.add(w);
                }
            }
            if (waypointList.isEmpty()) {
                Main.sendMsg(s, "&cNo waypoints found.");
                return true;
            }
            if (Main.isSpigot()) {
                TextComponent msg = new TextComponent("");
                boolean addSpace = false;
                for (Waypoint w : waypointList) {
                    TextComponent t = new TextComponent(w.getId());
                    t.setColor(ChatColor.GREEN);
                    BaseComponent[] hv = new BaseComponent[3];
                    hv[0] = new TextComponent(w.getName() + "\n");
                    if (w.hasPermission(p))
                        hv[0].setColor(ChatColor.GREEN);
                    else
                        hv[0].setColor(ChatColor.RED);
                    hv[1] = new TextComponent(w.getDesc() + "\n");
                    hv[1].setColor(ChatColor.WHITE);
                    hv[2] = new TextComponent("Click to navigate...");
                    hv[2].setColor(ChatColor.AQUA);
                    t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hv));
                    t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/navigate %s", w.getId())));
                    if (addSpace)
                        msg.addExtra(new TextComponent(", "));
                    msg.addExtra(t);
                    addSpace = true;
                }
                s.spigot().sendMessage(msg);
            } else {
                StringBuilder msg = new StringBuilder("&a");
                for (Waypoint w : waypointList) {
                    if (!msg.toString().equals("&a"))
                        msg.append(" ");
                    msg.append(w.getId());
                }
                Main.sendMsg(s, msg.toString());
            }
            return true;
        }
        Main.sendMsg(s, "&cInvalid arguments.");
        return false;
    }
}
