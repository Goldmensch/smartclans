
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

package de.goldmensch.smartclans.commands;

import de.eldoria.eldoutilities.simplecommands.EldoCommand;
import de.goldmensch.smartclans.Smartclans;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class SmartCommandSub extends EldoCommand {

    private final Smartclans smartclans;

    public SmartCommandSub(Smartclans smartclans) {
        super(smartclans);
        this.smartclans = smartclans;
    }

    public Smartclans getSmartclans() {
        return smartclans;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return this.onCommand(sender, command, label, args, smartclans.wrapSender(sender));
    }

    public abstract boolean onCommand(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args,
                                      @NotNull Audience audience);


}
