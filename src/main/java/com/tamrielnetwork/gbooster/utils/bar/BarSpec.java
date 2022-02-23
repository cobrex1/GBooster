/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel
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

package com.tamrielnetwork.gbooster.utils.bar;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.utils.Chat;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BarSpec {

	private static final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	public static void doBar(@NotNull BossBar bar) {

		double time = main.getActiveBoostersManager().getMostOldBoosterInMinutes() / 60.0;

		if (time > 1.0)
			time = 1;

		bar.setProgress(time);

		bar.setVisible(true);
	}

	public static boolean noActiveBoosters(@NotNull BossBar bar) {

		if (main.getActiveBoostersManager().getActiveBoosters().size() == 0) {

			if (main.getConfig().getBoolean("empty-bar")) {
				bar.setTitle(Chat.replaceColors(Objects.requireNonNull(main.getConfig().getString("default-bar-message"))));
			} else {
				bar.setVisible(false);
			}

			return true;
		}
		return false;
	}

}
