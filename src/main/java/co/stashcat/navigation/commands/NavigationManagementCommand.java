package co.stashcat.navigation.commands;

import co.stashcat.navigation.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class NavigationManagementCommand implements CommandExecutor {
    Main pl;

    public NavigationManagementCommand(Main p) {
        pl = p;
        p.getCommand("navigation").setExecutor(this);
    }

    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            Main.sendMsg(s, "  &a%s version %s", pl.getDescription().getName(), pl.getDescription().getVersion());
            Main.sendMsg(s, "&a%s reload&r - reloads the plugin", label);
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            Main.reload();
            Main.sendMsg(s, "&aSuccessfully reloaded plugin.");
            return true;
        }
        return false;
    }
}
