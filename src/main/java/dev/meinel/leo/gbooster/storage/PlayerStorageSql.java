/*
 * File: PlayerStorageSql.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.player.BoosterPlayer;
import dev.meinel.leo.gbooster.storage.mysql.SqlManager;
import dev.meinel.leo.gbooster.utils.sql.Sql;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PlayerStorageSql extends PlayerStorage {

    private static final String SQLEXCEPTION =
            "GBooster encountered an SQLException while executing task";

    public PlayerStorageSql() {
        new SqlManager();
    }

    @Override
    public void loadPlayers() {
        try (PreparedStatement selectStatement =
                SqlManager.getConnection().prepareStatement("SELECT * FROM ?PlayersBoosters")) {
            selectStatement.setString(1, Sql.getPrefix());
            selectStatement.executeUpdate();
            try (ResultSet rs = selectStatement.executeQuery()) {
                while (rs.next()) {
                    manageStorage(rs);
                }
            }
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }

    private void manageStorage(ResultSet rs) throws SQLException {
        if (main.getBoostersManager().isBooster(rs.getString(3))) {
            return;
        }
        BoosterPlayer boosterPlayer = getBoosterPlayerByUUID(UUID.fromString(rs.getString(1)));
        if (boosterPlayer == null) {
            boosterPlayers.add(new BoosterPlayer(rs));
            return;
        }
        boosterPlayer.getBoostersStorage().put(rs.getString(3), rs.getInt(4));
    }

    @Override
    public void savePlayers() {
        clear();
        for (BoosterPlayer boosterPlayer : boosterPlayers) {
            for (Map.Entry<String, Integer> boosterEntry : boosterPlayer.getBoostersStorage()
                    .entrySet()) {
                try (PreparedStatement insertStatement =
                        SqlManager.getConnection().prepareStatement(
                                "INSERT INTO ?PlayersBoosters (UUID, Name, Booster, Value) VALUES (?, ?, ?, ?)")) {
                    insertStatement.setString(1, Sql.getPrefix());
                    insertStatement.setString(2, boosterPlayer.getUuid().toString());
                    insertStatement.setString(3, boosterPlayer.getName());
                    insertStatement.setString(4, boosterEntry.getKey());
                    insertStatement.setInt(5, boosterEntry.getValue());
                    insertStatement.executeUpdate();
                } catch (SQLException ignored) {
                    Bukkit.getLogger().warning(SQLEXCEPTION);
                }
            }
        }
    }

    @Override
    protected void clear() {
        try (PreparedStatement truncateStatement =
                SqlManager.getConnection().prepareStatement("TRUNCATE TABLE ?PlayersBoosters")) {
            truncateStatement.setString(1, Sql.getPrefix());
            truncateStatement.executeUpdate();
        } catch (SQLException ignored) {
            Bukkit.getLogger().warning(SQLEXCEPTION);
        }
    }
}
