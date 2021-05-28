
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

import de.goldmensch.smartclans.data.repositories.player.elements.Player;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface IPlayerService {
    Player getPlayer(UUID uuid);

    Player getPlayer(String name);

    boolean exist(String name);

    Player createPlayer(OfflinePlayer player);
}
