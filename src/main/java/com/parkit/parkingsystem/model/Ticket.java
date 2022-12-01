package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * classe contenant les méthodes permettant de contenir sur le ticket. Les y
 * ôter, les données nécessaires pour stationner dans le parking. Payer en
 * sortant selon la durée passée: création des objets. Retour des valeurs
 * utilisées dans des calculs dans une autre classe.
 * 
 * @author annelore Création d'objets.
 */
public class Ticket {
    /**
     * C'est le numéro d'enregistrement
     */
    private int id;
    /**
     * La place de parking (numéro)
     */
    private ParkingSpot parkingSpot;
    /**
     * La plaque d'immatriculation qui sera demandé
     */
    private String vehicleRegNumber;
    /**
     * le prix à payer à la fin
     */
    private double price;
    /**
     * l'instant d'entrée dans le parking (en heures jusqu'aux nano)
     */
    private LocalDateTime inTime;
    /**
     * l'instant où on décide de partir et utilise son application
     */
    private LocalDateTime outTime;
    /**
     * @return int
     */
    public int getId() {
        return id;
    }
    /**
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }
    /**
     * @return le clone de la place de parking dispo ou pas.
     */
    public ParkingSpot getParkingSpot() {
        return (ParkingSpot) parkingSpot.clone();
    }
    /**
     * @param parkingSpot
     */
    public void setParkingSpot(final ParkingSpot parkingSpot) {
        this.parkingSpot = (ParkingSpot) parkingSpot.clone();
    }
    /**
     * @return la plaque d'immatriculation
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }
    /**
     * @param vehicleRegNumber
     */
    public void setVehicleRegNumber(final String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }
    /**
     * @return price (double)
     */
    public double getPrice() {
        return price;
    }
    /**
     * @param price
     */
    public void setPrice(final double price) {
        this.price = price;
    }
    /**
     * @return inTime (en zoneUTC)
     */
    public LocalDateTime getInTime() {
        return inTime;
    }
    /**
     * @param inTime
     */
    public void setInTime(final LocalDateTime inTime) {
        this.inTime = inTime;
    }
    /**
     * @return outTime (un temps en date heure minute seconde,...)
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }
    /**
     * @param outTime
     */
    public void setOutTime(final LocalDateTime outTime) {
        this.outTime = outTime;
    }
}
