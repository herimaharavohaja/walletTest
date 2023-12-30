package com.wallet.Haja.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectiondb {

    public static Connection getConnection() {
        String url ="jdbc:postgresql://localhost:5432/wallet";
        String user = "postgres";
        String password = "fitiavana";

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
