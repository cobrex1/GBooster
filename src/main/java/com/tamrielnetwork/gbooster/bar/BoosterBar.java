package com.tamrielnetwork.gbooster.bar;

import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.BoosterType;
import com.tamrielnetwork.gbooster.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class BoosterBar {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    private final BossBar bar;

    public BoosterBar() {
        this.bar = Bukkit.createBossBar(getTitle(), BarColor.PURPLE, BarStyle.SOLID);

        startTask();
    }

    private void startTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

            //Check if there are no active boosters
            if (main.getActiveBoostersManager().getActiveBoosters().size() == 0) {

                if (main.getConfig().getBoolean("empty-bar")) {
                    bar.setTitle(Utils.replaceColors(main.getConfig().getString("default-bar-message")));
                } else {
                    bar.setVisible(false);
                }

                return;
            }

            double time = main.getActiveBoostersManager().getMostOldBoosterInMinutes() / 60.0;

            if (time > 1.0)
                time = 1;

            bar.setProgress(time);

            bar.setVisible(true);
            bar.setTitle(getTitle());

        }, 0, 20 * 5);
    }

    private String getTitle() {
        return Utils.replaceColors(Objects.requireNonNull(main.getConfig().getString("bar-pattern"))
                .replace("%minecraft%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MINECRAFT, false) - 1) * 100)))
                .replace("%mcmmo%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.MCMMO, false) - 1) * 100)))
                .replace("%jobs_xp%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_XP, true) - 1) * 100)))
                .replace("%jobs_money%", String.valueOf(Math.round((main.getActiveBoostersManager().getBoosterMultiplier(BoosterType.JOBS_MONEY, true) - 1) * 100)))
                .replace("%duration%", String.valueOf(main.getActiveBoostersManager().getMostOldBoosterInMinutes())));
    }

    public BossBar getBar() {
        return bar;
    }

    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }
}
