package co.stashcat;

import co.stashcat.commands.Navigate;
import co.stashcat.commands.Track;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class bNavi extends JavaPlugin {
    public void onEnable()
    {
        saveDefaultConfig();
        new Navigate(this);
        new Track(this);
    }

    public void onDisable()
    {

    }

    public static void sendMsg(CommandSender s, String msg)
    {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
