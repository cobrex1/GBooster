/*
 * File: PlayerStorageYaml.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.player.BoosterPlayer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class PlayerStorageYaml
		extends PlayerStorage {

	private static final String IOEXCEPTION = "GBooster encountered an IOException while executing task";
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
			boosterPlayers.add(
					new BoosterPlayer(key, Objects.requireNonNull(playersConf.getConfigurationSection(key))));
		}
	}

	@Override
	public void savePlayers() {
		clear();
		for (BoosterPlayer boosterPlayer : boosterPlayers) {
			playersConf.set(boosterPlayer.getUuid()
					.toString() + ".name", boosterPlayer.getName());
			for (Map.Entry<String, Integer> booster : boosterPlayer.getBoostersStorage()
					.entrySet()) {
				playersConf.set(boosterPlayer.getUuid()
						.toString() + ".boosters." + booster.getKey(), booster.getValue());
			}
		}
		save();
	}

	@Override
	protected void clear() {
		for (String key : playersConf.getKeys(false)) {
			playersConf.set(key, null);
		}
	}

	private void save() {
		try {
			playersConf.save(playersFile);
		} catch (IOException ignored) {
			Bukkit.getLogger()
					.info(IOEXCEPTION);
		}
	}
}