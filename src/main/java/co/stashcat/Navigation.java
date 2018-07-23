package co.stashcat;

import co.stashcat.commands.Navigate;
import co.stashcat.commands.Track;
import co.stashcat.listeners.DestinationListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Navigation extends JavaPlugin {
    public void onEnable() {
        saveDefaultConfig();
        new DestinationListener(this);
        new Navigate(this);
        new Track(this);
        Metrics metrics = new Metrics(this);
    }

    public void onDisable() {
        Navigator.restoreCompassStates();
    }

    public static void sendMsg(CommandSender s, String msg) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}