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

import de.goldmensch.smartclans.data.repositories.clan.ClanRepository;
import de.goldmensch.smartclans.data.repositories.clan.elements.Clan;
import de.goldmensch.smartclans.data.services.IClanService;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class ClanService implements IClanService {

    private final ClanRepository repository;

    @Override
    public Clan createClan(String name) {
        return repository.create(new Clan(name));
    }

    @Override
    public Clan getClan(String name) {
        Optional<Clan> clan = repository.read(name);
        if (!clan.isPresent()) {
            throw new RuntimeException("Clan not exist: " + name);
        }
        return clan.get();
    }

    @Override
    public void deleteClan(Clan clan) {
        repository.delete(clan);
    }

    @Override
    public boolean exist(String name) {
        return repository.read(name).isPresent();
    }
}
