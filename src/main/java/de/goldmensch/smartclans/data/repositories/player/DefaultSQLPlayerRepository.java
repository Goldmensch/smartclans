
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
public class DefaultSQLPlayerRepository implements PlayerRepository {

    private final DataSource source;

    @Override
    public Player create(Player player) {
        QueryBuilder.builder(source, null)
                .setQuery("INSERT INTO players(uuid, name) VALUES(?, ?) ON DUPLICATE KEY UPDATE name = ?")
                .setStatements(prep -> {
                    prep.setBytes(1, TypeConverter.convert(player.getUuid()));
                    prep.setString(2, player.getName());
                    prep.setString(3, player.getName());
                })
                .update()
                .executeUpdate();
        return player;
    }

    @Override
    public void update(Player player) {
        QueryBuilder.builder(source, null)
                .setQuery("UPDATE players SET name = ?, clan_id = ?, rank_id = ? WHERE uuid = ?")
                .setStatements(prep -> {
                    prep.setString(1, player.getName());
                    prep.setLong(2, player.getClanId());
                    prep.setInt(3, player.getRankID());
                    prep.setBytes(4, TypeConverter.convert(player.getUuid()));
                })
                .update()
                .executeUpdate();
    }

    @Override
    public void delete(Player player) {
        QueryBuilder.builder(source, null)
                .setQuery("DELETE FROM players WHERE uuid = ?")
                .setStatements(prep -> prep.setBytes(1, TypeConverter.convert(player.getUuid())))
                .update()
                .executeUpdate();
    }

    @Override
    public Optional<Player> read(UUID uuid) {
        return QueryBuilder.builder(source, Player.class)
                .setQuery("SELECT * FROM players WHERE uuid = ?")
                .setStatements(prep -> prep.setBytes(1, TypeConverter.convert(uuid)))
                .extractResults(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .retrieveResult();
    }

    @Override
    public List<Player> getALl(Clan clan) {
        return QueryBuilder.builder(source, Player.class)
                .setQuery("SELECT * FROM players")
                .emptyStatements()
                .extractResults(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .retrieveResults();
    }

    @Override
    public Optional<Player> read(String name) {
        return QueryBuilder.builder(source, Player.class)
                .setQuery("SELECT * FROM players WHERE name = ?")
                .setStatements(prep -> prep.setString(1, name))
                .extractResults(rs -> new Player(
                        TypeConverter.convert(rs.getBytes("uuid")),
                        rs.getString("name"),
                        rs.getLong("clan_id"),
                        rs.getInt("rank_id")
                ))
                .retrieveResult();
    }
}
