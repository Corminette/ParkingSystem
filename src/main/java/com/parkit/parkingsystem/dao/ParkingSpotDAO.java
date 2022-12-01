package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

/**
 * classe regroupant les méthodes de gestion des places de parking au niveau de
 * la BDD et qui renvoie les infos
 */
public class ParkingSpotDAO {
    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");

    /**
     * @see DateBaseConfig
     */
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * méthode qui, comme son nom l'indique, va vérifier s'il y a encore une
     * place de parking de libre et donner la première disponible. Voir
     * "ParkingService" pour complément.
     * 
     * @param parkingType
     * @return result
     */
    public int getNextAvailableSlot(final ParkingType parkingType) {
        Connection con = null;
        int result = -1;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con
                    .prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            try {
                ps.setString(1, parkingType.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    result = rs.getInt(1);
                }

                dataBaseConfig.closeResultSet(rs);
            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        }
        return result;
    }

    /**
     * cela revérifie si la place est libre au moment de la sortie du véhicule,
     * met à jour le parking.
     * 
     * @param parkingSpot
     * @return false (boolean) ou le numéro de la place suivante.
     */
    public boolean updateParking(final ParkingSpot parkingSpot) {

        Connection con = null;

        try {

            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con
                    .prepareStatement(DBConstants.UPDATE_PARKING_SPOT);

            try {
                ps.setBoolean(1, parkingSpot.isAvailable());
                ps.setInt(2, parkingSpot.getId());
                int updateRowCount = ps.executeUpdate();
                return (updateRowCount == 1);

            } finally {
                dataBaseConfig.closePreparedStatement(ps);
                dataBaseConfig.closeConnection(con);
            }

        } catch (Exception ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        }
    }
}
