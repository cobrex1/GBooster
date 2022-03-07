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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class PlayerStorageYaml extends PlayerStorage {

	private final File playersFile;
	private final FileConfiguration playersConf;

	public PlayerStorageYaml() {

		playersFile = new File(main.getDataFolder(), "players.yml");
		playersConf = YamlConfiguration.loadConfiguration(playersFile);
		save();
	}

	@Override
	public void loadPlayers() {

		for (String key : playersConf.getKeys(false)) {
			boosterPlayers.add(new BoosterPlayer(key, Objects.requireNonNull(playersConf.getConfigurationSection(key))));
		}
	}

	@Override
	public void savePlayers() {

		clear();

		for (BoosterPlayer boosterPlayer : boosterPlayers) {
			playersConf.set(boosterPlayer.getUuid().toString() + ".name", boosterPlayer.getName());

			for (Map.Entry<String, Integer> booster : boosterPlayer.getBoostersStorage().entrySet()) {
				playersConf.set(boosterPlayer.getUuid().toString() + ".boosters." + booster.getKey(), booster.getValue());
			}
		}

		save();
	}

	@Override
	protected void clear() {

		for (String key : playersConf.getKeys(false)) {
			playersConf.set(key, null);
			save();
		}
	}

	public void save() {

		try {
			playersConf.save(playersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}