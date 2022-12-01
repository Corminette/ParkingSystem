package com.parkit.parkingsystem.service;

import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

/**
 * Classe qui regroupe les méthodes déjà vu en DAO. Gestion des places de
 * parking. Echange avec l'utilisateur.
 */
public class ParkingService {
    /**
     * @see LOGGER
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingService");
    /**
     * @see FareCalculatorService dans le même package
     */
    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();
    /**
     * @see voir package "util" Classe qui permet de lire ce qui est écrit,
     *      scanner.
     */
    private InputReaderUtil inputReaderUtil;
    /**
     * @see voir package DAO. Echange avec la BDD.
     */
    private ParkingSpotDAO parkingSpotDAO;
    /**
     * @see Même package, mais là pour le ticket.
     */
    private TicketDAO ticketDAO;

    /**
     * @param inputReaderUtil
     * @param parkingSpotDAO
     * @param ticketDAO
     */
    public ParkingService(final InputReaderUtil inputReaderUtil,
            final ParkingSpotDAO parkingSpotDAO, final TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }
    /**
     * @implSpec méthode de gestion d'entrée du véhicule. Messages, actions avec
     *           l'utilisateur et le reste.
     */
    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != null && parkingSpot.getId() > 0) {

                String vehicleRegNumber = getVehichleRegNumber();
                Integer count = ticketDAO.customerLoyalty(vehicleRegNumber);

                if (count >= Fare.NUMBER_OF_PASSAGE_NECESSARY_LOYALTY) {
                    System.out.println(
                            "Votre fidèlité parmis nous vous permet d'obtenir 5% de réduction enfin!");
                }

                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);

                LocalDateTime inTime = LocalDateTime.now();
                Ticket ticket = new Ticket();

                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);

                ticketDAO.saveTicket(ticket);

                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:"
                        + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:"
                        + vehicleRegNumber + " is:" + inTime);
            }

        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
        }
    }

    /*
     * méthode pour récupérer la plaque d'immatriculation écrite par
     * l'utilisateur
     **/

    private String getVehichleRegNumber() throws Exception {
        System.out.println(
                "Please type the vehicle registration number and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    public ParkingSpot getNextParkingNumberIfAvailable() {

        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;

        try {
            ParkingType parkingType = getVehichleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);

            } else {
                throw new Exception(
                        "Error fetching parking number from DB. Parking slots might be full");
            }

        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);

        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
        }

        return parkingSpot;
    }

    private ParkingType getVehichleType() {

        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();

        switch (input) {
            case 1 :
                return ParkingType.CAR;
            case 2 :
                return ParkingType.BIKE;
            default :
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    public void processExitingVehicle() {

        try {
            boolean loyalty = false;

            String vehicleRegNumber = getVehichleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            LocalDateTime outTime = LocalDateTime.now();
            ticket.setOutTime(outTime);

            Integer count = ticketDAO.customerLoyalty(vehicleRegNumber);
            if (count >= Fare.NUMBER_OF_PASSAGE_NECESSARY_LOYALTY) {
                loyalty = true;
            }

            fareCalculatorService.calculateFare(ticket, loyalty);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println(
                        "Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:"
                        + ticket.getVehicleRegNumber() + " is:" + outTime);

            } else {
                System.out.println(
                        "Unable to update ticket information. Error occurred");
            }

        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
        }
    }
}
