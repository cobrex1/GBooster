/*
 * File: BoosterPlayer.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.player;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.Booster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
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
            if (JavaPlugin.getPlugin(GBooster.class).getBoostersManager().isBooster(boosterId)) {
                continue;
            }
            this.boostersStorage.put(boosterId, section.getInt("boosters." + boosterId));
        }
    }

    public void addBooster(@NotNull String boosterId, int amount) {
        int previousAmount = boostersStorage.getOrDefault(boosterId, 0);
        boostersStorage.put(boosterId, amount + previousAmount);
    }

    public boolean takeBooster(@NotNull String boosterId, int amount) {
        if (!this.hasBooster(boosterId)) {
            return false;
        }
        int previousAmount = boostersStorage.get(boosterId);
        boostersStorage.put(boosterId, previousAmount - amount);
        return true;
    }

    public boolean canUseBooster(@NotNull Booster booster) {
        if (boostersCountdown.containsKey(booster.getBoosterType())
                && boostersCountdown.get(booster.getBoosterType())
                        + booster.getDuration() * 1000L > System.currentTimeMillis()) {
            return false;
        }
        boostersCountdown.put(booster.getBoosterType(), System.currentTimeMillis());
        return true;
    }

    public boolean hasBooster(@NotNull String boosterId) {
        Integer amount = boostersStorage.get(boosterId);
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
