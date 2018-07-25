package co.stashcat.navigation.listeners;

import co.stashcat.navigation.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateListener implements Listener {
    Main pl;
    Pattern xyzRegex = Pattern.compile("(\\d+)([ ]|[,;][ ]?)(\\d+)\\2(\\d+)");
    Set<String> listeningCommands = new HashSet<>();

    public CoordinateListener(Main pl) {
        this.pl = pl;
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            Bukkit.getPluginManager().registerEvents(this, pl);
            loadListeningCommands();
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("Server implementation is not Spigot - chat coordinates are disabled.");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void commandListener(PlayerCommandPreprocessEvent e) { //TODO: doesnt work, check where it stops
        String s = e.getMessage().replace("/", "");
        if (s.split(" ").length >= 3) {
            String cmd = s.substring(0, s.indexOf(" "));
            Bukkit.getLogger().info("command: " + cmd);
            Bukkit.getLogger().info("listeningCommands: ");
            for (String a : listeningCommands)
                Bukkit.getLogger().info(a);
            if (listeningCommands.contains(cmd)) {
                String recipient = s.split(" ")[1];
                String text = s.replace(cmd, "").replaceFirst(" ", "").replace(recipient, "");
                Bukkit.getLogger().info("recipient: " + recipient);
                Bukkit.getLogger().info("text: " + text);
                Matcher m = xyzRegex.matcher(text);
                Player rp = Bukkit.getPlayer(recipient);
                if (rp.isOnline()) {
                    rp.spigot().sendMessage(getCoordText(matchCoords(m)));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void chatListener(AsyncPlayerChatEvent e) {
        Set<Player> recipients = new HashSet<>();
        for (Player p : e.getRecipients()) {
            if (p.hasPermission("navigation.chatcoords"))
                recipients.add(p);
        }
        if (recipients.isEmpty())
            return;
        String msg = e.getMessage();
        Matcher m = xyzRegex.matcher(msg);
        List<Vector> navigateTo = matchCoords(m);
        for (Player p : recipients)
            p.spigot().sendMessage(getCoordText(navigateTo));
    }

    public List<Vector> matchCoords(Matcher m) {
        List<Vector> coordList = new ArrayList<>();
        while (m.find()) {
            String coords = m.group(0);
            String separator = m.group(2);
            String[] xyz = coords.split(separator);
            int x = Integer.parseInt(xyz[0]);
            int y = Integer.parseInt(xyz[1]);
            int z = Integer.parseInt(xyz[2]);
            coordList.add(new Vector(x, y, z));
        }
        return coordList;
    }

    public TextComponent getCoordText(List<Vector> l) {
        TextComponent s = new TextComponent("Navigate to");
        boolean addSemicolon = false;
        for (Vector v : l) {
            if (addSemicolon)
                s.addExtra(";");
            s.addExtra(" ");
            s.addExtra(getCoordText(v.getBlockX(), v.getBlockY(), v.getBlockZ()));
            addSemicolon = true;
        }
        s.addExtra(new TextComponent("."));
        return s;
    }

    public TextComponent getCoordText(int x, int y, int z) {
        TextComponent s = new TextComponent(String.format("(%d, %d, %d)", x, y, z));
        s.setColor(ChatColor.GREEN);
        s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/navigate %d %d %d", x, y, z)));
        s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to navigate").create()));
        return s;
    }

    public void loadListeningCommands() {
        listeningCommands.addAll(pl.getConfig().getStringList("coordsCommands"));
    }
}
