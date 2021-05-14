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
