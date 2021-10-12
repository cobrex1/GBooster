package com.tamrielnetwork.gbooster.mysql;

import com.tamrielnetwork.gbooster.GBooster;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlManager {

    private static Connection connection;
    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    private final int port;
    private final String host, database, username, password;

    public SqlManager() {
        this.host = main.getConfig().getString("mysql.host");
        this.port = main.getConfig().getInt("mysql.port");
        this.database = main.getConfig().getString("mysql.database");
        this.username = main.getConfig().getString("mysql.username");
        this.password = main.getConfig().getString("mysql.password");

        enableConnection();

        try {
            PreparedStatement statementPlayersTable = SqlManager.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS PlayersBoosters (`UUID` TEXT, `Name` TEXT, `Booster` TEXT, `Value` INT)");
            PreparedStatement statementBoostersTable = SqlManager.getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS Boosters (`ID` TEXT, `Time` BIGINT)");
            statementPlayersTable.executeUpdate();
            statementBoostersTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    private static void setConnection(Connection connection) {
        SqlManager.connection = connection;
    }

    private void enableConnection() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return;
            }

            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));

            main.getLogger().info("Connected successfully with the database!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}