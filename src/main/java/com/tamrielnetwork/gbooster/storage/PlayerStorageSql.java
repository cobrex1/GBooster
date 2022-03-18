/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel & contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE
 */

package com.tamrielnetwork.gbooster.storage;

import com.tamrielnetwork.gbooster.player.BoosterPlayer;
import com.tamrielnetwork.gbooster.storage.mysql.SqlManager;
import com.tamrielnetwork.gbooster.utils.sql.Sql;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class PlayerStorageSql
		extends PlayerStorage {

	private static final String SQLEXCEPTION = "GBooster encountered an SQLException while executing task";

	public PlayerStorageSql() {
		new SqlManager();
	}

	@Override
	public void loadPlayers() {
		try (PreparedStatement selectStatement = SqlManager.getConnection()
		                                                   .prepareStatement("SELECT * FROM " + Sql.getPrefix()
		                                                                     + "PlayersBoosters")) {
			try (ResultSet rs = selectStatement.executeQuery()) {
				while (rs.next()) {
					manageStorage(rs);
				}
			}
		}
		catch (SQLException ignored) {
			Bukkit.getLogger()
			      .warning(SQLEXCEPTION);
		}
	}

	private void manageStorage(ResultSet rs) throws SQLException {
		if (main.getBoostersManager()
		        .isBooster(rs.getString(3))) {
			return;
		}
		BoosterPlayer boosterPlayer = getBoosterPlayerByUUID(UUID.fromString(rs.getString(1)));
		if (boosterPlayer == null) {
			boosterPlayers.add(new BoosterPlayer(rs));
			return;
		}
		boosterPlayer.getBoostersStorage()
		             .put(rs.getString(3), rs.getInt(4));
	}

	@Override
	public void savePlayers() {
		clear();
		for (BoosterPlayer boosterPlayer : boosterPlayers) {
			for (Map.Entry<String, Integer> boosterEntry : boosterPlayer.getBoostersStorage()
			                                                            .entrySet()) {
				try (PreparedStatement insertStatement = SqlManager.getConnection()
				                                                   .prepareStatement("INSERT INTO " + Sql.getPrefix()
				                                                                     + "PlayersBoosters (UUID, Name, Booster, Value) VALUES (?, ?, ?, ?)")) {
					insertStatement.setString(1, boosterPlayer.getUuid()
					                                          .toString());
					insertStatement.setString(2, boosterPlayer.getName());
					insertStatement.setString(3, boosterEntry.getKey());
					insertStatement.setInt(4, boosterEntry.getValue());
					insertStatement.executeUpdate();
				}
				catch (SQLException ignored) {
					Bukkit.getLogger()
					      .warning(SQLEXCEPTION);
				}
			}
		}
	}

	@Override
	protected void clear() {
		try (PreparedStatement truncateStatement = SqlManager.getConnection()
		                                                     .prepareStatement("TRUNCATE TABLE " + Sql.getPrefix()
		                                                                       + "PlayersBoosters")) {
			truncateStatement.executeUpdate();
		}
		catch (SQLException ignored) {
			Bukkit.getLogger()
			      .warning(SQLEXCEPTION);
		}
	}
}
