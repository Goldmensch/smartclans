
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

package de.goldmensch.smartclans.commands.clancommand.subs;

import de.goldmensch.smartclans.Smartclans;
import de.goldmensch.smartclans.commands.SmartCommandSub;
import de.goldmensch.smartclans.text.MessageBuilder;
import de.goldmensch.smartclans.text.MessageUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class About extends SmartCommandSub {

    private Component message;

    public About(Smartclans smartclans) {
        super(smartclans);
    }

    public About buildMessage() {
        message = MessageBuilder.builder(Smartclans.PREFIX)
                .line("Name: " + getSmartclans().getName())
                .line("Version: " + getSmartclans().getDescription().getVersion())
                .line("Authors: " + MessageUtils.formatList(getSmartclans().getDescription().getAuthors()))
                .link("Website: ", getSmartclans().getDescription().getWebsite())
                .build();
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, @NotNull Audience audience) {
        audience.sendMessage(message);
        return true;
    }
}
