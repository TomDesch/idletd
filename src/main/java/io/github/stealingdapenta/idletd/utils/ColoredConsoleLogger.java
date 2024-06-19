package io.github.stealingdapenta.idletd.utils;

import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ColoredConsoleLogger {

    private final Logger logger;

    public void info(String message) {
        this.info(ANSIColor.WHITE, message);
    }

    public void info(ANSIColor color, String message) {
        logger.info(color + message + ANSIColor.RESET);
    }

    public void warning(String message) {
        this.warning(ANSIColor.YELLOW, message);
    }

    public void warning(ANSIColor color, String message) {
        logger.warning(color + message + ANSIColor.RESET);
    }

    public void severe(String message) {
        this.severe(ANSIColor.RED, message);
    }

    public void severe(ANSIColor color, String message) {
        logger.severe(color + message + ANSIColor.RESET);
    }

}
