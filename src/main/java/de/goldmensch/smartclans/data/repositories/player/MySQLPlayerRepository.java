
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

package de.goldmensch.smartclans.data.repositories.player;

import de.goldmensch.common.sql.querybuilder.QueryBuilder;
import de.goldmensch.common.sql.utils.TypeConverter;
import de.goldmensch.smartclans.data.repositories.clan.elements.Clan;
import de.goldmensch.smartclans.data.repositories.player.elements.Player;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class MySQLPlayerRepository implements PlayerRepository {

    private final DataSource source;

    @Override
    public Player create(Player player) {
        QueryBuilder.builder(source, null)
                .query("INSERT INTO players(uuid, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE name = ?")
                .params(prep -> {
                    prep.setBytes(1, TypeConverter.convert(player.getUuid()));
                    prep.setString(2, player.getName());
                    prep.setString(3, player.getName());
                })
                .update()
                .execute();
        return player;
    }

    @Override
    public void update(Player player) {
        QueryBuilder.builder(source, null)
                .query("UPDATE players SET name = ?, clan_id = ?, rank_id = ? WHERE uuid = ?")
                .params(prep -> {
                    prep.setString(1, player.getName());
                    prep.setLong(2, player.getClanId());
                    prep.setInt(3, player.getRankID());
                    prep.setBytes(4, TypeConverter.convert(player.getUuid()));
                })
                .update()
                .execute();
    }

    @Override
    public void delete(Player player) {
        QueryBuilder.builder(source, null)
                .query("DELETE FROM players WHERE uuid = ?")
                .params(prep -> prep.setBytes(1, TypeConverter.convert(player.getUuid())))
                .update()
                .execute();
    }

    @Override
    public Optional<Player> read(UUID uuid) {
        return QueryBuilder.builder(source, Player.class)
                .query("SELECT * FROM players WHERE uuid = ?")
                .params(prep -> prep.setBytes(1, TypeConverter.convert(uuid)))
                .readRow(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .first();
    }

    @Override
    public List<Player> getALl(Clan clan) {
        return QueryBuilder.builder(source, Player.class)
                .queryWithoutParams("SELECT * FROM players")
                .readRow(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .all();
    }

    @Override
    public Optional<Player> read(String name) {
        return QueryBuilder.builder(source, Player.class)
                .query("SELECT * FROM players WHERE name = ?")
                .params(prep -> prep.setString(1, name))
                .readRow(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .first();
    }
}
