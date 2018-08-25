package co.stashcat.navigation.commands;

import co.stashcat.navigation.WaypointManager;
import co.stashcat.navigation.types.Waypoint;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaypointTabCompleter implements TabCompleter {
    public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
        List<String> c = new ArrayList<>();
        if (args.length == 1) {
            c.addAll(Arrays.asList("new", "edit", "review", "set", "save", "cancel", "delete"));
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("review") || args[0].equalsIgnoreCase("delete"))) {
            for (Waypoint w : WaypointManager.getWaypointList()) {
                if (w.hasPermission(s))
                    c.add(w.getId());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            c.addAll(Arrays.asList("id", "name", "desc", "item", "location", "radius", "ignoreheight", "permissionrequired"));
        } else if (s instanceof Player && args.length > 2 && args.length < 8 && args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("location")) {
            Player p = (Player) s;
            Location l = p.getLocation();
            if (args.length == 3)
                c.add("" + l.getBlockX());
            if (args.length == 4)
                c.add("" + l.getBlockY());
            if (args.length == 5)
                c.add("" + l.getBlockZ());
            if (args.length == 6)
                c.add(l.getWorld().getName());
        }
        String lastArg = args[args.length - 1];
        if (args.length > 0 && !lastArg.equals("")) {
            for (int i = 0; i < c.size(); i++) {
                String str = c.get(i);
                if (!str.startsWith(lastArg)) {
                    c.remove(i);
                    i--;
                }
            }
        }
        return c;
    }
}
