package de.goldmensch.smartclans;

import de.goldmensch.smartclans.util.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.util.Objects;

public class PluginConfiguration {

    private final FileConfiguration config;

    public PluginConfiguration(FileConfiguration config) {
        this.config = config;
    }

    public void buildConfig(JavaPlugin plugin) {
        FileConfiguration jarConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(plugin.getResource("config.yml"))));
        plugin.reloadConfig();
        FileConfiguration fileConfig = plugin.getConfig();

        for (String current : jarConfig.getKeys(true)) {
            if (!fileConfig.getKeys(true).contains(current)) {
                fileConfig.set(current, jarConfig.get(current));
            }
        }

        for (String current : fileConfig.getKeys(true)) {
            if (!jarConfig.getKeys(true).contains(current)) {
                if (!current.startsWith(".")) {
                    fileConfig.set(current, null);
                }
            }
        }
        plugin.saveConfig();
        if(tempDebugEnabled()) Logger.debug("config built");
    }

    @Getter
    private boolean debugEnabled;
    @Getter
    private boolean bStatsEnabled;

    public void load() {
        debugEnabled = tempDebugEnabled();
        bStatsEnabled = config.getBoolean("metrics.bStats");
        if(debugEnabled) Logger.debug("config loaded");
    }

    private boolean tempDebugEnabled() {
        return config.getBoolean("debug.enabled");
    }

}
