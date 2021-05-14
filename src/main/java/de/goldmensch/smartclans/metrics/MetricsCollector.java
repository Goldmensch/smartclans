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
