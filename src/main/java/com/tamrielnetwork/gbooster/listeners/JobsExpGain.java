/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel & contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/LeoMeinel/GBooster/blob/main/LICENSE
 */

package com.tamrielnetwork.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JobsExpGain
		implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onExpGain(@NotNull JobsExpGainEvent event) {
		if (main.getActiveBoostersManager()
		        .getJobsBooster() >= 1) {
			event.setExp((event.getExp() / main.getActiveBoostersManager()
			                                   .getJobsBooster()) * (float) main.getActiveBoostersManager()
			                                                                    .getBoosterMultiplier(
					                                                                    BoosterType.JOBS_XP, true));
		}
		else {
			event.setExp(event.getExp() * (float) main.getActiveBoostersManager()
			                                          .getBoosterMultiplier(BoosterType.JOBS_XP, true));
		}
	}
}
