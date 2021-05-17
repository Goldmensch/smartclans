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

import de.goldmensch.smartclans.metrics.MetricsCollector;
import de.goldmensch.smartclans.util.Logger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Smartclans extends JavaPlugin {

    private PluginConfiguration config;

    @Getter
    private boolean developerVersion = false;

    @Override
    public void onLoad() {
        Logger.setup(getLogger());
        if(getDescription().getVersion().toLowerCase().contains("snapshot")) {
            developerVersion = true;
            Logger.log(Level.WARNING, "You use a developer Version of this plugin, this may contain bugs!");
        }

        initConfig();

        Logger.setDebug(config.isDebugEnabled());
        Logger.debug("logger set up");

        initMetrics();
    }

    @Override
    public void onEnable() {
        //hehe nothing here
    }

    private void initMetrics() {
        MetricsCollector metrics = new MetricsCollector(this);
        if(config.isBStatsEnabled()) {
            metrics.setupBStats(10354);
        }else {
            Logger.info("bStats disabled");
        }
        Logger.debug("successfully init metrics");
    }

    private void initConfig() {
        config = new PluginConfiguration(getConfig());
        config.buildConfig(this);
        config.load();
    }

}
