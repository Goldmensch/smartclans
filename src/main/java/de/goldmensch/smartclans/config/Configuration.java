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

package de.goldmensch.smartclans.config;

import de.goldmensch.smartclans.Smartclans;
import de.goldmensch.smartclans.config.elements.Database;
import de.goldmensch.smartclans.util.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.util.Objects;
import java.util.logging.Level;

public class Configuration {

    private final FileConfiguration config;
    private final Smartclans smartclans;

    @Getter
    private boolean debugEnabled;
    @Getter
    private Database database;
    @Getter
    private DatabaseDriver databaseDriver;

    public Configuration(Smartclans smartclans) {
        this.config = smartclans.getConfig();
        this.smartclans = smartclans;
    }

    public void reload() {
        debugEnabled = tempDebugEnabled();
        if (loadDatabase()) return;

        if (debugEnabled) Logger.debug("config reloaded");
    }

    public void buildConfig() {
        FileConfiguration jarConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(Objects.requireNonNull(smartclans.getResource("config.yml"))));

        for (String current : jarConfig.getKeys(true)) {
            if (!config.getKeys(true).contains(current)) {
                config.set(current, jarConfig.get(current));
            }
        }

        for (String current : config.getKeys(true)) {
            if (!jarConfig.getKeys(true).contains(current)) {
                if (!current.startsWith(".")) {
                    config.set(current, null);
                }
            }
        }
        smartclans.saveConfig();
        if (tempDebugEnabled()) Logger.debug("config built");
    }

    private boolean tempDebugEnabled() {
        return config.getBoolean("debug.enabled");
    }

    private boolean loadDatabase() {
        String value = config.getString("storage.driver");
        DatabaseDriver driver = DatabaseDriver.get(value);
        if (driver == null) {
            Logger.log(Level.SEVERE, "No Driver found that matches '" + value + "' - Disable Plugin!");
            smartclans.setDisabled();
            return true;
        }
        databaseDriver = driver;
        database = Database.fromMap(config.getConfigurationSection("storage.database").getValues(false));
        return false;
    }

}
