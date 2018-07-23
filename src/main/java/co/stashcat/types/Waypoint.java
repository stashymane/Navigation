package co.stashcat.types;

import org.bukkit.Location;

public class Waypoint {
    String id;
    String name;
    String desc;
    Location loc;
    String world;
    int destinationRadius;
    boolean ignoreHeight;

    public Waypoint(String id, String name, String desc, Location loc, String world, int destinationRadius, boolean ignoreHeight) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.loc = loc;
        this.world = world;
        this.destinationRadius = destinationRadius;
        this.ignoreHeight = ignoreHeight;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Location getLocation() {
        return loc;
    }

    public String getWorld() {
        return world;
    }

    public int getDestinationRadius() {
        return destinationRadius;
    }

    public boolean isHeightIgnored() {
        return ignoreHeight;
    }
}
