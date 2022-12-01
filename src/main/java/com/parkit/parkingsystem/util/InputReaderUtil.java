package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.dao.TicketDAO;

import java.util.Scanner;

/* classe avec les méthodes permettant de lire ce que l'utilisateur inscrit comme données 
 * en réponse à ce qu'il lui est demandé (le chiffre de sélection d'action, de type choisit, puis
 * la plaque d'immatriculation tapée)
 */

public class InputReaderUtil {

    private static Scanner scan = new Scanner(System.in, "UTF-8");
    private static final Logger logger = LogManager
            .getLogger("InputReaderUtil");

    public int readSelection() {

        try {
            int input = Integer.parseInt(scan.nextLine());
            return input;

        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println(
                    "Error reading input. Please enter valid number for proceeding further");
            return -1;
        }
    }

    public String readVehicleRegistrationNumber() throws Exception {

        try {
            String vehicleRegNumber = scan.nextLine();;

            if (vehicleRegNumber == null
                    || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }

            /**
             * if (vehicleRegNumber.equals(Ticket.getVehicleRegNumber()) ){
             * throw new IllegalArgumentException("Invalid input provided.
             * VehicleRegNumber are just present"); }
             **/

            return vehicleRegNumber;

        } catch (Exception e) {
            logger.error("Error while reading user input from Shell", e);
            System.out.println(
                    "Error reading input. Please enter a valid string for vehicle registration number");
            throw e;
        }
    }

}
