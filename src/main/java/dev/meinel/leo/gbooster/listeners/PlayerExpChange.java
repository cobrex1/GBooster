/*
 * File: PlayerExpChange.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerExpChange
        implements Listener {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @EventHandler
    public void onPlayerExpChange(@NotNull PlayerExpChangeEvent event) {
        event.setAmount((event.getAmount() * (byte) main.getActiveBoostersManager()
                .getBoosterMultiplier(BoosterType.MINECRAFT, false)));
    }
}