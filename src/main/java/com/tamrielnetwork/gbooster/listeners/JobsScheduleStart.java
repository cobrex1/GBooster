package com.tamrielnetwork.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsScheduleStartEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.tamrielnetwork.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsScheduleStart implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onBoosterStart(JobsScheduleStartEvent event) {
		main.getActiveBoostersManager().addJobsBooster(event.getSchedule().getBoost().get(CurrencyType.EXP) + 1);
	}
}