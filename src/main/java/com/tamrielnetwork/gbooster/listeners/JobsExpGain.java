package com.tamrielnetwork.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsExpGain implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onExpGain(JobsExpGainEvent event) {
		// Check if getJobsBooster returns >= 1 to prevent dividing by < 1
		if (main.getActiveBoostersManager().getJobsBooster() >= 1) {
			event.setExp((event.getExp() / main.getActiveBoostersManager().getJobsBooster()) * main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true));
		} else {
			event.setExp(event.getExp() * main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true));
		}
	}
}
