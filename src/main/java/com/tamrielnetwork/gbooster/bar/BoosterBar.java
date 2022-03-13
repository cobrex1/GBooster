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

	private static final String DURATION = "duration";
	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
	private final BossBar bar;

	public BoosterBar() {
		this.bar = Bukkit.createBossBar(getTitle(), BarColor.BLUE, BarStyle.SOLID);
		startTask();
	}

	private void startTask() {
		Bukkit.getScheduler()
		      .scheduleSyncRepeatingTask(main, () -> {
			      if (main.getActiveBoostersManager()
			              .getActiveBoosters()
			              .size() == 0) {
				      bar.setVisible(false);
			      }
			      double time = main.getActiveBoostersManager()
			                        .getMostOldBoosterInMinutes() / 60.0;
			      if (time > 1.0) {
				      time = 1;
			      }
			      bar.setProgress(time);
			      bar.setVisible(true);
			      bar.setTitle(getTitle());
		      }, 0, 20 * 5L);
	}

	private String getTitle() {
		int minecraftValue = (int) main.getActiveBoostersManager()
		                               .getBoosterMultiplier(BoosterType.MINECRAFT, false);
		int mcmmoValue = (int) main.getActiveBoostersManager()
		                           .getBoosterMultiplier(BoosterType.MCMMO, false);
		int jobsXpValue = (int) main.getActiveBoostersManager()
		                            .getBoosterMultiplier(BoosterType.JOBS_XP, true);
		int jobsMoneyValue = (int) main.getActiveBoostersManager()
		                               .getBoosterMultiplier(BoosterType.JOBS_MONEY, true);
		int durationValue = main.getActiveBoostersManager()
		                        .getMostOldBoosterInMinutes();
		StringBuilder titleBuilder = new StringBuilder();
		appendTitleBuilderIfNecessary(titleBuilder, "minecraft", minecraftValue);
		appendTitleBuilderIfNecessary(titleBuilder, "mcmmo", mcmmoValue);
		appendTitleBuilderIfNecessary(titleBuilder, "jobs-xp", jobsXpValue);
		appendTitleBuilderIfNecessary(titleBuilder, "jobs-money", jobsMoneyValue);
		appendTitleBuilderIfNecessary(titleBuilder, DURATION, durationValue);
		String title = Chat.replaceColors(Objects.requireNonNull(titleBuilder.toString()));
		title = title.replace("%minecraft%", String.valueOf(minecraftValue * 100))
		             .replace("%mcmmo%", String.valueOf(mcmmoValue * 100))
		             .replace("%jobs_xp%", String.valueOf(jobsXpValue * 100))
		             .replace("%jobs_money%", String.valueOf(jobsMoneyValue * 100))
		             .replace("%duration%", String.valueOf(durationValue));
		if (bar != null && !bar.getPlayers()
		                       .isEmpty()) {
			Player player = bar.getPlayers()
			                   .get(0);
			if (player != null) {
				title = PlaceholderAPI.setPlaceholders(player, title);
			}
		}
		return title;
	}

	private void appendTitleBuilderIfNecessary(StringBuilder titleBuilder, String subSection, int value) {
		if (value > 1 || Objects.equals(subSection, DURATION)) {
			if (!titleBuilder.isEmpty() && !Objects.equals(subSection, DURATION)) {
				titleBuilder.append(main.getConfig()
				                        .getString("bar-pattern.separator"));
			}
			titleBuilder.append(main.getConfig()
			                        .getString("bar-pattern." + subSection));
		}
	}

	public BossBar getBar() {
		return bar;
	}

	public void addPlayer(@NotNull Player player) {
		bar.addPlayer(player);
	}
}
