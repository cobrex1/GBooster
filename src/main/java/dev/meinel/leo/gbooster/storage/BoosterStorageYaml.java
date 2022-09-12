/*
 * File: BoosterStorageYaml.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.boosters.Booster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class BoosterStorageYaml
        extends BoostersStorage {

    private static final String IOEXCEPTION = "GBooster encountered an IOException while executing task";
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
            Booster booster = main.getBoostersManager()
                    .getBoosterById(Objects.requireNonNull(boostersConf.getString(key + ".id")));
            if (booster == null) {
                continue;
            }
            this.activeBoosters.put(booster, boostersConf.getLong(key + ".time"));
        }
        clear();
    }

    @Override
    public void saveBoosters() {
        clear();
        int counter = 1;
        for (Map.Entry<Booster, Long> entry : this.activeBoosters.entries()) {
            boostersConf.set(counter + ".id", entry.getKey()
                    .getId());
            boostersConf.set(counter + ".time", entry.getValue());
            counter++;
        }
        save();
    }

    @Override
    protected void clear() {
        for (String key : boostersConf.getKeys(false)) {
            boostersConf.set(key, null);
        }
    }

    private void save() {
        try {
            boostersConf.save(boostersFile);
        } catch (IOException ignored) {
            Bukkit.getLogger()
                    .info(IOEXCEPTION);
        }
    }
}