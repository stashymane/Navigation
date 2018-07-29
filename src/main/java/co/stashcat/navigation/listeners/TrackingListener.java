package co.stashcat.navigation.listeners;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.Navigator;
import co.stashcat.navigation.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class TrackingListener implements Listener {
    Map<Player, Long> lastCheck = new HashMap<>();
    Main pl;

    public TrackingListener(Main p) {
        Bukkit.getPluginManager().registerEvents(this, p);
        pl = p;
    }

    @EventHandler
    public void trackPlayer(PlayerMoveEvent e) {
        if (Tracker.isBeingTracked(e.getPlayer()) && (!lastCheck.containsKey(e.getPlayer()) || System.currentTimeMillis() - lastCheck.get(e.getPlayer()) > pl.getConfig().getDouble("checkInterval") * 50)) { // 1000ms / 20ticks = 50
            Player p = Tracker.getTracker(e.getPlayer());
            Navigator.getDestination(p).setLocation(e.getPlayer().getLocation());
            lastCheck.put(p, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void disconnectListener(PlayerQuitEvent e) {
        if (e.getPlayer() != null && Tracker.isBeingTracked(e.getPlayer())) {
            Player p = Tracker.getTracker(e.getPlayer());
            Main.sendMsg(p, "&cTracking target \"&a%s&c\" has disconnected, stopping tracking.", e.getPlayer().getDisplayName());
            Tracker.stopTracking(p);
        }
    }
}
