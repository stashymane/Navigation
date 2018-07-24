package co.stashcat.commands;

import co.stashcat.Navigation;
import co.stashcat.Tracker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Track implements CommandExecutor {
    public Track(Navigation p) {
        p.getCommand("track").setExecutor(this);
    }

    public boolean onCommand(CommandSender s, Command cmd, String str, String[] args) {
        if (!(s instanceof Player)) {
            Navigation.sendMsg(s, "&cThis command can only be used by players.");
            return true;
        }
        Player p = (Player) s;
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null && target.isOnline()) {
                Tracker.track(p, target);
                Navigation.sendMsg(p, "Type \"&a/track&r\" to stop tracking.");
                return true;
            } else {
                Navigation.sendMsg(s, "&aPlayer \"%s\" is not online.", args[0]);
                return true;
            }
        } else if (args.length == 0) {
            Navigation.sendMsg(p, "&aStopped tracking &2%s&a.", Tracker.getTracking(p));
            Tracker.stopTracking(p);
        }
        Navigation.sendMsg(s, "&cInvalid arguments.");
        return false;
    }
}