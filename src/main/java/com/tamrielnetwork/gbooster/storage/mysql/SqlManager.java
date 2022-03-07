/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel & Contributors
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

package com.tamrielnetwork.gbooster.storage.mysql;

import com.tamrielnetwork.gbooster.GBooster;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

	private static void setConnection(@NotNull Connection connection) {

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