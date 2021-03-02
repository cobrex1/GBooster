package com.tamrielnetwork.gbooster.gbooster.managers;

import com.tamrielnetwork.gbooster.gbooster.GBooster;
import com.tamrielnetwork.gbooster.gbooster.boosters.Booster;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoostersManager {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    private final List<Booster> boosters = new ArrayList<>();

    public void loadBoosters(){
        for (String key : Objects.requireNonNull(main.getConfig().getConfigurationSection("boosters")).getKeys(false)){
            boosters.add(new Booster(key, Objects.requireNonNull(main.getConfig().getConfigurationSection("boosters." + key))));
        }
    }

    public Booster getBoosterById(String id){
        return boosters.stream()
                .filter(booster -> booster.getId().equals(id))
                .findFirst().orElse(null);
    }

    public boolean isBooster(String id){
        return boosters.stream()
                .map(Booster::getId)
                .noneMatch(boosterId -> boosterId.equals(id));
    }

    public List<Booster> getBoosters() {
        return boosters;
    }
}