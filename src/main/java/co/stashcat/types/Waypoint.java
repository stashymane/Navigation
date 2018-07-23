package co.stashcat.types;

import org.bukkit.Location;

public class Waypoint {
    String id;
    String name;
    String desc;
    Location loc;

    public Waypoint(String id, String name, String desc, Location loc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.loc = loc;
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
}
