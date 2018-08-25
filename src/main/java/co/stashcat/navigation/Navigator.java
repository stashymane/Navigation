package co.stashcat.navigation;

import co.stashcat.navigation.menus.Prefabs;
import co.stashcat.navigation.types.Waypoint;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.naming.NoPermissionException;
import java.util.*;

public class Navigator {
    static Map<Player, Location> compassState = new HashMap<>();
    static Map<Player, Waypoint> destinations = new HashMap<>();
    static List<Player> lentCompass = new ArrayList<>();
    public static boolean compassRequired = true;

    public static void navigate(Player p, Waypoint w) throws NoPermissionException {
        if (w.hasPermission(p)) {
            if (!checkCompass(p))
                throw new NoPermissionException("A compass is required to navigate.");
            saveCompassState(p);
            destinations.put(p, w);
            p.setCompassTarget(w.getLocation());
        } else {
            throw new NoPermissionException(String.format("Permission is required to navigate to %s", w.getId()));
        }
    }

    public static void stopNavigation(Player p) {
        returnCompass(p);
        destinations.remove(p);
        restoreCompassState(p);
    }

    public static void updateCompassTarget(Player p) {
        if (isNavigating(p) && p.getWorld().equals(destinations.get(p).getLocation().getWorld()))
            p.setCompassTarget(destinations.get(p).getLocation());
    }

    public static boolean hasReachedDestination(Location player, Waypoint dest) {
        if (!player.getWorld().equals(dest.getLocation().getWorld()))
            return false;
        Location temp = dest.getLocation();
        if (dest.isHeightIgnored()) {
            player.setY(0);
            temp.setY(0);
        }
        return player.distance(temp) < dest.getDestinationRadius();
    }

    public static boolean isNavigating(Player p) {
        return destinations.containsKey(p);
    }

    public static Player getNavigator(Waypoint target) {
        if (destinations.containsValue(target)) {
            for (Player key : destinations.keySet()) {
                if (destinations.get(key) == target) {
                    return key;
                }
            }
        }
        return null;
    }

    public static void saveCompassState(Player p) {
        if (!compassState.containsKey(p))
            compassState.put(p, p.getCompassTarget());
    }

    public static void restoreCompassState(Player p) {
        if (compassState.containsKey(p)) {
            p.setCompassTarget(compassState.get(p));
            compassState.remove(p);
        }
    }

    public static void restoreCompassStates() {
        for (Player p : compassState.keySet()) {
            restoreCompassState(p);
        }
    }

    public static Waypoint getDestination(Player p) {
        return destinations.get(p);
    }

    public static Collection<Player> getNavigatingList() {
        return destinations.keySet();
    }

    public static boolean checkCompass(Player p) {
        if (p.hasPermission("navigation.receivecompass") && p.getInventory().firstEmpty() != -1 && !p.getInventory().contains(Material.COMPASS)) {
            p.getInventory().addItem(Prefabs.getGPS());
            lentCompass.add(p);
        }
        return compassRequired && p.getInventory().contains(Material.COMPASS);
    }

    public static void returnCompass(Player p) {
        if (isCompassLent(p)) {
            p.getInventory().remove(Prefabs.getGPS());
            lentCompass.remove(p);
        }
    }

    public static boolean isCompassLent(Player p) {
        return lentCompass.contains(p);
    }
}
