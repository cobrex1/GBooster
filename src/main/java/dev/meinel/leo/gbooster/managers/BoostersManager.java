/*
 * File: BoostersManager.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.managers;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.Booster;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoostersManager {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    private final List<Booster> boosters = new ArrayList<>();

    public void loadBoosters() {
        for (String key : Objects
                .requireNonNull(main.getConfig().getConfigurationSection("boosters"))
                .getKeys(false)) {
            boosters.add(new Booster(key, Objects
                    .requireNonNull(main.getConfig().getConfigurationSection("boosters." + key))));
        }
    }

    public Booster getBoosterById(@NotNull String id) {
        return boosters.stream().filter(booster -> booster.getId().equals(id)).findFirst()
                .orElse(null);
    }

    public boolean isBooster(@NotNull String id) {
        return boosters.stream().map(Booster::getId).noneMatch(boosterId -> boosterId.equals(id));
    }

    public List<Booster> getBoosters() {
        return boosters;
    }
}
