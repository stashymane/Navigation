package co.stashcat;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class Tracker implements Listener {
    static Map<Player, Player> tracking = new HashMap<>();

    public static boolean isTracking(Player p) {
        return tracking.containsKey(p);
    }
}
