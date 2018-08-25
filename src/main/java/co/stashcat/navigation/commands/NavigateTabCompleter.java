package co.stashcat.navigation.commands;

import co.stashcat.navigation.WaypointManager;
import co.stashcat.navigation.types.Waypoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NavigateTabCompleter implements TabCompleter {
    public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
        List<String> c = new ArrayList<>();
        if (!(s instanceof Player))
            return c;
        Player p = (Player) s;
        if (args.length == 1) {
            for (Waypoint w : WaypointManager.getWaypointList()) {
                if (w.hasPermission(s) && w.getLocation().getWorld().equals(p.getWorld()))
                    c.add(w.getId());
            }
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
