package com.example.steam_tracker.Settings;

import com.example.steam_tracker.DatabaseConnection.JdbcSettings;

import java.sql.Connection;

public class AppSettings extends JdbcSettings {

    JdbcSettings jdbcSettingsController = new JdbcSettings();

    private String ipAddress;
    private int port;
    private String dbName;
    private String dbLogin;
    private String dbPassword;
    private Connection dbConnection;

    static AppSettings currentAppSettings = null;

    public AppSettings(String ipAddress,int port,String dbName,String dbLogin,String dbPassword,Connection dbConnection)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.dbName = dbName;
        this.dbLogin =dbLogin;
        this.dbPassword = dbPassword;
        this.dbConnection = dbConnection;
    }

    public AppSettings()
    {

    }

    public String getIpAddress() {
        return currentAppSettings.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        currentAppSettings.ipAddress = ipAddress;
    }

    public int getPort() {
        return currentAppSettings.port;
    }

    public void setPort(int port) {
        currentAppSettings.port = port;
    }

    public String getDbName() {
        return currentAppSettings.dbName;
    }

    public void setDbName(String dbName) {
        currentAppSettings.dbName = dbName;
    }

    public String getDbLogin() {
        return currentAppSettings.dbLogin;
    }

    public void setDbLogin(String dbLogin) {
        currentAppSettings.dbLogin = dbLogin;
    }

    public String getDbPassword() {
        return currentAppSettings.dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        currentAppSettings.dbPassword = dbPassword;
    }

    public Connection getDbConnection() {
        return currentAppSettings.dbConnection;
    }

    public void setDbConnection(Connection dbConnection) {
        currentAppSettings.dbConnection = dbConnection;
    }

    public void setCurrentAppSettings(AppSettings newSettings)
    {
        currentAppSettings = newSettings;
        currentAppSettings.setDbConnection(jdbcSettingsController.getConnectionWithDb(
                currentAppSettings.getIpAddress(),
                currentAppSettings.getPort(),
                currentAppSettings.getDbLogin(),
                currentAppSettings.getDbPassword(),
                currentAppSettings.getDbName()));
    }

    public AppSettings getCurrentAppSettings()
    {
        if(currentAppSettings == null)
        {
            currentAppSettings = new AppSettings("",0,"","","",null);
        }
        return currentAppSettings;
    }
}

