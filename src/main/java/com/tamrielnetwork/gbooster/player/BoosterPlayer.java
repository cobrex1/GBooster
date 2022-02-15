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

package com.tamrielnetwork.gbooster.player;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.Booster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BoosterPlayer {

	private final String name;
	private final UUID uuid;
	private final Map<String, Integer> boostersStorage = new HashMap<>();
	private final Map<BoosterType, Long> boostersCountdown = new HashMap<>();

	public BoosterPlayer(UUID uuid, String name) {
		this.name = name;
		this.uuid = uuid;
	}

	public BoosterPlayer(ResultSet resultSet) throws SQLException {

		this.name = resultSet.getString(2);
		this.uuid = UUID.fromString(resultSet.getString(1));

		boostersStorage.put(resultSet.getString(3), resultSet.getInt(4));
	}

	public BoosterPlayer(String key, ConfigurationSection section) {
		this.uuid = UUID.fromString(key);
		this.name = section.getString("name");

		if (!section.isConfigurationSection("boosters"))
			return;

		for (String boosterId : Objects.requireNonNull(section.getConfigurationSection("boosters")).getKeys(false)) {
			if (JavaPlugin.getPlugin(GBooster.class).getBoostersManager().isBooster(boosterId))
				continue;

			this.boostersStorage.put(boosterId, section.getInt("boosters." + boosterId));
		}
	}

	public void addBooster(String boosterId, int amount) {
		int previousAmount = boostersStorage.getOrDefault(boosterId, 0);

		boostersStorage.put(boosterId, amount + previousAmount);
	}

	public boolean takeBooster(Booster booster) {
		Integer amount = boostersStorage.get(booster.getId());

		if (amount == null || amount == 0)
			return false;

		boostersStorage.put(booster.getId(), amount - 1);

		return true;
	}

	public boolean canUseBooster(Booster booster) {
		if (boostersCountdown.containsKey(booster.getBoosterType()) && boostersCountdown.get(booster.getBoosterType()) + 60 * 60 * 1000 > System.currentTimeMillis()) {
			return false;
		}

		boostersCountdown.put(booster.getBoosterType(), System.currentTimeMillis());

		return true;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public Map<String, Integer> getBoostersStorage() {
		return boostersStorage;
	}
}