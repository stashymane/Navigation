package co.stashcat.navigation.events;

import co.stashcat.navigation.types.Waypoint;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WaypointChangeEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    boolean cancelled = false;

    Waypoint w;
    Location last;
    Location current;

    public WaypointChangeEvent(Waypoint w, Location last, Location current) {
        this.w = w;
        this.last = last;
        this.current = current;
    }

    public Waypoint getWaypoint() {
        return w;
    }

    public Location getLastLocation() {
        return last.clone();
    }

    public Location getCurrentLocation() {
        return current.clone();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
