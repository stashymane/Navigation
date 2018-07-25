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
        createWaypointConfig();
    }

    public FileConfiguration getWaypointConfig() {
        return this.customConfig;
    }

    public void saveWaypointConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWaypointConfig() {
        customConfigFile = new File(pl.getDataFolder(), "waypoints.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
