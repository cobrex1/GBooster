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
 * along with this program. If not, see https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE
 */

package com.tamrielnetwork.gbooster.bar;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import com.tamrielnetwork.gbooster.utils.Chat;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

					String defaultBarMessage = main.getConfig().getString("default-bar-message");
					bar.setTitle(Chat.replaceColors(Objects.requireNonNull(defaultBarMessage)));

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

		}, 0, 20 * 5L);
	}

	private String getTitle() {
		String title = main.getConfig().getString("bar-pattern");

		title = Chat.replaceColors(Objects.requireNonNull(main.getConfig().getString("bar-pattern"))
				.replace("%minecraft%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MINECRAFT, false)) * 100)))
				.replace("%mcmmo%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MCMMO, false)) * 100)))
				.replace("%jobs_xp%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true)) * 100)))
				.replace("%jobs_money%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_MONEY, true)) * 100)))
				.replace("%duration%", String.valueOf(main.getActiveBoostersManager().getMostOldBoosterInMinutes())));

		if (bar != null) {
			if(bar.getPlayers().size() > 0) {
				Player somePlayer = bar.getPlayers().get(0);
				if (somePlayer != null) {
					title = PlaceholderAPI.setPlaceholders(somePlayer, title);
				}
			}
		}

		return title;
	}

	public BossBar getBar() {

		return bar;
	}

	public void addPlayer(@NotNull Player player) {

		bar.addPlayer(player);
	}

}
