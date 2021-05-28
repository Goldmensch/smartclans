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

package de.goldmensch.smartclans.config.elements;

import lombok.Getter;

import java.util.Map;

public class Database {
    @Getter
    private final String host;
    @Getter
    private final int port;
    @Getter
    private final String database;
    @Getter
    private final String user;
    @Getter
    private final String password;

    public Database(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public static Database fromMap(Map<String, Object> map) {
        return new Database(
                (String) map.get("host"),
                (int) map.get("port"),
                (String) map.get("database"),
                (String) map.get("user"),
                (String) map.get("password"));
    }
}

