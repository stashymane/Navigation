package co.stashcat.navigation;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WaypointConfiguration {
    Main pl;
    private File customConfigFile;
    private FileConfiguration customConfig;

    public WaypointConfiguration(Main pl) {
        this.pl = pl;
    }

    public FileConfiguration getWaypointConfig() {
        return this.customConfig;
    }

    private void createWaypointConfig() {
        customConfigFile = new File(pl.getDataFolder(), "waypoints.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            pl.saveResource("waypoints.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
