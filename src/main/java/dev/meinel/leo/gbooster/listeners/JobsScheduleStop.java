/*
 * File: JobsScheduleStop.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsScheduleStopEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import dev.meinel.leo.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JobsScheduleStop
		implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onBoosterStop(@NotNull JobsScheduleStopEvent event) {
		main.getActiveBoostersManager()
				.addJobsBooster(-(event.getSchedule()
						.getBoost(CurrencyType.EXP) + 1));
	}
}