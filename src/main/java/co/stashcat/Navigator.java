package co.stashcat;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Navigator {
    static Map<Player, Location> compassState = new HashMap<>();

    public static void setDestination(Player p, Location loc) {
        saveCompassState(p);
        p.setCompassTarget(loc);
        Navigation.sendMsg(p, "&aNavigation target set to " + loc.getBlockX() + ", " + loc.getBlockZ() + ".");
    }

    public static boolean hasReachedDestination(Location player, Location destination) {
        return player.distance(destination) < 10;
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
