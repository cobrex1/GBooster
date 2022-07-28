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

package com.tamrielnetwork.gbooster.player;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.Booster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BoosterPlayer {

	private final String name;
	private final UUID uuid;
	private final Map<String, Integer> boostersStorage = new HashMap<>();
	private final EnumMap<BoosterType, Long> boostersCountdown = new EnumMap<>(BoosterType.class);

	public BoosterPlayer(@NotNull UUID uuid, @NotNull String name) {
		this.name = name;
		this.uuid = uuid;
	}

	public BoosterPlayer(@NotNull ResultSet resultSet) throws SQLException {
		this.name = resultSet.getString(2);
		this.uuid = UUID.fromString(resultSet.getString(1));
		boostersStorage.put(resultSet.getString(3), resultSet.getInt(4));
	}

	public BoosterPlayer(@NotNull String key, @NotNull ConfigurationSection section) {
		this.uuid = UUID.fromString(key);
		this.name = section.getString("name");
		if (!section.isConfigurationSection("boosters")) {
			return;
		}
		for (String boosterId : Objects.requireNonNull(section.getConfigurationSection("boosters"))
		                               .getKeys(false)) {
			if (JavaPlugin.getPlugin(GBooster.class)
			              .getBoostersManager()
			              .isBooster(boosterId)) {
				continue;
			}
			this.boostersStorage.put(boosterId, section.getInt("boosters." + boosterId));
		}
	}

	public void addBooster(@NotNull String boosterId, int amount) {
		int previousAmount = boostersStorage.getOrDefault(boosterId, 0);
		boostersStorage.put(boosterId, amount + previousAmount);
	}

	public void takeBooster(@NotNull Booster booster) {
		Integer amount = boostersStorage.get(booster.getId());
		boostersStorage.put(booster.getId(), amount - 1);
	}

	public boolean canUseBooster(@NotNull Booster booster) {
		if (boostersCountdown.containsKey(booster.getBoosterType())
		    && boostersCountdown.get(booster.getBoosterType()) + booster.getDuration() * 1000L
		       > System.currentTimeMillis()) {
			return false;
		}
		boostersCountdown.put(booster.getBoosterType(), System.currentTimeMillis());
		return true;
	}

	public boolean hasBooster(@NotNull Booster booster) {
		Integer amount = boostersStorage.get(booster.getId());
		return amount != null && amount != 0;
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