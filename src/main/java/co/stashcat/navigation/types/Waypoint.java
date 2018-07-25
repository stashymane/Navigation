package co.stashcat.navigation.types;

import co.stashcat.navigation.Main;
import co.stashcat.navigation.events.WaypointChangeEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

public class Waypoint {
    Location loc;
    int destinationRadius;
    boolean ignoreHeight;
    String id;
    String name = "Waypoint";
    String desc = "";
    ItemStack item = new ItemStack(Material.PAPER);

    public Waypoint(Location loc, int destinationRadius, boolean ignoreHeight) {
        setLocation(loc);
        setDestinationRadius(destinationRadius);
        setIgnoreHeight(ignoreHeight);
    }

    public Waypoint(Location loc, int destinationRadius, boolean ignoreHeight, String id, String name, String desc, ItemStack item) {
        setId(id);
        setName(name);
        setDescription(desc);
        setLocation(loc);
        setDestinationRadius(destinationRadius);
        setIgnoreHeight(ignoreHeight);
        setItem(item);
    }

    public boolean save() {
        if (id == null)
            return false;
        Configuration c = Main.waypointConfig.getWaypointConfig();
        save(c, id, "name", name);
        save(c, id, "description", desc);
        save(c, id, "location", loc);
        save(c, id, "destinationRadius", destinationRadius);
        save(c, id, "ignoreHeight", ignoreHeight);
        save(c, id, "item", item);
        Main.waypointConfig.saveWaypointConfig();
        Main.reload();
        return true;
    }

    private void save(Configuration s, String id, String var, Object val) {
        s.set("waypoints." + id + "." + var, val);
    }

    public boolean delete() {
        Configuration c = Main.waypointConfig.getWaypointConfig();
        if (id == null)
            return false;
        c.set("waypoints." + id, null);
        Main.waypointConfig.saveWaypointConfig();
        Main.reload();
        return true;
    }


    public void setId(String id) throws IllegalArgumentException {
        if (StringUtils.isAlphanumeric(id))
            this.id = id;
        else
            throw new IllegalArgumentException("IDs can only be alphanumeric.");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setLocation(Location loc) {
        Bukkit.getPluginManager().callEvent(new WaypointChangeEvent(this, this.loc, loc));
        this.loc = loc;
    }

    public void setDestinationRadius(int radius) {
        this.destinationRadius = radius;
    }

    public void setIgnoreHeight(boolean ignoreHeight) {
        this.ignoreHeight = ignoreHeight;
    }

    public void setItem(ItemStack item) {
        this.item = item;
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

    public ItemStack getItem() {
        return item.clone();
    }

    public int getDestinationRadius() {
        return destinationRadius;
    }

    public boolean isHeightIgnored() {
        return ignoreHeight;
    }
}
