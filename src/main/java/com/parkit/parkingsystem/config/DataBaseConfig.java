package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConfig {
    /**
     * @see
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    /**
     * @return éléments nécessaires de connexion
     * @throws ClassNotFoundException,
     *             SQLException, IOException
     */
    public Connection getConnection()
            throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        String user = null;
        String password = null;
        String url = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    new File("resources/credentials.properties"));
            try {
                properties.load(fileInputStream);
                user = properties.getProperty("username");
                password = properties.getProperty("password");
                url = properties.getProperty("urlprod");
                LOGGER.info("Create DB connection");
                Class.forName("com.mysql.cj.jdbc.Driver");

            } finally {

                fileInputStream.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("ce n'est pas le bon chemin!");
        }
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * @param con
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * @param ps
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * @param rs
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}
