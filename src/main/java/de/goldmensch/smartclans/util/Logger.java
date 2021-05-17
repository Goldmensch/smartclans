/*
 * # Copyright (C) 2021 Nick Hensel
 * # This file is part of Smartclans.
 * #
 * # Smartclans is free software: you can redistribute it and/or modify
 * # it under the terms of the GNU General Public License as published by
 * # the Free Software Foundation, either version 3 of the License, or
 * # (at your option) any later version.
 * #
 * # Smartclans is distributed in the hope that it will be useful,
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * # GNU General Public License for more details.
 * #
 * # You should have received a copy of the GNU General Public License
 */

package de.goldmensch.smartclans.util;

import java.util.logging.Level;

public class Logger {

    private static boolean debug = true;
    private static java.util.logging.Logger logger;

    public static void setup(java.util.logging.Logger logger) {
        Logger.logger = logger;
    }

    public static void setDebug(boolean enabled) {
        Logger.debug = enabled;
    }

    public static void debug(String msg) {
        if(debug) {
            logger.log(Level.WARNING, "[Debug] " + msg);
        }
    }

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void log(Level level, String msg) {
        logger.log(level, msg);
    }

}
