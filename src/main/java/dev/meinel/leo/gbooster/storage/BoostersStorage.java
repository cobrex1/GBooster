/*
 * File: BoostersStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.Booster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class BoostersStorage {

    protected final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    protected final Multimap<Booster, Long> activeBoosters = ArrayListMultimap.create();
    private double jobsBooster = 0;

    public abstract void loadBoosters();

    public abstract void saveBoosters();

    protected abstract void clear();

    public void activateBooster(@NotNull Booster booster) {
        activeBoosters.put(booster, System.currentTimeMillis());
    }

    public double getBoosterMultiplier(@NotNull BoosterType boosterType, boolean addJobsBooster) {
        double totalMultiplier = 0;
        if (addJobsBooster) {
            totalMultiplier += jobsBooster;
        }
        for (Booster booster : activeBoosters.keys()) {
            if (booster.getBoosterType() == boosterType) {
                totalMultiplier += booster.getMultiplier();
            }
        }
        return totalMultiplier == 0
                ? 1
                : totalMultiplier;
    }

    public boolean canUseBooster(@NotNull Booster booster) {
        double totalAmount = getBoosterMultiplier(booster.getBoosterType(),
                booster.getBoosterType() == BoosterType.JOBS_MONEY
                        || booster.getBoosterType() == BoosterType.JOBS_XP);
        return totalAmount + booster.getMultiplier() <= 8;
    }

    public int getMostOldBoosterInMinutes() {
        long mostOldBoosterCountdown = Long.MAX_VALUE;
        for (Map.Entry<Booster, Long> entry : ArrayListMultimap.create(activeBoosters)
                .entries()) {
            long boosterCountdown = entry.getKey()
                    .getDuration() * 1000L + entry.getValue();
            if (boosterCountdown <= System.currentTimeMillis()) {
                activeBoosters.remove(entry.getKey(), entry.getValue());
                continue;
            }
            if (mostOldBoosterCountdown > boosterCountdown) {
                mostOldBoosterCountdown = boosterCountdown;
            }
        }
        if (mostOldBoosterCountdown == Long.MAX_VALUE) {
            return 0;
        }
        return ((int) ((mostOldBoosterCountdown - System.currentTimeMillis()) / 1000 / 60)) + 1;
    }

    public Multimap<Booster, Long> getActiveBoosters() {
        return activeBoosters;
    }

    public void addJobsBooster(double jobsBooster) {
        this.jobsBooster += jobsBooster;
    }

    public double getJobsBooster() {
        return jobsBooster;
    }
}