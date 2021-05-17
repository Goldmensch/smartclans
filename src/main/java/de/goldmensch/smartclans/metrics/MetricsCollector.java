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

package de.goldmensch.smartclans.metrics;

import de.goldmensch.smartclans.Smartclans;
import de.goldmensch.smartclans.util.Logger;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

public class MetricsCollector {

    private final Smartclans smartclans;

    public MetricsCollector(Smartclans smartclans) {
        this.smartclans = smartclans;
    }

    public void setupBStats(int id) {
        Metrics metrics = new Metrics(smartclans, id);
        if(smartclans.isDeveloperVersion()) {
            metrics.addCustomChart(new SimplePie("developer_version", () -> "yes"));
        }else {
            metrics.addCustomChart(new SimplePie("developer_version", () -> "no"));
        }
        Logger.info("bStats Metrics were successfully activated.");
    }

}
