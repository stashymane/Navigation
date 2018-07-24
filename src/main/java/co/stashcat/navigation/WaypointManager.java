package co.stashcat.navigation;

import co.stashcat.navigation.types.Waypoint;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaypointManager {
    public static Map<String, Waypoint> waypoints = new HashMap<>();

    public static void loadWaypoints(Configuration c, boolean clear) {
        if (clear) waypoints.clear();
        List<String> keys = c.getStringList("waypoints");
        for (String k : keys) {
            ConfigurationSection cs = c.getConfigurationSection("waypoints." + k);
            Location loc = (Location) cs.get("location");
            int destinationRadius = cs.getInt("destinationRadius");
            boolean ignoreHeight = cs.getBoolean("ignoreHeight");
            String id = cs.getString("id");
            String name = cs.getString("name");
            String desc = cs.getString("description");
            ItemStack item = (ItemStack) cs.get("item");
            waypoints.put(id, new Waypoint(loc, destinationRadius, ignoreHeight, id, name, desc, item));
        }
    }

    public static void loadWaypoints(Configuration c) {
        loadWaypoints(c, true);
    }

    public static Waypoint getWaypoint(String s) {
        return waypoints.get(s);
    }
}
