package co.stashcat;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Tracker {
    static Map<Player, Player> tracking = new HashMap<>();

    public static void track(Player p, Player target) {
        tracking.put(p, target);
        Navigation.sendMsg(p, "&aTracking %s...", target.getDisplayName());
        Navigation.sendMsg(p, "Type \"&a/track&r\" to stop tracking.");
    }

    public static void stopTracking(Player p) {
        Navigation.sendMsg(p, "&aStopped tracking &2%s&a.", tracking.get(p));
        tracking.remove(p);
        Navigator.restoreCompassState(p);
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
