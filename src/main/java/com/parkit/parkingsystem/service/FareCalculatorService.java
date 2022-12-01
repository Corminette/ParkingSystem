package com.parkit.parkingsystem.service;

import java.time.temporal.ChronoUnit;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
/* Méthode permettant de calculer à partir de la différence entre le moment d'entrée et le moment de sortie du parking inscrit sur le ticket
 * et BDD le prix à régler, en prenant en compte les 30 premières minutes gratuites et 5% de réduction à partir du 5ème 
 * passage dans le parking. Local Date Time est utilisé comme mode de temps. **/
public class FareCalculatorService {

    /**
     * Permet de calculer le prix du stationnement
     * 
     * @param ticket
     * @param loyalty
     */
    public void calculateFare(final Ticket ticket, final boolean loyalty) {

        if ((ticket.getOutTime() == null)
                || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:"
                    + ticket.getOutTime().toString());
        }
        long hours = ticket.getInTime().until(ticket.getOutTime(),
                ChronoUnit.SECONDS);
        System.out.println(">>>" + hours / Fare.GIVE_SECOND_3600D);

        double duration = hours / Fare.GIVE_SECOND_3600D;

        if (duration <= Fare.GIVE_UNE_DEMI_HEURE) {
            ticket.setPrice(0);
            System.out.println("gratuit");

        } else {

            if (loyalty) {

                System.out.println(
                        "votre fidèlité vous accorde 5% de réduction à partir de maintenant");

                switch (ticket.getParkingSpot().getParkingType()) {
                    case CAR :
                        ticket.setPrice(Fare.TAUX_DE_REDUCTION * duration
                                * Fare.CAR_RATE_PER_HOUR);
                        break;
                    case BIKE :
                        ticket.setPrice(Fare.TAUX_DE_REDUCTION * duration
                                * Fare.BIKE_RATE_PER_HOUR);
                        break;
                    default :
                        throw new IllegalArgumentException(
                                "Unkown Parking Type");
                }
            } else {
                switch (ticket.getParkingSpot().getParkingType()) {
                    case CAR :
                        ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                        break;
                    case BIKE :
                        ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                        break;
                    default :
                        throw new IllegalArgumentException(
                                "Unkown Parking Type");
                }
            }
        }
    }
}