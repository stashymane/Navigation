package co.stashcat;

import co.stashcat.commands.Navigate;
import co.stashcat.commands.Track;
import co.stashcat.listeners.NavigatorListener;
import co.stashcat.listeners.TrackingListener;
import net.gravitydevelopment.updater.Updater;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Navigation extends JavaPlugin {
    public void onEnable() {
        saveDefaultConfig();
        new NavigatorListener(this);
        new TrackingListener(this);
        new Navigate(this);
        new Track(this);
        Metrics metrics = new Metrics(this);
        Updater updater = new Updater(this, 56256, this.getFile(), Updater.UpdateType.DEFAULT, true);
    }

    public void onDisable() {
        Navigator.restoreCompassStates();
    }

    public static void sendMsg(CommandSender s, String msg) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void sendMsg(CommandSender s, String msg, Object... vars) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        msg = String.format(msg, vars);
        s.sendMessage(msg);
    }
}
