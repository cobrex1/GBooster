/*
 * File: JobsExpGain.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JobsExpGain implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onExpGain(@NotNull JobsExpGainEvent event) {
        if (main.getActiveBoostersManager().getJobsBooster() >= 1) {
            event.setExp((event.getExp() / main.getActiveBoostersManager().getJobsBooster())
                    * (float) main.getActiveBoostersManager()
                            .getBoosterMultiplier(BoosterType.JOBS_XP, true));
        } else {
            event.setExp(event.getExp() * (float) main.getActiveBoostersManager()
                    .getBoosterMultiplier(BoosterType.JOBS_XP, true));
        }
    }
}
