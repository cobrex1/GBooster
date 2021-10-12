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