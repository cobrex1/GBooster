/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel
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

import com.tamrielnetwork.gbooster.boosters.Booster;
import com.tamrielnetwork.gbooster.storage.mysql.SqlManager;

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
