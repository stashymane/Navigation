package co.stashcat.commands;

import co.stashcat.Navigation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Navigate implements CommandExecutor {
    public Navigate(Navigation p) {
        p.getCommand("navigate").setExecutor(this);
    }

    public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
        if (!(s instanceof Player)) {
            Navigation.sendMsg(s, "&cThis command can only be used by players.");
            return true;
        }
        if (args.length == 1) {
            Navigation.sendMsg(s, "&cCommand under construction.");
            return true;
        } else if (args.length == 2 || args.length == 3) {
            //Navigation.sendMsg(p, "&aNavigating to %s...", w.getName());
        }
        return false;
    }
}
