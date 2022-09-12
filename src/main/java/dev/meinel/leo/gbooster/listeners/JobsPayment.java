/*
 * File: JobsPayment.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.listeners;

import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class JobsPayment
		implements Listener {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@EventHandler
	public void onPayment(@NotNull JobsPaymentEvent event) {
		if (event.getPayment()
				.get(CurrencyType.MONEY) == null) {
			return;
		}
		if (main.getActiveBoostersManager()
				.getJobsBooster() >= 1) {
			event.set(CurrencyType.MONEY, (event.getPayment()
					.get(CurrencyType.MONEY)
					/ main.getActiveBoostersManager()
							.getJobsBooster())
					* (float) main.getActiveBoostersManager()
							.getBoosterMultiplier(BoosterType.JOBS_MONEY, true));
		} else {
			event.set(CurrencyType.MONEY, event.getPayment()
					.get(CurrencyType.MONEY)
					* (float) main.getActiveBoostersManager()
							.getBoosterMultiplier(
									BoosterType.JOBS_MONEY,
									true));
		}
	}
}
