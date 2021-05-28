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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.goldmensch.smartclans.config.elements.Database;

import javax.sql.DataSource;
import java.util.Properties;

public final class DataSourceProvider {

    private final Database database;
    private final Class<? extends DataSource> dataSourceClass;

    public DataSourceProvider(Database database, Class<? extends DataSource> dataSourceClass) {
        this.database = database;
        this.dataSourceClass = dataSourceClass;
    }

    public DataSource initDataSource() {
        Properties properties = new Properties();
        properties.setProperty("dataSourceClassName", dataSourceClass.getName());
        properties.setProperty("dataSource.serverName", database.getHost());
        properties.setProperty("dataSource.portNumber", String.valueOf(database.getPort()));
        properties.setProperty("dataSource.user", database.getUser());
        properties.setProperty("dataSource.password", database.getPassword());
        properties.setProperty("dataSource.databaseName", database.getDatabase());

        HikariConfig config = new HikariConfig(properties);
        config.setMaximumPoolSize(10);
        return new HikariDataSource(config);
    }

}
