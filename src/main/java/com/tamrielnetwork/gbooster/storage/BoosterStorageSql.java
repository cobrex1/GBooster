package com.tamrielnetwork.gbooster.storage;

import com.tamrielnetwork.gbooster.boosters.Booster;
import com.tamrielnetwork.gbooster.mysql.SqlManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class BoosterStorageSql extends BoostersStorage {

    @Override
    public void loadBoosters() {
        try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT * FROM Boosters")) {
            try (ResultSet rs = selectStatement.executeQuery()) {
                while (rs.next()) {

                    Booster booster = main.getBoostersManager().getBoosterById(rs.getString(1));

                    if (booster == null)
                        continue;

                    activeBoosters.put(booster, rs.getLong(2));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void saveBoosters() {
        clear();

        for (Map.Entry<Booster, Long> entry : activeBoosters.entries()) {
            try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement("INSERT INTO Boosters (ID, Time) VALUES (?, ?)")) {
                insertStatement.setString(1, entry.getKey().getId());
                insertStatement.setLong(2, entry.getValue());
                insertStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void clear() {
        try (PreparedStatement truncateStatement = SqlManager.getConnection().prepareStatement("TRUNCATE TABLE Boosters")) {
            truncateStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
