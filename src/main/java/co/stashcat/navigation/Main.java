package co.stashcat.navigation;

import co.stashcat.navigation.commands.*;
import co.stashcat.navigation.listeners.CoordinateListener;
import co.stashcat.navigation.listeners.NavigatorListener;
import co.stashcat.navigation.listeners.TrackingListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
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
        registerActionBar();
        reload();
        if (getConfig().getBoolean("allowStats"))
            new Metrics(this);
    }

    public static void reload() {
        getPlugin(Main.class).reloadConfig();
        waypointConfig.reloadConfig();
        WaypointManager.loadWaypoints(waypointConfig.getConfig());
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
        getConfig().set("lastVersion", getDescription().getVersion());
        getConfig().addDefault("allowStats", true);
        getConfig().addDefault("checkInterval", "5");
        getConfig().addDefault("coordsCommands", new String[]{"tell, w, msg, r"});
        getConfig().addDefault("showActionBar", true);
        getConfig().options().copyDefaults();
        saveConfig();
    }

    private void registerActionBar() {
        if (!getConfig().getBoolean("showActionBar"))
            return;
        int ci = getConfig().getInt("checkInterval");
        ActionBar.plugin = this;
        ActionBar.nmsver = Bukkit.getServer().getClass().getPackage().getName();
        ActionBar.nmsver = ActionBar.nmsver.substring(ActionBar.nmsver.lastIndexOf(".") + 1);
        if (ActionBar.nmsver.startsWith("v1_7_")) {
            ActionBar.useOldMethods = true;
        }
        new ActionBarUpdater().runTaskTimerAsynchronously(this, ci, ci);
    }
}
