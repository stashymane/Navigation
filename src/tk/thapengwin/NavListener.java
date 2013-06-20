package tk.thapengwin;

import java.util.ArrayList;
import java.util.List;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class NavListener implements Listener {
	
	private Navigation plugin;
	public static Inventory navInventory;
	public NavListener(Navigation plugin) {
		this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		if (plugin.setting.get("name") == e.getPlayer().getName()){
		String msg = e.getMessage();
		Player p = e.getPlayer();
		e.setCancelled(true);
		plugin.newPoint.put("name", msg);
		plugin.setting.remove("name");
		plugin.sendMsg(p, 1, "Successfully set the point name!");
		} else if (plugin.setting.get("desc") == e.getPlayer().getName()){
			String msg = e.getMessage();
			Player p = e.getPlayer();
			e.setCancelled(true);
			plugin.newPoint.put("desc", msg);
			plugin.setting.remove("desc");
			plugin.sendMsg(p, 1, "Successfully set the point description!");
		}
	}
	@EventHandler
	public void onCompassRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (player.getItemInHand().getType() == Material.COMPASS) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				List<String> lore = new ArrayList<String>();
				navInventory = Bukkit.createInventory(null, 27, plugin.getConfig().getString("menu-title"));
				for (Object enabled : plugin.getConfig().getList("points.enabled")){
    				lore.add(plugin.getConfig().getString("points." + enabled + ".desc"));
    				navInventory.addItem(setName(new ItemStack(Material.getMaterial(plugin.getConfig().getString("points." + enabled + ".item"))), ChatColor.GREEN + plugin.getConfig().getString("points." + enabled + ".name"), lore));
    				lore.clear();
    				}
				player.openInventory(navInventory);
				navInventory.clear();
				for (Object enabled : plugin.getConfig().getList("points.enabled")){
    				lore.add(plugin.getConfig().getString("points." + enabled + ".desc"));
    				navInventory.addItem(setName(new ItemStack(Material.getMaterial(plugin.getConfig().getString("points." + enabled + ".item"))), ChatColor.GREEN + plugin.getConfig().getString("points." + enabled + ".name"), lore));
    				lore.clear();
    				}
			}
		}
	}
	
	private ItemStack setName(ItemStack is, String name, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		if (name != null)
			im.setDisplayName(name);
		if (lore != null)
			im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getSize() == 27 || e.getInventory().getTitle() == plugin.getConfig().getString("menu-title")) {
			String itemChosen = "nope";
			for (Object enabled : plugin.getConfig().getList("points.enabled")){
			if (e.getCurrentItem().getType() == Material.getMaterial(plugin.getConfig().getString("points." + enabled + ".item"))) {
				itemChosen = "yep";
				Player player = (Player) e.getWhoClicked();
				e.setCancelled(true);
				setCompassTarget(player, plugin.getConfig().getString("points." + enabled + ".world"), plugin.getConfig().getInt("points." + enabled + ".X"), plugin.getConfig().getInt("points." + enabled + ".Z"));
				player.sendMessage(plugin.prefix + "Navigating to " + plugin.getConfig().getString("points." + enabled + ".name"));
				player.closeInventory();
			}
			}
			if (itemChosen != "yep") {
				e.setCancelled(true);
			}
		}
	}
	public void setCompassTarget(Player p, String world, int X, int Z){
		Location location = new Location (Bukkit.getWorld(world), X, 0, Z);
		p.setCompassTarget(location);
	}
}
