/*
 * File: BoosterActivateEvent.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.api;

import dev.meinel.leo.gbooster.boosters.Booster;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class BoosterActivateEvent
		extends Event
		implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private final Booster booster;
	private final Player player;
	private boolean isCancelled;

	public BoosterActivateEvent(Booster booster, Player player) {
		this.booster = booster;
		this.player = player;
		this.isCancelled = false;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
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

	public @NotNull HandlerList getHandlers() {
		return getHandlerList();
	}
}