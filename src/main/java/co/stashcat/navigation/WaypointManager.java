package co.stashcat.navigation;

import co.stashcat.navigation.types.Waypoint;
import org.bukkit.configuration.Configuration;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WaypointManager {
    public static Map<String, Waypoint> waypoints = new HashMap<>();

    public static void loadWaypoints(Configuration c, boolean clear) {
        if (clear) waypoints.clear();
        Set<String> keys = c.getKeys(false);
        for (String k : keys)
            waypoints.put(k, Waypoint.fromConfig(c, k));
    }

    public static void loadWaypoints(Configuration c) {
        loadWaypoints(c, true);
    }

    public static Waypoint getWaypoint(String s) {
        return waypoints.get(s);
    }

    public static Map<String, Waypoint> getWaypoints() {
        return waypoints;
    }

    public static Collection<Waypoint> getWaypointList() {
        return waypoints.values();
    }
}
