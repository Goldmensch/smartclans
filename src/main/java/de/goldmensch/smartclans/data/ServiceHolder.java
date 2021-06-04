/*
 * Copyright (C) 2021 Nick Hensel
 * This file is part of Smartclans.
 *
 * Smartclans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * Smartclans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package de.goldmensch.smartclans.data;

import de.goldmensch.smartclans.config.DatabaseDriver;
import de.goldmensch.smartclans.data.repositories.clan.ClanRepository;
import de.goldmensch.smartclans.data.repositories.clan.MySQLClanRepository;
import de.goldmensch.smartclans.data.repositories.player.MySQLPlayerRepository;
import de.goldmensch.smartclans.data.repositories.player.PlayerRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.sql.DataSource;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceHolder {

    private final ClanService clanService;
    private final PlayerService playerService;

    public static ServiceHolder loadServices(DatabaseDriver driver, DataSource dataSource) {
        PlayerRepository playerRepository = null;
        ClanRepository clanRepository = null;
        switch (driver) {
            case MARIA_DB:
            case MYSQL:
                playerRepository = new MySQLPlayerRepository(dataSource);
                clanRepository = new MySQLClanRepository(dataSource);
        }
        return new ServiceHolder(new ClanService(clanRepository), new PlayerService(playerRepository));
    }

}
