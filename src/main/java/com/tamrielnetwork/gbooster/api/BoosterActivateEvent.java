package com.tamrielnetwork.gbooster.api;

import com.tamrielnetwork.gbooster.boosters.Booster;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BoosterActivateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Booster booster;
    private final Player player;
    private boolean isCancelled;

    public BoosterActivateEvent(Booster booster, Player player) {
        this.booster = booster;
        this.player = player;
        this.isCancelled = false;
    }

    public Booster getBooster() {
        return this.booster;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}