package co.stashcat;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
public class bNavi extends JavaPlugin {
    public void onEnable()
    {
        saveDefaultConfig();
    }

    public void onDisable()
    {

    }

    public static void sendMsg(CommandSender s, String msg)
    {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
}
