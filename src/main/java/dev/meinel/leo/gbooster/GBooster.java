/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel & contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/LeoMeinel/GBooster/blob/main/LICENSE
 */

package dev.meinel.leo.gbooster;

import dev.meinel.leo.gbooster.bar.BoosterBar;
import dev.meinel.leo.gbooster.commands.GBoosterCmd;
import dev.meinel.leo.gbooster.files.Messages;
import dev.meinel.leo.gbooster.listeners.JobsExpGain;
import dev.meinel.leo.gbooster.listeners.JobsPayment;
import dev.meinel.leo.gbooster.listeners.JobsScheduleStart;
import dev.meinel.leo.gbooster.listeners.JobsScheduleStop;
import dev.meinel.leo.gbooster.listeners.McMMOPlayerXpGain;
import dev.meinel.leo.gbooster.listeners.PlayerExpChange;
import dev.meinel.leo.gbooster.listeners.PlayerJoin;
import dev.meinel.leo.gbooster.managers.BoostersManager;
import dev.meinel.leo.gbooster.papi.BoosterExpansion;
import dev.meinel.leo.gbooster.storage.BoosterStorageSql;
import dev.meinel.leo.gbooster.storage.BoosterStorageYaml;
import dev.meinel.leo.gbooster.storage.BoostersStorage;
import dev.meinel.leo.gbooster.storage.PlayerStorage;
import dev.meinel.leo.gbooster.storage.PlayerStorageSql;
import dev.meinel.leo.gbooster.storage.PlayerStorageYaml;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Objects;

public final class GBooster
		extends JavaPlugin {

	private BoostersManager boostersManager;
	private BoostersStorage boostersStorage;
	private PlayerStorage playerStorage;
	private BoosterBar boosterBar;
	private Messages messages;

	@Override
	public void onEnable() {
		generateConfig();
		registerListeners();
		Objects.requireNonNull(getCommand("gbooster"))
		       .setExecutor(new GBoosterCmd());
		Objects.requireNonNull(getCommand("gbooster"))
		       .setTabCompleter(new GBoosterCmd());
		setupStorage();
		boostersManager = new BoostersManager();
		boostersManager.loadBoosters();
		playerStorage.loadPlayers();
		boostersStorage.loadBoosters();
		boosterBar = new BoosterBar();
		messages = new Messages();
		new BoosterExpansion().register();
		startSaveTask();
		Bukkit.getLogger()
		      .info("GBooster v" + this.getDescription()
		                               .getVersion() + " enabled");
		Bukkit.getLogger()
		      .info("Copyright (C) 2022 Leopold Meinel");
		Bukkit.getLogger()
		      .info("This program comes with ABSOLUTELY NO WARRANTY!");
		Bukkit.getLogger()
		      .info("This is free software, and you are welcome to redistribute it under certain conditions.");
		Bukkit.getLogger()
		      .info("See https://github.com/LeoMeinel/GBooster/blob/main/LICENSE for more details.");
	}

	@Override
	public void onDisable() {
		playerStorage.savePlayers();
		boostersStorage.saveBoosters();
		Bukkit.getLogger()
		      .info("GBooster v" + this.getDescription()
		                               .getVersion() + " disabled");
	}

	private void registerListeners() {
		if (getServer().getPluginManager()
		               .getPlugin("Jobs") != null) {
			Bukkit.getLogger()
			      .info("Found Jobs! - Registering listeners!");
			getServer().getPluginManager()
			           .registerEvents(new JobsExpGain(), this);
			getServer().getPluginManager()
			           .registerEvents(new JobsPayment(), this);
			getServer().getPluginManager()
			           .registerEvents(new JobsScheduleStart(), this);
			getServer().getPluginManager()
			           .registerEvents(new JobsScheduleStop(), this);
		}
		else {
			Bukkit.getLogger()
			      .info("Could not find Jobs - Ignoring.");
		}
		if (getServer().getPluginManager()
		               .getPlugin("mcMMO") != null) {
			Bukkit.getLogger()
			      .info("Found McMMO! - Registering listener!");
			getServer().getPluginManager()
			           .registerEvents(new McMMOPlayerXpGain(), this);
		}
		else {
			Bukkit.getLogger()
			      .info("Could not find McMMO - Ignoring.");
		}
		getServer().getPluginManager()
		           .registerEvents(new PlayerExpChange(), this);
		getServer().getPluginManager()
		           .registerEvents(new PlayerJoin(), this);
	}

	private void generateConfig() {
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			saveDefaultConfig();
		}
	}

	private void setupStorage() {
		String storageSystem = getConfig().getString("storage-system");
		if (Objects.requireNonNull(storageSystem)
		           .equalsIgnoreCase("mysql")) {
			this.playerStorage = new PlayerStorageSql();
			this.boostersStorage = new BoosterStorageSql();
		}
		else {
			this.playerStorage = new PlayerStorageYaml();
			this.boostersStorage = new BoosterStorageYaml();
		}
	}

	public void startSaveTask() {
		new BukkitRunnable() {

			@Override
			public void run() {
				playerStorage.savePlayers();
			}
		}.runTaskTimerAsynchronously(this, getConfig().getInt("saving-time") * 20L,
		                             getConfig().getInt("saving-time") * 20L);
	}

	public BoostersManager getBoostersManager() {
		return boostersManager;
	}

	public BoostersStorage getActiveBoostersManager() {
		return boostersStorage;
	}

	public PlayerStorage getPlayerStorage() {
		return playerStorage;
	}

	public BoosterBar getBoosterBar() {
		return boosterBar;
	}

	public Messages getMessages() {
		return messages;
	}
}