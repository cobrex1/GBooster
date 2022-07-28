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
 * along with this program. If not, see https://github.com/LeoMeinel/GBooster/blob/main/LICENSE
 */

package com.tamrielnetwork.gbooster.utils.sql;

import com.tamrielnetwork.gbooster.GBooster;
import org.bukkit.plugin.java.JavaPlugin;

public class Sql {

	private static final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	private Sql() {
		throw new IllegalStateException("Utility class");
	}

	public static String getPrefix() {
		return main.getConfig()
		           .getString("mysql.prefix");
	}
}
