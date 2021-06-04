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

package de.goldmensch.smartclans.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class MessageBuilder {

    private TextComponent.Builder builder;
    private final Component prefix;
    private final TextColor COLOR = NamedTextColor.GRAY;

    private MessageBuilder(Component prefix) {
        builder = Component.empty().toBuilder();
        this.prefix = prefix;
    }

    public static MessageBuilder builder(Component prefix) {
        return new MessageBuilder(prefix);
    }

    public MessageBuilder line(String msg) {
        appendComponent(prepare(msg));
        return this;
    }

    public MessageBuilder link(String pre, String link) {
        appendComponent(Component.text()
                .append(prepare(pre))
                .append(Component.text(link)
                        .clickEvent(ClickEvent.openUrl(link))
                .color(NamedTextColor.AQUA))
                .build());
        return this;
    }

    public Component build() {
        return builder.build();
    }

    private void appendComponent(Component component) {
        if(builder.children().size() != 0) {
            builder.append(Component.text("\n"));
        }
        builder.append(prefix);
        builder.append(component);
    }

    private Component prepare(String msg) {
        return Component.text(msg).color(COLOR);
    }

}
