/*
 * File: PlayerJoin.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import dev.meinel.leo.gbooster.GBooster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoin implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent event) {
        if (!main.getBoosterBar().getBar().getPlayers().contains(event.getPlayer())) {
            main.getBoosterBar().addPlayer(event.getPlayer());
        }
        if (!main.getPlayerStorage().isPlayerRegistered(event.getPlayer().getUniqueId())) {
            main.getPlayerStorage().registerPlayer(event.getPlayer().getUniqueId(),
                    event.getPlayer().getName());
        }
    }
}
