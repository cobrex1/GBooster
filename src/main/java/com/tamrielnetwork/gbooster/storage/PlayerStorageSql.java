/*
 Copyright (C) 2022  Leopold Meinel
 Visit https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE for more details.
 */
package com.tamrielnetwork.gbooster.storage;

import com.tamrielnetwork.gbooster.player.BoosterPlayer;
import com.tamrielnetwork.gbooster.storage.mysql.SqlManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PlayerStorageSql extends PlayerStorage {

	public PlayerStorageSql() {
		new SqlManager();
	}

	@Override
	public void loadPlayers() {
		try (PreparedStatement selectStatement = SqlManager.getConnection().prepareStatement("SELECT * FROM PlayersBoosters")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {

					if (main.getBoostersManager().isBooster(rs.getString(3)))
						continue;

					BoosterPlayer boosterPlayer = getBoosterPlayerByUUID(UUID.fromString(rs.getString(1)));

					if (boosterPlayer == null) {
						boosterPlayers.add(new BoosterPlayer(rs));
						continue;
					}

					boosterPlayer.getBoostersStorage().put(rs.getString(3), rs.getInt(4));
				}
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	@Override
	public void savePlayers() {
		clear();

		for (BoosterPlayer boosterPlayer : boosterPlayers) {
			for (Map.Entry<String, Integer> boosterEntry : boosterPlayer.getBoostersStorage().entrySet()) {

				try (PreparedStatement insertStatement = SqlManager.getConnection().prepareStatement("INSERT INTO PlayersBoosters (UUID, Name, Booster, Value) VALUES (?, ?, ?, ?)")) {
					insertStatement.setString(1, boosterPlayer.getUuid().toString());
					insertStatement.setString(2, boosterPlayer.getName());
					insertStatement.setString(3, boosterEntry.getKey());
					insertStatement.setInt(4, boosterEntry.getValue());
					insertStatement.executeUpdate();
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void clear() {
		try (PreparedStatement truncateStatement = SqlManager.getConnection().prepareStatement("TRUNCATE TABLE PlayersBoosters")) {
			truncateStatement.executeUpdate();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}
}
