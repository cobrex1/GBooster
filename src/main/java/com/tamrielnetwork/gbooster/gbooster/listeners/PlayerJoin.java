package com.tamrielnetwork.gbooster.gbooster.listeners;

import com.tamrielnetwork.gbooster.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoin implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!main.getBoosterBar().getBar().getPlayers().contains(event.getPlayer())) {
            main.getBoosterBar().addPlayer(event.getPlayer());
        }

        if (!main.getPlayerStorage().isPlayerRegistered(event.getPlayer().getUniqueId())) {
            main.getPlayerStorage().registerPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        }
    }
}