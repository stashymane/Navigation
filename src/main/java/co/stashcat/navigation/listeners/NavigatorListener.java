package co.stashcat.navigation.listeners;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.Navigator;
import co.stashcat.navigation.Tracker;
import co.stashcat.navigation.events.WaypointChangeEvent;
import co.stashcat.navigation.types.Waypoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class NavigatorListener implements Listener {
    static Map<Player, Long> lastCheck = new HashMap<>();
    Main pl;

    public NavigatorListener(Main p) {
        Bukkit.getPluginManager().registerEvents(this, p);
        pl = p;
    }

    @EventHandler
    public void destinationListener(PlayerMoveEvent e) {
        if (Navigator.isNavigating(e.getPlayer()) && !Tracker.isTracking(e.getPlayer()) && (!lastCheck.containsKey(e.getPlayer()) || System.currentTimeMillis() - lastCheck.get(e.getPlayer()) > pl.getConfig().getDouble("checkInterval") * 1000)) {
            Player p = e.getPlayer();
            Location ploc = p.getLocation();
            Waypoint w = Navigator.getDestination(p);
            lastCheck.put(p, System.currentTimeMillis());
            if (Navigator.hasReachedDestination(p.getLocation(), w)) {
                Main.sendMsg(p, "&aYou have reached your destination.");
                Navigator.stopNavigation(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void updateEvent(WaypointChangeEvent e) {
        if (!e.isCancelled()) {
            Navigator.updateCompassTarget(Navigator.getNavigator(e.getWaypoint()));
        }
    }
}
