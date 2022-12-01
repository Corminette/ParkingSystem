package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
/* Classe contenant les différentes méthodes d'interaction. 
 * Entre la base de donnée et le programme dans l'utilisation du ticket. **/

public class TicketDAO {
    /**
     * @see LOGGER
     */
    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");

    /**
     * @see magic number
     */
    public static final int SAVE_TICKET_3 = 3;
    /**
     * @see magic number
     */
    public static final int SAVE_TICKET_4 = 4;
    /**
     * @see magic number
     */
    public static final int SAVE_TICKET_5 = 5;

    /**
     * @see DataBaseConfig dans Config
     */
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * créer le ticket.
     * 
     * @param ticket
     * @return method la sauvegarde du ticket avec les données dessus sinon,
     *         comme boolean: false.
     * @exception ex
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;

        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con
                    .prepareStatement(DBConstants.SAVE_TICKET);

            try {

                ps.setInt(1, ticket.getParkingSpot().getId());
                ps.setString(2, ticket.getVehicleRegNumber());
                ps.setDouble(SAVE_TICKET_3, ticket.getPrice());
                ps.setTimestamp(SAVE_TICKET_4,
                        Timestamp.valueOf(ticket.getInTime()));
                ps.setTimestamp(SAVE_TICKET_5,
                        (ticket.getOutTime() == null)
                                ? null
                                : (Timestamp.valueOf(ticket.getOutTime())));
                return ps.execute();

            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }

        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        }

        return false;

    }

    /**
     * @see magic number
     */
    public static final int GET_TICKET_3 = 3;
    /**
     * @see magic number
     */
    public static final int GET_TICKET_4 = 4;
    /**
     * @see magic number
     */
    public static final int GET_TICKET_5 = 5;
    /**
     * @see magic number il n'y a que 6 places au total, pour info.
     */
    public static final int GET_TICKET_6 = 6;

    /**
     * Donner le ticket au client avec dessus les informations concernant sa
     * place. De parking, type de véhicule, etc.
     * 
     * @param vehicleRegNumber
     * @return ticket
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;

        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);

            try {

                ps.setString(1, vehicleRegNumber);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    ticket = new Ticket();
                    ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                            ParkingType.valueOf(rs.getString(GET_TICKET_6)),
                            false);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setId(rs.getInt(2));
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(rs.getDouble(GET_TICKET_3));
                    ticket.setInTime(rs.getTimestamp(GET_TICKET_4)
                            .toLocalDateTime().truncatedTo(ChronoUnit.SECONDS));
                    ticket.setOutTime((rs.getTimestamp(GET_TICKET_5) == null)
                            ? null
                            : rs.getTimestamp(GET_TICKET_5).toLocalDateTime()
                                    .truncatedTo(ChronoUnit.SECONDS));
                }
                dataBaseConfig.closeResultSet(rs);

            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);

            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        }
        return ticket;
    }

    public static final int UPDATE_TICKET_3 = 3;

    /**
     * Quand le client redonne son ticket et que ça effectue le calcul, etc.
     * Avant de lui rendre avec le montant payé.
     * 
     * @param ticket
     * @return boolean uniquement : true si sortie correcte, false sinon.
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con
                    .prepareStatement(DBConstants.UPDATE_TICKET);
            try {
                ps.setDouble(1, ticket.getPrice());
                ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
                ps.setInt(UPDATE_TICKET_3, ticket.getId());
                ps.execute();
                return true;

            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }

        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        }

        return false;
    }

    /**
     * Méthode pour déterminer qui peut bénéficier de la réduction de 5%. Et
     * comment elle est obtenue.
     * 
     * @see FareCalculatorService
     * @param vehicleRegNumber
     * @return count c-a-d le nombre de passage dans le parking
     */
    public Integer customerLoyalty(final String vehicleRegNumber) {
        Connection con = null;
        Integer count = 0;

        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.GET_COUNT_OF_THE_VEHICLEREGNUMBER);
            try {
                ps.setString(1, vehicleRegNumber);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                dataBaseConfig.closeResultSet(rs);
            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }
        } catch (Exception ex) {
            LOGGER.error("la plaque d'immatriculation n'est pas en mémoire",
                    ex);
        }
        return count;
    }
}
