package co.stashcat;

import co.stashcat.types.Waypoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Navigator {
    static Map<Player, Location> compassState = new HashMap<>();
    static Map<Player, Waypoint> destinations = new HashMap<>();

    public static void setDestination(Player p, Waypoint w) {
        setDestination(p, w.getLocation());
        Navigation.sendMsg(p, "&aNavigating to %s...", w.getName());
    }

    public static void setDestination(Player p, Waypoint loc) {
        saveCompassState(p);
        destinations.put(p, loc);
        p.setCompassTarget(loc);
        Navigation.sendMsg(p, "&aNavigation target set to %d, %d.", loc.getBlockX(), loc.getBlockZ());
    }

    public static boolean hasReachedDestination(Location player, Waypoint dest) {
        Location temp = dest.getLocation();
        if (dest.isHeightIgnored()) {
            player.setY(0);
            temp.setY(0);
        }
        return player.distance(temp) < dest.getDestinationRadius();
    }

    public static boolean isNavigating(Player p) {
        return compassState.containsKey(p);
    }

    public static void saveCompassState(Player p) {
        if (!compassState.containsKey(p))
            compassState.put(p, p.getCompassTarget());
    }

    public static void restoreCompassState(Player p) {
        if (compassState.containsKey(p)) {
            p.setCompassTarget(compassState.get(p));
            compassState.remove(p);
        }
    }

    public static void restoreCompassStates() {
        for (Player p : compassState.keySet()) {
            restoreCompassState(p);
        }
    }
}
