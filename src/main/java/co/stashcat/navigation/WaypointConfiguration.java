package co.stashcat.navigation;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WaypointConfiguration {
    Main pl;
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    public WaypointConfiguration(Main pl) {
        this.pl = pl;
        reloadConfig();
    }

    public void reloadConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(pl.getDataFolder(), "waypoints.yml");
        }
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            reloadConfig();
        }
        return customConfig;
    }

    public void saveConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            pl.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }
}
