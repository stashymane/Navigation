package co.stashcat.listeners;

import co.stashcat.Navigator;
import co.stashcat.Tracker;
import co.stashcat.Navigation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;

public class DestinationListener implements Listener {
    static Map<Player, Long> lastCheck = new HashMap<>();

    public DestinationListener(Navigation p) {
        Bukkit.getPluginManager().registerEvents(this, p);
    }

    public static void destinationListener(PlayerMoveEvent e) {
        if (Navigator.isNavigating(e.getPlayer()) && !Tracker.isTracking(e.getPlayer()) && (!lastCheck.containsKey(e.getPlayer()) || System.currentTimeMillis() - lastCheck.get(e.getPlayer()) > 1000)) {
            Player p = e.getPlayer();
            Location loc = p.getLocation();
            lastCheck.put(p, System.currentTimeMillis());
            if (Navigator.hasReachedDestination(p.getLocation(), p.getCompassTarget())) {
                Navigation.sendMsg(p, "&aYou have reached your destination.");
                Navigator.restoreCompassState(p);
            }
        }
    }
}
