package com.tamrielnetwork.gbooster.gbooster;

import com.tamrielnetwork.gbooster.gbooster.bar.BoosterBar;
import com.tamrielnetwork.gbooster.gbooster.commands.GBoosterCmd;
import com.tamrielnetwork.gbooster.gbooster.files.Messages;
import com.tamrielnetwork.gbooster.gbooster.listeners.*;
import com.tamrielnetwork.gbooster.gbooster.storage.*;
import com.tamrielnetwork.gbooster.gbooster.managers.BoostersManager;
import com.tamrielnetwork.gbooster.gbooster.papi.BoosterExpansion;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Objects;

public final class GBooster extends JavaPlugin {

    private BoostersManager boostersManager;
    private BoostersStorage boostersStorage;
    private PlayerStorage playerStorage;
    private BoosterBar boosterBar;
    private Messages messages;

    @Override
    public void onEnable() {
        System.out.println("""
                [GBooster] GBooster Copyright (C) 2021 Leopold Meinel
                [GBooster] This program comes with ABSOLUTELY NO WARRANTY!
                [GBooster] This is free software, and you are welcome to redistribute it under certain conditions.
                [GBooster] Visit https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE for more details.""");

        generateConfig();

        registerListeners();

        Objects.requireNonNull(getCommand("gbooster")).setExecutor(new GBoosterCmd());

        setupStorage();
        boostersManager = new BoostersManager();
        boostersManager.loadBoosters();
        playerStorage.loadPlayers();
        boostersStorage.loadBoosters();

        boosterBar = new BoosterBar();
        messages = new Messages();

        new BoosterExpansion().register();

        startSaveTask();
    }

    @Override
    public void onDisable(){
        playerStorage.savePlayers();
        boostersStorage.saveBoosters();
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new JobsExpGain(), this);
        getServer().getPluginManager().registerEvents(new JobsPayment(), this);
        getServer().getPluginManager().registerEvents(new JobsScheduleStart(), this);
        getServer().getPluginManager().registerEvents(new JobsScheduleStop(), this);
        getServer().getPluginManager().registerEvents(new McMMOPlayerXpGain(), this);
        getServer().getPluginManager().registerEvents(new PlayerExpChange(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    private void generateConfig(){
        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            saveDefaultConfig();
        }
    }

    private void setupStorage(){
        String storageSystem = getConfig().getString("storage-system");

        if (Objects.requireNonNull(storageSystem).equalsIgnoreCase("mysql")){
            this.playerStorage = new PlayerStorageSql();
            this.boostersStorage = new BoosterStorageSql();
        } else {
            this.playerStorage = new PlayerStorageYaml();
            this.boostersStorage = new BoosterStorageYaml();
        }
    }

    public void startSaveTask(){
        new BukkitRunnable(){

            @Override
            public void run() {
                playerStorage.savePlayers();
            }
        }.runTaskTimer(this, getConfig().getInt("saving-time")* 20L, getConfig().getInt("saving-time")* 20L);
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