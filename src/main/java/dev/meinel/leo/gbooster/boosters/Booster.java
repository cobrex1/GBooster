/*
 * File: Booster.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.boosters;

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
        this.boosterType = BoosterType
                .valueOf(Objects.requireNonNull(section.getString("type")).toUpperCase());
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
