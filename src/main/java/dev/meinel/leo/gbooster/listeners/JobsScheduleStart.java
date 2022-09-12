/*
 * File: JobsScheduleStart.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsScheduleStartEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import dev.meinel.leo.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JobsScheduleStart
        implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onBoosterStart(@NotNull JobsScheduleStartEvent event) {
        main.getActiveBoostersManager()
                .addJobsBooster(event.getSchedule()
                        .getBoost()
                        .get(CurrencyType.EXP) + 1);
    }
}