package com.tamrielnetwork.gbooster.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.tamrielnetwork.gbooster.gbooster.GBooster;
import com.tamrielnetwork.gbooster.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsExpGain implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onExpGain(JobsExpGainEvent event){
        event.setExp((event.getExp()/main.getActiveBoostersManager().getJobsBooster())*main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true));
    }
}