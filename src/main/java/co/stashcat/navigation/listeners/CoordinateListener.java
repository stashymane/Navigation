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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateListener implements Listener {
    Pattern xyzRegex = Pattern.compile("(\\d+)([ ]|[,;][ ]?)(\\d+)\\2(\\d+)");

    public CoordinateListener(Main pl) {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            Bukkit.getPluginManager().registerEvents(this, pl);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().warning("Server implementation is not Spigot - chat coordinates are disabled.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
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
        List<TextComponent> navigateTo = new ArrayList<>();
        while (m.find()) {
            String coords = m.group(0);
            String separator = m.group(2);
            String[] xyz = coords.split(separator);
            int x = Integer.parseInt(xyz[0]);
            int y = Integer.parseInt(xyz[1]);
            int z = Integer.parseInt(xyz[2]);

            TextComponent s = new TextComponent(String.format("(%d, %d, %d)", x, y, z));
            s.setColor(ChatColor.GREEN);
            s.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/navigate %d %d %d", x, y, z)));
            s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to navigate").create()));
            navigateTo.add(s);
        }
        TextComponent s = new TextComponent("Navigate to");
        boolean addSemicolon = false;
        for (TextComponent t : navigateTo) {
            if (addSemicolon)
                s.addExtra(";");
            s.addExtra(" ");
            s.addExtra(t);
            addSemicolon = true;
        }
        s.addExtra(new TextComponent("."));
        for (Player p : recipients)
            p.spigot().sendMessage(s);
    }
}
