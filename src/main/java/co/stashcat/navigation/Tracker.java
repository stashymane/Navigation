package co.stashcat.navigation;

import co.stashcat.navigation.types.Waypoint;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;
import java.util.HashMap;
import java.util.Map;

public class Tracker {
    static Map<Player, Player> tracking = new HashMap<>();

    public static void track(Player p, Player target) {
        tracking.put(p, target);
        try {
            Waypoint w = new Waypoint(target.getLocation(), 0, false);
            w.setName(target.getDisplayName());
            Navigator.navigate(p, w);
        } catch (NoPermissionException ignored) {
            //Impossible
        }
    }

    public static void stopTracking(Player p) {
        tracking.remove(p);
        Navigator.stopNavigation(p);
    }

    public static boolean isTracking(Player p) {
        return tracking.containsKey(p);
    }

    public static boolean isBeingTracked(Player p) {
        return tracking.containsValue(p);
    }

    public static Player getTracker(Player target) {
        if (isBeingTracked(target)) {
            for (Player key : tracking.keySet()) {
                if (tracking.get(key) == target) {
                    return key;
                }
            }
        }
        return null;
    }

    public static Player getTracking(Player p) {
        return tracking.get(p);
    }
}
