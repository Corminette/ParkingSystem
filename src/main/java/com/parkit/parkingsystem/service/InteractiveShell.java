package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InteractiveShell {
    private InteractiveShell() {
        throw new IllegalStateException(
                "Classe utile ne devant pas être publique");
    }
    /**
     * @see LOGGER
     */
    private static final Logger LOGGER = LogManager
            .getLogger("InteractiveShell");
    /**
     * @see number magic et loadInterface case.
     */
    public static final int LOAD_INTERFACE_MAGIC3 = 3;
    /**
     * Menu et actions liées à l'interface utilisateur
     */
    public static void loadInterface() {
        LOGGER.info("App initialized!!!");
        System.out.println("Welcome to Parking System!");

        boolean continueApp = true;
        InputReaderUtil inputReaderUtil = new InputReaderUtil();
        ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
        TicketDAO ticketDAO = new TicketDAO();
        ParkingService parkingService = new ParkingService(inputReaderUtil,
                parkingSpotDAO, ticketDAO);

        while (continueApp) {
            loadMenu();
            int option = inputReaderUtil.readSelection();
            switch (option) {
                case 1 :
                    parkingService.processIncomingVehicle();
                    break;
                case 2 :
                    parkingService.processExitingVehicle();
                    break;
                case LOAD_INTERFACE_MAGIC3 :
                    System.out.println("Exiting from the system!");
                    continueApp = false;
                    break;
                default :
                    System.out.println(
                            "Unsupported option. Please enter a number corresponding to the provided menu");
            }
        }
    }
    /**
     * @see Ecrire sur l'écran de l'application les différents choix d'entrée.
     */
    private static void loadMenu() {
        System.out.println(
                "Please select an option. Simply enter the number to choose an action");
        System.out.println("1 New Vehicle Entering - Allocate Parking Space");
        System.out.println("2 Vehicle Exiting - Generate Ticket Price");
        System.out.println("3 Shutdown System");
    }
}
