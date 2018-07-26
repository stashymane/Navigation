package co.stashcat.navigation;

import co.stashcat.navigation.commands.*;
import co.stashcat.navigation.listeners.CoordinateListener;
import co.stashcat.navigation.listeners.NavigatorListener;
import co.stashcat.navigation.listeners.TrackingListener;
import net.gravitydevelopment.updater.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static WaypointConfiguration waypointConfig;
    private static boolean isSpigot;

    public void onEnable() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            isSpigot = true;
        } catch (ClassNotFoundException e) {
            isSpigot = false;
        }
        saveDefaultConfig();
        updateDefaults();
        waypointConfig = new WaypointConfiguration(this);
        new NavigatorListener(this);
        new TrackingListener(this);
        new CoordinateListener(this);
        new NavigateCommand(this);
        new TrackCommand(this);
        new WaypointCommand(this);
        new WaypointListCommand(this);
        new NavigationManagementCommand(this);
        reload();
        Metrics metrics = new Metrics(this);
        Updater updater = new Updater(this, 56256, this.getFile(), Updater.UpdateType.DEFAULT, true);
    }

    public static void reload() {
        WaypointManager.loadWaypoints(waypointConfig.getWaypointConfig());
    }

    public void onDisable() {
        Navigator.restoreCompassStates();
    }

    public static boolean isSpigot() {
        return isSpigot;
    }

    public static void sendMsg(CommandSender s, String msg) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void sendMsg(CommandSender s, String msg, Object... vars) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        msg = String.format(msg, vars);
        s.sendMessage(msg);
    }

    public void updateDefaults() {
        getConfig().addDefault("checkInterval", "1");
        getConfig().addDefault("coordsCommands", new String[]{"tell, w, msg, r"});
    }
}
