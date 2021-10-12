package com.tamrielnetwork.gbooster.gbooster.storage;

import com.tamrielnetwork.gbooster.gbooster.GBooster;
import com.tamrielnetwork.gbooster.gbooster.player.BoosterPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class PlayerStorage {

    protected final GBooster main = JavaPlugin.getPlugin(GBooster.class);
    protected final List<BoosterPlayer> boosterPlayers = new ArrayList<>();

    public abstract void loadPlayers();

    public abstract void savePlayers();

    protected abstract void clear();

    public BoosterPlayer getBoosterPlayerByUUID(UUID uuid) {
        return boosterPlayers.stream()
                .filter(boosterPlayer -> boosterPlayer.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }

    public BoosterPlayer getBoosterPlayerByName(String name) {
        return boosterPlayers.stream()
                .filter(boosterPlayer -> boosterPlayer.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public void registerPlayer(UUID uuid, String name) {
        boosterPlayers.add(new BoosterPlayer(uuid, name));
    }

    public boolean isPlayerRegistered(UUID uuid) {
        return boosterPlayers.stream()
                .anyMatch(player -> player.getUuid().equals(uuid));
    }
}