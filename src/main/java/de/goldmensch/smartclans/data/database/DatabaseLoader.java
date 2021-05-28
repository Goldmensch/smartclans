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

package de.goldmensch.smartclans.data.database;

import de.goldmensch.smartclans.Smartclans;
import de.goldmensch.smartclans.config.DatabaseDriver;
import de.goldmensch.smartclans.config.elements.Database;
import de.goldmensch.smartclans.data.database.updater.DefaultSQLUpdater;
import de.goldmensch.smartclans.data.database.updater.Updater;
import de.goldmensch.smartclans.util.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseLoader {

    private final Smartclans smartclans;
    private final DatabaseDriver driver;
    private DataSource source;

    public DatabaseLoader(Smartclans smartclans) {
        this.smartclans = smartclans;
        this.driver = smartclans.getPluginConfig().getDatabaseDriver();
    }

    public DataSource loadDatabase() {
        initDataBase();
        Logger.debug("finished " + driver.getName() + " initialisation");
        return source;
    }

    private void initDataBase() {
        try {
            Database database = smartclans.getPluginConfig().getDatabase();
            source = new DataSourceProvider(database, driver.getDataSourceClass()).initDataSource();
            testDataSource(source);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void setup() {
        Updater updater = new DefaultSQLUpdater(source);
        updater.update(driver);
    }

    private void testDataSource(DataSource source) throws SQLException {
        Logger.debug("start database connection test");
        try (Connection conn = source.getConnection()) {
            if (!conn.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
        Logger.info("Database connection test successful");
    }

}
