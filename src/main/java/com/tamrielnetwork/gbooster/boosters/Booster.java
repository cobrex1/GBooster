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

package com.tamrielnetwork.gbooster.boosters;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Booster {

	private final String id;
	private final BoosterType boosterType;
	private final double multiplier;
	private final int duration;

	public Booster(@NotNull String id, @NotNull ConfigurationSection section) {

		this.id = id;
		this.boosterType = BoosterType.valueOf(Objects.requireNonNull(section.getString("type")).toUpperCase());
		this.multiplier = section.getDouble("multiplier");
		this.duration = section.getInt("duration");
	}

	public String getId() {

		return id;
	}

	public BoosterType getBoosterType() {

		return boosterType;
	}

	public double getMultiplier() {

		return multiplier;
	}

	public int getDuration() {

		return duration;
	}

}
