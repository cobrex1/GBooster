/*
 * File: GBoosterCmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.commands;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.api.BoosterActivateEvent;
import dev.meinel.leo.gbooster.boosters.Booster;
import dev.meinel.leo.gbooster.player.BoosterPlayer;
import dev.meinel.leo.gbooster.utils.Chat;
import dev.meinel.leo.gbooster.utils.commands.Cmd;
import dev.meinel.leo.gbooster.utils.commands.CmdSpec;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GBoosterCmd implements TabExecutor {

    private static final String GBOOSTER_GIVE = "gbooster.give";
    private static final String GBOOSTER_TAKE = "gbooster.take";
    private static final String GBOOSTER_USE = "gbooster.use";
    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (Cmd.isArgsLengthEqualTo(sender, args, 0)
                || Cmd.isArgsLengthGreaterThan(sender, args, 4)) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "give" -> doGive(sender, args);
            case "take" -> doTake(sender, args);
            case "use" -> doUse(sender, args);
            case "time" -> doTime(sender, args);
            default -> {
                Chat.sendMessage(sender, "cmd");
                return false;
            }
        }
        return true;
    }

    private void doGive(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Cmd.isArgsLengthNotEqualTo(sender, args, 4)) {
            return;
        }
        Booster booster = main.getBoostersManager().getBoosterById(args[2]);
        BoosterPlayer boosterPlayer = main.getPlayerStorage().getBoosterPlayerByName(args[1]);
        if (CmdSpec.isInvalidCmd(sender, args, GBOOSTER_GIVE, booster, boosterPlayer)) {
            return;
        }
        int amount = Integer.parseInt(args[3]);
        Player onlineBoosterPlayer = Bukkit.getPlayer(boosterPlayer.getUuid());
        boosterPlayer.addBooster(booster.getId(), amount);
        if (onlineBoosterPlayer != null) {
            Chat.sendMessage(onlineBoosterPlayer,
                    Map.of("%amount%", String.valueOf(amount), "%booster%", booster.getId()),
                    "receive-boosters");
        }
        Chat.sendMessage(sender, Map.of("%amount%", String.valueOf(amount), "%booster%",
                booster.getId(), "%player%", boosterPlayer.getName()), "give-boosters");
    }

    private void doTake(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Cmd.isArgsLengthNotEqualTo(sender, args, 4)) {
            return;
        }
        Booster booster = main.getBoostersManager().getBoosterById(args[2]);
        BoosterPlayer boosterPlayer = main.getPlayerStorage().getBoosterPlayerByName(args[1]);
        if (CmdSpec.isInvalidCmd(sender, args, GBOOSTER_TAKE, booster, boosterPlayer)) {
            return;
        }
        int amount = Integer.parseInt(args[3]);
        Player onlineBoosterPlayer = Bukkit.getPlayer(boosterPlayer.getUuid());
        if (!boosterPlayer.takeBooster(booster.getId(), amount)) {
            Chat.sendMessage(sender, "no-booster");
            return;
        }
        if (onlineBoosterPlayer != null) {
            Chat.sendMessage(onlineBoosterPlayer,
                    Map.of("%amount%", String.valueOf(amount), "%booster%", booster.getId()),
                    "lose-boosters");
        }
        Chat.sendMessage(sender, Map.of("%amount%", String.valueOf(amount), "%booster%",
                booster.getId(), "%player%", boosterPlayer.getName()), "take-boosters");
    }

    private void doUse(@NotNull CommandSender sender, @NotNull String[] args) {
        if (Cmd.isArgsLengthNotEqualTo(sender, args, 2) || Cmd.isInvalidSender(sender)) {
            return;
        }
        Player senderPlayer = (Player) sender;
        Booster booster = main.getBoostersManager().getBoosterById(args[1]);
        BoosterPlayer boosterPlayer =
                main.getPlayerStorage().getBoosterPlayerByUUID(senderPlayer.getUniqueId());
        if (CmdSpec.isInvalidCmd(sender, args, GBOOSTER_USE, booster, boosterPlayer)) {
            return;
        }
        if (!boosterPlayer.takeBooster(booster.getId(), 1)) {
            Chat.sendMessage(sender, "no-booster");
            return;
        }
        BoosterActivateEvent boosterActivateEvent = new BoosterActivateEvent(booster, senderPlayer);
        Bukkit.getPluginManager().callEvent(boosterActivateEvent);
        if (boosterActivateEvent.isCancelled()) {
            return;
        }
        main.getActiveBoostersManager().activateBooster(booster);
        Chat.sendMessage(sender, "active-booster");
        Chat.sendBroadcast(Map.of("%player%", boosterPlayer.getName()), "active-booster-broadcast");
    }

    private void doTime(@NotNull CommandSender sender, @NotNull String[] args) {
        if (CmdSpec.isInvalidCmd(sender, args, "gbooster.time", 1)) {
            return;
        }
        if (main.getActiveBoostersManager().getActiveBoosters().size() == 0) {
            Chat.sendMessage(sender, "no-active-booster");
            return;
        }
        Chat.sendMessage(sender,
                Map.of("%duration%",
                        String.valueOf(
                                main.getActiveBoostersManager().getMostOldBoosterInMinutes())),
                "booster-timer");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
            @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        @Nullable
        List<String> tabComplete = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission(GBOOSTER_GIVE)) {
                tabComplete.add("give");
            }
            if (sender.hasPermission(GBOOSTER_TAKE)) {
                tabComplete.add("take");
            }
            if (sender.hasPermission(GBOOSTER_USE)) {
                tabComplete.add("use");
            }
            if (sender.hasPermission("gbooster.time")) {
                tabComplete.add("time");
            }
            return tabComplete;
        }
        List<String> keys = new ArrayList<>(
                Objects.requireNonNull(main.getConfig().getConfigurationSection("boosters"))
                        .getKeys(false));
        if (args.length == 2 && args[0].equalsIgnoreCase("use")
                && sender.hasPermission(GBOOSTER_USE)) {
            tabComplete.addAll(keys);
            return tabComplete;
        }
        if (args.length == 3
                && (args[0].equalsIgnoreCase("give") && sender.hasPermission(GBOOSTER_GIVE))
                || (args[0].equalsIgnoreCase("take") && sender.hasPermission(GBOOSTER_TAKE))) {
            return keys;
        }
        return null;
    }
}
