package com.parkit.parkingsystem.constants;

/* Transcription en SQL de certaines méthodes attendues dans la base de données */

public final class DBConstants {
    private DBConstants() {
        throw new AssertionError("Instantiating utility class");
    }

    /**
     * @apiNote dit de choisir le prochain numéro de place de libre et type de
     *          véhicule à SQL.
     */
    public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
    /**
     * @apiNote My SQL doit communiquer alors le numéro de la place autorisé.
     */
    public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
    /**
     * @apiNote SQL: faire indiquer sur le ticketDAO tous les paramètres.
     */
    public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    /**
     * @apiNote SQL interrogé pour communiquer et sortir le prix et moment de
     *          sortie.
     */
    public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    /**
     * @apiNote SQL une fois récupéré les données doit les transmettre pour que
     *          inscrite sur le ticket.
     */
    public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME desc limit 1";
    /**
     * @apiNote demande à SQL de retrouver à partir de la plaque
     *          d'immatriculation le compte/nombre de passage dans le parking.
     */
    public static final String GET_COUNT_OF_THE_VEHICLEREGNUMBER = "SELECT Count(*) FROM prod.ticket where VEHICLE_REG_NUMBER = ?";
}
