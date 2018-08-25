package co.stashcat.navigation.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Prefabs {
    public static ItemStack getGPS() {
        ItemStack i = new ItemStack(Material.COMPASS);
        ItemMeta m = i.getItemMeta();
        m.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "GPS");
        m.setLore(Collections.singletonList(ChatColor.WHITE + "A compass that points to where you want to go."));
        i.setItemMeta(m);
        return i;
    }
}
