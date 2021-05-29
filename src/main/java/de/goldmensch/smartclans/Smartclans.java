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

import de.goldmensch.smartclans.config.Configuration;
import de.goldmensch.smartclans.data.ServiceHolder;
import de.goldmensch.smartclans.data.database.DatabaseLoader;
import de.goldmensch.smartclans.metrics.MetricsCollector;
import de.goldmensch.smartclans.util.Logger;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.sql.DataSource;
import java.util.UUID;
import java.util.logging.Level;

public class Smartclans extends JavaPlugin {
    private Configuration config;
    private DataSource dataSource;

    @Getter
    private ServiceHolder serviceHolder;
    @Getter
    private BukkitAudiences adventure;
    @Getter
    private boolean developerVersion = false;
    private boolean disabled = false;

    @Override
    public void onLoad() {
        Logger.setup(getLogger());
        if (getDescription().getVersion().toLowerCase().contains("snapshot")) {
            developerVersion = true;
            Logger.log(Level.WARNING, "You use a developer Version of this plugin, this may contain bugs!");
        }

        initConfig();
        if (disabled) return;

        Logger.setDebug(config.isDebugEnabled());
        Logger.debug("logger set up");

        initMetrics();
    }

    @Override
    public void onEnable() {
        if (disabled) {
            getPluginLoader().disablePlugin(this);
            return;
        }
        adventure = BukkitAudiences.create(this);
        Logger.debug("adventure set up");

        initDataBase();
        initServices();

        if(!serviceHolder.getClanService().exist("test")) {
            serviceHolder.getClanService().createClan("test");
            Logger.info("clan test created");
        }else {
            Logger.info("clan test exist");
        }

        Logger.info(String.valueOf(serviceHolder.getClanService().getClan("test").getId()));

        if(!serviceHolder.getPlayerService().exist("goldmensch")) {
            serviceHolder.getPlayerService().createPlayer(Bukkit.getOfflinePlayer(UUID.fromString("3876fc3f-ab8f-4789-a105-e68bafa11f4d")));
            Logger.info("player goldmensch created");
        }else {
            Logger.info("player goldmensch exist");
        }

        Logger.info(serviceHolder.getPlayerService().getPlayer("goldmensch").getUuid().toString());
    }

    @Override
    public void onDisable() {
        disableAdventure();
    }

    private void initMetrics() {
        MetricsCollector metrics = new MetricsCollector(this);
        metrics.setupBStats(10354);
        Logger.debug("successfully init metrics");
    }

    private void initConfig() {
        config = new Configuration(this);
        config.buildConfig();
        config.reload();
        Logger.debug("config setup completed");
    }

    private void disableAdventure() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
    }

    public Audience wrapSender(CommandSender sender) {
        return adventure.sender(sender);
    }

    public void setDisabled() {
        disabled = true;
    }

    public Configuration getPluginConfig() {
        return config;
    }

    private void initDataBase() {
        DatabaseLoader loader = new DatabaseLoader(this);
        dataSource = loader.loadDatabase();
        loader.setup();
    }

    private void initServices() {
        serviceHolder = ServiceHolder.loadServices(config.getDatabaseDriver(), dataSource);
    }


}
