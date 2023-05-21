/*
 * File: CmdSpec.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.utils.commands;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.Booster;
import dev.meinel.leo.gbooster.player.BoosterPlayer;
import dev.meinel.leo.gbooster.utils.Chat;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CmdSpec {

    private static final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    private CmdSpec() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String[] args,
            @NotNull String perm, Booster booster, BoosterPlayer boosterPlayer) {
        return switch (args[0].toLowerCase()) {
            case "give", "remove" -> !Cmd.isPermitted(sender, perm)
                    || isInvalidBooster(sender, booster) || isInvalidNumber(sender, args[3])
                    || isInvalidBoosterPlayer(sender, boosterPlayer);
            case "use" -> !Cmd.isPermitted(sender, perm) || isInvalidBooster(sender, booster)
                    || exceedsLimit(sender, booster)
                    || isOnCountdown(sender, boosterPlayer, booster);
            default -> true;
        };
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm,
            BoosterPlayer boosterPlayer) {
        return !Cmd.isPermitted(sender, perm) || isInvalidBoosterPlayer(sender, boosterPlayer);
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String[] args,
            @NotNull String perm, int length) {
        return !Cmd.isPermitted(sender, perm) || !Cmd.isArgsLengthEqualTo(sender, args, length);
    }

    private static boolean isInvalidBoosterPlayer(@NotNull CommandSender sender,
            BoosterPlayer boosterPlayer) {
        if (boosterPlayer == null) {
            Chat.sendMessage(sender, "invalid-player");
            return true;
        }
        return false;
    }

    private static boolean isInvalidBooster(@NotNull CommandSender sender, Booster booster) {
        if (booster == null) {
            Chat.sendMessage(sender, "invalid-booster");
            return true;
        }
        return false;
    }

    private static boolean isInvalidNumber(@NotNull CommandSender sender, @NotNull String arg) {
        if (!StringUtils.isNumeric(arg) || Integer.parseInt(arg) <= 0) {
            Chat.sendMessage(sender, "invalid-amount");
            return true;
        }
        return false;
    }

    private static boolean isOnCountdown(@NotNull CommandSender sender,
            @NotNull BoosterPlayer boosterPlayer, @NotNull Booster booster) {
        if (!boosterPlayer.canUseBooster(booster)) {
            Chat.sendMessage(sender, "countdown-active");
            return true;
        }
        return false;
    }

    private static boolean exceedsLimit(@NotNull CommandSender sender, @NotNull Booster booster) {
        if (!main.getActiveBoostersManager().canUseBooster(booster)) {
            Chat.sendMessage(sender, "limit");
            return true;
        }
        return false;
    }
}
