package co.stashcat.navigation.listeners;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.Navigator;
import co.stashcat.navigation.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashMap;
import java.util.Map;

public class CompassListener implements Listener {
    Map<Player, Long> lastCheck = new HashMap<>();
    Main pl;

    public CompassListener(Main p) {
        Bukkit.getPluginManager().registerEvents(this, p);
        pl = p;
    }

    @EventHandler
    public void cancelNavigation(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType().equals(Material.COMPASS) && Navigator.isNavigating(e.getPlayer())) {
            Player p = e.getPlayer();
            if (Navigator.isCompassLent(p))
                e.getItemDrop().getItemStack().setAmount(0);
            else
                e.setCancelled(true);
            if (Tracker.isTracking(e.getPlayer()))
                Main.sendMsg(e.getPlayer(), "&aTracking stopped.");
            else
                Main.sendMsg(e.getPlayer(), "&aNavigation stopped.");
            Tracker.stopTracking(e.getPlayer());
        }

    }
}