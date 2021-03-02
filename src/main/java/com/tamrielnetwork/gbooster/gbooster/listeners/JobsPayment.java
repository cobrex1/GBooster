package com.tamrielnetwork.gbooster.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.tamrielnetwork.gbooster.gbooster.GBooster;
import com.tamrielnetwork.gbooster.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class JobsPayment implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onPayment(JobsPaymentEvent event){
        // Check if getJobsBooster returns >= 1 to prevent dividing by < 1
        if(main.getActiveBoostersManager().getJobsBooster() >= 1){
            event.set(CurrencyType.MONEY, (event.getPayment().get(CurrencyType.MONEY) / main.getActiveBoostersManager().getJobsBooster()) * main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_MONEY, true));
        }else{
            event.set(CurrencyType.MONEY, event.getPayment().get(CurrencyType.MONEY) * main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_MONEY, true));
        }
    }
}
