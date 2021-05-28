
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

import de.goldmensch.smartclans.data.repositories.player.IPlayerService;
import de.goldmensch.smartclans.data.repositories.player.PlayerRepository;
import de.goldmensch.smartclans.data.repositories.player.elements.Player;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class PlayerService implements IPlayerService {

    private final PlayerRepository repository;

    @Override
    public Player getPlayer(UUID uuid) {
        Optional<Player> optional = repository.read(uuid);
        if (!optional.isPresent()) {
            throw new RuntimeException("cannot find player(uuid): " + uuid);
        }
        return optional.get();
    }

    @Override
    public Player getPlayer(String name) {
        Optional<Player> optional = repository.read(name);
        if (!optional.isPresent()) {
            throw new RuntimeException("cannot find player(name): " + name);
        }
        return optional.get();
    }

    @Override
    public boolean exist(String name) {
        return repository.read(name).isPresent();
    }

    @Override
    public Player createPlayer(OfflinePlayer player) {
        return repository.create(new Player(player.getName(), player.getUniqueId()));
    }
}
