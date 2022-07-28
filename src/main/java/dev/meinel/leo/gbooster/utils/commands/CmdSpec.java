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

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String[] args, @NotNull String perm,
	                                   Booster booster, BoosterPlayer boosterPlayer) {
		return switch (args[0].toLowerCase()) {
			case "give" -> Cmd.isNotPermitted(sender, perm) || isInvalidBooster(sender, booster) || isInvalidNumber(
					sender, args[3]) || isInvalidBoosterPlayer(sender, boosterPlayer);
			case "use" -> Cmd.isNotPermitted(sender, perm) || isInvalidBooster(sender, booster) || exceedsLimit(sender,
			                                                                                                    booster)
			              || hasNoBooster(sender, boosterPlayer, booster) || isOnCountdown(sender, boosterPlayer,
			                                                                               booster);
			default -> true;
		};
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String[] args, @NotNull String perm,
	                                   int length) {
		return Cmd.isNotPermitted(sender, perm) || Cmd.isArgsLengthNotEqualTo(sender, args, length);
	}

	private static boolean isInvalidBoosterPlayer(@NotNull CommandSender sender, BoosterPlayer boosterPlayer) {
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

	private static boolean isOnCountdown(@NotNull CommandSender sender, @NotNull BoosterPlayer boosterPlayer,
	                                     @NotNull Booster booster) {
		if (!boosterPlayer.canUseBooster(booster)) {
			Chat.sendMessage(sender, "countdown-active");
			return true;
		}
		return false;
	}

	private static boolean exceedsLimit(@NotNull CommandSender sender, @NotNull Booster booster) {
		if (!main.getActiveBoostersManager()
		         .canUseBooster(booster)) {
			Chat.sendMessage(sender, "limit");
			return true;
		}
		return false;
	}

	private static boolean hasNoBooster(@NotNull CommandSender sender, @NotNull BoosterPlayer boosterPlayer,
	                                    @NotNull Booster booster) {
		if (!boosterPlayer.hasBooster(booster)) {
			Chat.sendMessage(sender, "no-booster");
			return true;
		}
		return false;
	}
}
