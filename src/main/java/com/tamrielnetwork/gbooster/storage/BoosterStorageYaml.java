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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class BoosterStorageYaml extends BoostersStorage {

	private final File boostersFile;
	private final FileConfiguration boostersConf;

	public BoosterStorageYaml() {
		boostersFile = new File(main.getDataFolder(), "boosters.yml");
		boostersConf = YamlConfiguration.loadConfiguration(boostersFile);
		save();
	}

	@Override
	public void loadBoosters() {
		for (String key : boostersConf.getKeys(false)) {
			Booster booster = main.getBoostersManager().getBoosterById(boostersConf.getString(key + ".id"));

			if (booster == null) continue;

			this.activeBoosters.put(booster, boostersConf.getLong(key + ".time"));
		}

		clear();
	}

	@Override
	public void saveBoosters() {

		clear();

		int counter = 1;

		for (Map.Entry<Booster, Long> entry : this.activeBoosters.entries()) {

			boostersConf.set(counter + ".id", entry.getKey().getId());
			boostersConf.set(counter + ".time", entry.getValue());

			counter++;
		}

		save();
	}

	@Override
	public void clear() {
		for (String key : boostersConf.getKeys(false)) {
			boostersConf.set(key, null);
		}
	}

	public void save() {
		try {
			boostersConf.save(boostersFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}