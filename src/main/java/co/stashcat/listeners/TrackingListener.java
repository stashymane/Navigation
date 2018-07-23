package co.stashcat.listeners;

import co.stashcat.Navigation;
import co.stashcat.Navigator;
import co.stashcat.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class TrackingListener implements Listener {
    Map<Player, Long> lastCheck = new HashMap<>();
    Navigation pl;

    public TrackingListener(Navigation p) {
        Bukkit.getPluginManager().registerEvents(this, p);
        pl = p;
    }

    public void trackPlayer(PlayerMoveEvent e) {
        if (Tracker.isBeingTracked(e.getPlayer()) && (!lastCheck.containsKey(e.getPlayer()) || System.currentTimeMillis() - lastCheck.get(e.getPlayer()) > pl.getConfig().getDouble("checkInterval") * 1000)) {
            Player p = Tracker.getTracker(e.getPlayer());
            Navigator.setDestination(p, e.getPlayer().getLocation());
            lastCheck.put(p, System.currentTimeMillis());
        }
    }

    public void disconnectListener(PlayerQuitEvent e) {
        if (Tracker.isBeingTracked(e.getPlayer()) && e.getPlayer() != null) {
            Navigation.sendMsg(Tracker.getTracker(e.getPlayer()), "&cTracking target \"&a%s&c\" has disconnected, stopping tracking.", e.getPlayer().getDisplayName());
            Tracker.stopTracking(e.getPlayer());
        }
    }
}
