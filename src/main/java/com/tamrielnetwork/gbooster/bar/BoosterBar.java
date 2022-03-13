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
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BoosterBar {

	private static final String DURATION = "duration";
	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
	private final BossBar bar;

	public BoosterBar() {
		this.bar = Bukkit.createBossBar(getTitle(), BarColor.BLUE, BarStyle.SOLID);
		doTimer();
	}

	private void doTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if (main.getActiveBoostersManager()
				        .getActiveBoosters()
				        .isEmpty()) {
					bar.setVisible(false);
					return;
				}
				String title = getTitle();
				if (title.isEmpty()) {
					bar.setVisible(false);
					return;
				}
				double time = main.getActiveBoostersManager()
				                  .getMostOldBoosterInMinutes() / 60.0;
				if (time > 1.0) {
					time = 1;
				}
				bar.setProgress(time);
				bar.setVisible(true);
				bar.setTitle(title);
			}
		}.runTaskTimerAsynchronously(main, 0, 20 * 10L);
	}

	private String getTitle() {
		String title = Chat.replaceColors(Objects.requireNonNull(getTitleForActiveBoosters()));
		if (PlaceholderAPI.containsPlaceholders(title) && bar != null && !bar.getPlayers()
		                                                                     .isEmpty()) {
			Player player = bar.getPlayers()
			                   .get(0);
			if (player != null) {
				title = PlaceholderAPI.setPlaceholders(player, title);
			}
		}
		return title;
	}

	private void appendTitleBuilderIfNecessary(StringBuilder titleBuilder, String subSection, float value) {
		if (value > 1 || Objects.equals(subSection, DURATION)) {
			if (!titleBuilder.isEmpty() && !Objects.equals(subSection, DURATION)) {
				titleBuilder.append(main.getConfig()
				                        .getString("bar-pattern.separator"));
			}
			titleBuilder.append(main.getConfig()
			                        .getString("bar-pattern." + subSection));
		}
	}

	private String getTitleForActiveBoosters() {
		short durationValue = (short) main.getActiveBoostersManager()
		                                  .getMostOldBoosterInMinutes();
		if (durationValue == 0) {
			return "";
		}
		StringBuilder titleBuilder = new StringBuilder();
		byte minecraftValue = (byte) main.getActiveBoostersManager()
		                                 .getBoosterMultiplier(BoosterType.MINECRAFT, false);
		float mcmmoValue = (float) main.getActiveBoostersManager()
		                               .getBoosterMultiplier(BoosterType.MCMMO, false);
		float jobsXpValue = (float) main.getActiveBoostersManager()
		                                .getBoosterMultiplier(BoosterType.JOBS_XP, true);
		float jobsMoneyValue = (float) main.getActiveBoostersManager()
		                                   .getBoosterMultiplier(BoosterType.JOBS_MONEY, true);
		appendTitleBuilderIfNecessary(titleBuilder, "minecraft", minecraftValue);
		appendTitleBuilderIfNecessary(titleBuilder, "mcmmo", mcmmoValue);
		appendTitleBuilderIfNecessary(titleBuilder, "jobs-xp", jobsXpValue);
		appendTitleBuilderIfNecessary(titleBuilder, "jobs-money", jobsMoneyValue);
		appendTitleBuilderIfNecessary(titleBuilder, DURATION, durationValue);
		return titleBuilder.toString()
		                   .replace("%minecraft%", String.valueOf((short) (minecraftValue * 100)))
		                   .replace("%mcmmo%", String.valueOf((short) (mcmmoValue * 100)))
		                   .replace("%jobs_xp%", String.valueOf((short) (jobsXpValue * 100)))
		                   .replace("%jobs_money%", String.valueOf((short) (jobsMoneyValue * 100)))
		                   .replace("%duration%", String.valueOf(durationValue));
	}

	public BossBar getBar() {
		return bar;
	}

	public void addPlayer(@NotNull Player player) {
		bar.addPlayer(player);
	}
}
