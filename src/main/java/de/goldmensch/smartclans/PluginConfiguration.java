/*
 * Copyright (C) 2021 Nick Hensel
 * This file is part of Smartclans.
 *
 * Smartclans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smartclans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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
