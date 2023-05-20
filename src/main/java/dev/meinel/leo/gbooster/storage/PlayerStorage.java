/*
 * File: PlayerStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.storage;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.player.BoosterPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PlayerStorage {

    protected final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    protected final List<BoosterPlayer> boosterPlayers = new ArrayList<>();

    public abstract void loadPlayers();

    public abstract void savePlayers();

    protected abstract void clear();

    public BoosterPlayer getBoosterPlayerByUUID(@NotNull UUID uuid) {
        return boosterPlayers.stream().filter(boosterPlayer -> boosterPlayer.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public BoosterPlayer getBoosterPlayerByName(@NotNull String name) {
        return boosterPlayers.stream()
                .filter(boosterPlayer -> boosterPlayer.getName().equalsIgnoreCase(name)).findFirst()
                .orElse(null);
    }

    public void registerPlayer(@NotNull UUID uuid, @NotNull String name) {
        boosterPlayers.add(new BoosterPlayer(uuid, name));
    }

    public boolean isPlayerRegistered(@NotNull UUID uuid) {
        return boosterPlayers.stream().anyMatch(player -> player.getUuid().equals(uuid));
    }
}
