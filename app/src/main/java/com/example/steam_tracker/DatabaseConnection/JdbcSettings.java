package com.example.steam_tracker.DatabaseConnection;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcSettings implements Serializable {

    //returns connection
    public Connection getConnectionWithDb(String ip, int port, String login, String password, String dbName) {
        Connection[] connection = {null};
        try {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
                        connection[0] = (Connection) DriverManager.getConnection(connectionString, login, password);
                    } catch (Exception e) {
                    }
                }
            };
            thread.start();
            thread.join(3000);
        } catch (Exception e) {
        }
        return connection[0];
    }
}
