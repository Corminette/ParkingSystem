package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* Lance l'application avec ses logs et l'initialise. */
public final class APP {
    private APP() {
        throw new IllegalStateException("Dans App");
    }
    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger("App");
  /**
   * @param args
   */
    public static void main(final String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
