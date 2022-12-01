package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Ensemble des méthodes nécessaires pour le ticket. Afin d'identifier les
 * places disponibles. Ainsi que le type de véhicule, etc.. Créer les objets et
 * retourner les informations nécessaires.
 */
public class ParkingSpot implements Cloneable {
    /**
     * @see c'est le numéro de place de parking
     */
    private int number;
    /**
     * C'est le type de véhicule (demandé au moment d'entrée).
     * 
     * @see constants.
     */
    private ParkingType parkingType;
    /**
     * @see Vérification qu'une place est disponible, pas complet.
     */
    private boolean isAvailable;
    /**
     * ici nous avons donc le parking avec le numéro de la place où garer la
     * voiture, si c'est une voiture ou un vélo, et si une place est
     * effectivement disponible dans le parking.
     * 
     * @see Ticket
     * @return null
     * @exception CloneNotSuuportedException
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    /**
     * (Constructeur définissant le parking et place de parking).
     * 
     * @param number
     * @param parkingType
     * @param isAvailable
     */
    public ParkingSpot(final int number, final ParkingType parkingType,
            final boolean isAvailable) {
        this.number = number;
        this.parkingType = parkingType;
        this.isAvailable = isAvailable;
    }

    /**
     * @return le chiffre d'identité du ticket
     */
    public int getId() {
        return number;
    }

    /**
     * @param number
     */
    public void setId(int number) {
        this.number = number;
    }

    /**
     * @return le type de véhicule (constante)
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * @param parkingType
     */
    public void setParkingType(final ParkingType parkingType) {
        this.parkingType = parkingType;
    }

    /**
     * @return un boolean et si il est possible ou non de se garer
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * @param available
     *            boolean réponse positive
     */
    public void setAvailable(final boolean available) {
        isAvailable = available;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }
    /**
     * {@link}https://fr.wikipedia.org/wiki/Java_hashCode()
     */
    @Override
    public int hashCode() {
        return number;
    }
}
