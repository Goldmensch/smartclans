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

package de.goldmensch.smartclans.data.repositories.clan;

import de.goldmensch.common.sql.exceptions.EmptyOptionalException;
import de.goldmensch.common.sql.querybuilder.QueryBuilder;
import de.goldmensch.smartclans.data.repositories.clan.elements.Clan;

import javax.sql.DataSource;
import java.util.Optional;

public class MySQLClanRepository implements ClanRepository {

    DataSource dataSource;

    public MySQLClanRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Clan create(Clan clan) {
        clan.setId(QueryBuilder.builder(dataSource, Long.class)
                .query("INSERT IGNORE INTO clans(name) VALUES(?)")
                .params(prep -> prep.setString(1, clan.getName()))
                .append()
                .query("SELECT id FROM clans WHERE name = ?")
                .params(prep -> prep.setString(1, clan.getName()))
                .readRow(rs -> rs.getLong("id"))
                .first().orElseThrow(EmptyOptionalException::new));
        return clan;
    }

    @Override
    public void update(Clan clan) {
        QueryBuilder.builder(dataSource, null)
                .query("UPDATE clans SET name = ? WHERE id = ?")
                .params(prep -> {
                    prep.setString(1, clan.getName());
                    prep.setLong(2, clan.getId());
                })
                .update()
                .execute();
    }

    @Override
    public void delete(Clan clan) {
        QueryBuilder.builder(dataSource, null)
                .query("DELETE FROM clans WHERE id = ?")
                .params(prep -> prep.setLong(1, clan.getId()))
                .update()
                .execute();
    }

    @Override
    public Optional<Clan> read(Long id) {
        return QueryBuilder.builder(dataSource, Clan.class)
                .query("SELECT name FROM clans WHERE id = ?")
                .params(prep -> prep.setLong(1, id))
                .readRow(rs -> new Clan(id, rs.getString("name")))
                .first();
    }

    @Override
    public Optional<Clan> read(String name) {
        return QueryBuilder.builder(dataSource, Clan.class)
                .query("SELECT id FROM clans WHERE name = ?")
                .params(prep -> prep.setString(1, name))
                .readRow(rs -> new Clan(rs.getLong("id"), name))
                .first();
    }
}
