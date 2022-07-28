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

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.player.BoosterPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PlayerStorage {

	protected final GBooster main = JavaPlugin.getPlugin(GBooster.class);
	protected final List<BoosterPlayer> boosterPlayers = new ArrayList<>();

	public abstract void loadPlayers();

	public abstract void savePlayers();

	protected abstract void clear();

	public BoosterPlayer getBoosterPlayerByUUID(@NotNull UUID uuid) {
		return boosterPlayers.stream()
		                     .filter(boosterPlayer -> boosterPlayer.getUuid()
		                                                           .equals(uuid))
		                     .findFirst()
		                     .orElse(null);
	}

	public BoosterPlayer getBoosterPlayerByName(@NotNull String name) {
		return boosterPlayers.stream()
		                     .filter(boosterPlayer -> boosterPlayer.getName()
		                                                           .equalsIgnoreCase(name))
		                     .findFirst()
		                     .orElse(null);
	}

	public void registerPlayer(@NotNull UUID uuid, @NotNull String name) {
		boosterPlayers.add(new BoosterPlayer(uuid, name));
	}

	public boolean isPlayerRegistered(@NotNull UUID uuid) {
		return boosterPlayers.stream()
		                     .anyMatch(player -> player.getUuid()
		                                               .equals(uuid));
	}
}