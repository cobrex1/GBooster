/*
 * File: McMMOPlayerXpGain.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class McMMOPlayerXpGain
		implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onMcMMOPlayerXpGain(@NotNull McMMOPlayerXpGainEvent event) {
		event.setRawXpGained((event.getRawXpGained() * (float) main.getActiveBoostersManager()
				.getBoosterMultiplier(BoosterType.MCMMO, false)));
	}
}