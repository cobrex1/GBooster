/*
 GBooster is a Spigot Plugin providing Global Boosters.
 Copyright (C) 2022  Leopold Meinel

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE.
 */
package com.tamrielnetwork.gbooster.bar;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import com.tamrielnetwork.gbooster.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BoosterBar {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
	private final BossBar bar;

	public BoosterBar() {
		this.bar = Bukkit.createBossBar(getTitle(), BarColor.BLUE, BarStyle.SOLID);

		startTask();
	}

	private void startTask() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

			// Check if there are no active boosters
			if (main.getActiveBoostersManager().getActiveBoosters().size() == 0) {

				if (main.getConfig().getBoolean("empty-bar")) {
					bar.setTitle(Utils.replaceColors(main.getConfig().getString("default-bar-message")));
				} else {
					bar.setVisible(false);
				}

				return;
			}

			double time = main.getActiveBoostersManager().getMostOldBoosterInMinutes() / 60.0;

			if (time > 1.0)
				time = 1;

			bar.setProgress(time);

			bar.setVisible(true);
			bar.setTitle(getTitle());

		}, 0, 20 * 5);
	}

	private String getTitle() {
		return Utils.replaceColors(Objects.requireNonNull(main.getConfig().getString("bar-pattern"))
				.replace("%minecraft%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MINECRAFT, false)) * 100)))
				.replace("%mcmmo%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MCMMO, false)) * 100)))
				.replace("%jobs_xp%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true)) * 100)))
				.replace("%jobs_money%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_MONEY, true)) * 100)))
				.replace("%duration%", String.valueOf(main.getActiveBoostersManager().getMostOldBoosterInMinutes())));
	}

	public BossBar getBar() {
		return bar;
	}

	public void addPlayer(Player player) {
		bar.addPlayer(player);
	}
}
