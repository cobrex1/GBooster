/*
 * File: BoosterStorageSql.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.boosters.Booster;
import dev.meinel.leo.gbooster.storage.mysql.SqlManager;
import dev.meinel.leo.gbooster.utils.sql.Sql;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class BoosterStorageSql extends BoostersStorage {

    private static final String SQLEXCEPTION =
            "GBooster encountered an SQLException while executing task";

    @Override
    public void loadBoosters() {
        try (PreparedStatement selectStatement = SqlManager.getConnection()
                .prepareStatement("SELECT * FROM " + Sql.getPrefix() + "Boosters")) {
            try (ResultSet rs = selectStatement.executeQuery()) {
                while (rs.next()) {
                    Booster booster = main.getBoostersManager().getBoosterById(rs.getString(1));
                    if (booster == null) {
                        continue;
                    }
                    activeBoosters.put(booster, rs.getLong(2));
                }
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }

    @Override
    public void saveBoosters() {
        clear();
        for (Map.Entry<Booster, Long> entry : activeBoosters.entries()) {
            try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement(
                    "INSERT INTO " + Sql.getPrefix() + "Boosters (ID, Time) VALUES (?, ?)")) {
                insertStatement.setString(1, entry.getKey().getId());
                insertStatement.setLong(2, entry.getValue());
                insertStatement.executeUpdate();
            } catch (SQLException ignored) {
                Bukkit.getLogger().warning(SQLEXCEPTION);
            }
        }
    }

    @Override
    protected void clear() {
        try (PreparedStatement truncateStatement = SqlManager.getConnection()
                .prepareStatement("TRUNCATE TABLE " + Sql.getPrefix() + "Boosters")) {
            truncateStatement.executeUpdate();
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }
}
