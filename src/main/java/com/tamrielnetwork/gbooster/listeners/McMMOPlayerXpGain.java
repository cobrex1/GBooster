/*
 Copyright (C) 2022  Leopold Meinel
 Visit https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE for more details.
 */
package com.tamrielnetwork.gbooster.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class McMMOPlayerXpGain implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onMcMMOPlayerXpGain(McMMOPlayerXpGainEvent event) {
		event.setRawXpGained((event.getRawXpGained() * (float) main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MCMMO, false)));
	}

}