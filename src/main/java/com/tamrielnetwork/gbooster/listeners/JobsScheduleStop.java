package com.tamrielnetwork.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsScheduleStopEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.tamrielnetwork.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsScheduleStop implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onBoosterStop(JobsScheduleStopEvent event) {
        main.getActiveBoostersManager().addJobsBooster(-(event.getSchedule().getBoost(CurrencyType.EXP) + 1));
    }
}