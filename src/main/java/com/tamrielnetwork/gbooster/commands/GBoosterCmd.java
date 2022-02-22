/*
 * GBooster is a Spigot Plugin providing Global Boosters for Jobs McMMO and Minecraft.
 * Copyright © 2022 Leopold Meinel
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
 * along with this program. If not, see https://github.com/TamrielNetwork/GBooster/blob/main/LICENSE
 */

package com.tamrielnetwork.gbooster.commands;

import com.google.common.collect.ImmutableMap;
import com.tamrielnetwork.gbooster.GBooster;
import com.tamrielnetwork.gbooster.boosters.Booster;
import com.tamrielnetwork.gbooster.player.BoosterPlayer;
import com.tamrielnetwork.gbooster.utils.Chat;
import com.tamrielnetwork.gbooster.utils.commands.Cmd;
import com.tamrielnetwork.gbooster.utils.commands.CmdSpec;
import org.apache.commons.lang.StringUtils;
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
import java.util.Objects;

public class GBoosterCmd implements TabExecutor {

	private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (Cmd.isArgsLengthEqualTo(sender, args, 0) || Cmd.isArgsLengthGreaterThan(sender, args, 4)) {
			return true;
		}

		switch (args[0]) {
			case "give" -> doGive(sender, args);
			case "use" -> doUse(sender, args);
			case "time" -> doTime(sender, args);
			default -> Chat.sendMessage(sender, "cmd");
		}
		return true;
	}

	private void doGive(@NotNull CommandSender sender, @NotNull String[] args) {

		Booster booster = main.getBoostersManager().getBoosterById(args[2]);
		BoosterPlayer boosterPlayer = main.getPlayerStorage().getBoosterPlayerByName(args[1]);

		if (CmdSpec.isInvalidCmd(sender, boosterPlayer, "gbooster.give")) {
			return;
		}
		// Check permissions
		if (!sender.hasPermission("gbooster.give")) {
			Chat.sendMessage(sender, "no-perms");
			return;
		}

		// Check args length
		if (args.length != 4) {
			Chat.sendMessage(sender, "invalid-id");
			return;
		}

		// Get booster by id
		Booster booster = main.getBoostersManager().getBoosterById(args[2]);

		// Check if the given booster id is valid
		if (booster == null) {
			Chat.sendMessage(sender, "invalid-id");
			return;
		}

		// Get booster player by name
		BoosterPlayer boosterPlayer = main.getPlayerStorage().getBoosterPlayerByName(args[1]);

		// Check if the given player is registered
		if (boosterPlayer == null) {
			Chat.sendMessage(sender, "invalid-player");
			return;
		}

		// Check if the 4th arg is a number
		if (!StringUtils.isNumeric(args[3])) {
			Chat.sendMessage(sender, "invalid-amount");
			return;
		}

		int amount = Integer.parseInt(args[3]);

		// Check if the amount is greater than 0
		if (amount <= 0) {
			Chat.sendMessage(sender, "invalid-amount");
			return;
		}

		boosterPlayer.addBooster(booster.getId(), amount);

		// Send messages
		Player onlineBoosterPlayer = Bukkit.getPlayer(boosterPlayer.getUuid());

		if (onlineBoosterPlayer != null) {
			Chat.sendMessage(onlineBoosterPlayer, ImmutableMap.of(
							"%amount%", String.valueOf(amount),
							"%booster%", booster.getId()),
					"receive-boosters");
		}

		Chat.sendMessage(sender, ImmutableMap.of(
						"%amount%", String.valueOf(amount),
						"%booster%", booster.getId(),
						"%player%", boosterPlayer.getName()),
				"give-boosters");
	}

	private void doUse(@NotNull CommandSender sender, @NotNull String[] args) {

		// Check if command sender is a player
		if (!(sender instanceof Player)) {
			Chat.sendMessage(sender, "player-only");
			return;
		}

		BoosterPlayer boosterPlayer = main.getPlayerStorage().getBoosterPlayerByUUID(((Player) sender).getUniqueId());

		// Check args length
		if (args.length != 2) {
			Chat.sendMessage(sender, "no-args");
			return;
		}

		// Check permissions
		if (!sender.hasPermission("gbooster.use")) {
			Chat.sendMessage(sender, "no-perms");
			return;
		}

		// Get booster by id
		Booster booster = main.getBoostersManager().getBoosterById(args[1]);

		// Check if the given booster id is valid
		if (booster == null) {
			Chat.sendMessage(sender, "invalid-id");
			return;
		}

		// Check if there is an active countdown
		if (!boosterPlayer.canUseBooster(booster)) {
			Chat.sendMessage(sender, "countdown-active");
			return;
		}


		// Check if the max limit is reached
		if (!main.getActiveBoostersManager().canUseBooster(booster)) {
			Chat.sendMessage(sender, "max-limit");
			return;
		}

		// Try to remove a booster from a player
		if (!boosterPlayer.takeBooster(booster)) {
			Chat.sendMessage(sender, "no-booster");
			return;
		}
		main.getActiveBoostersManager().activateBooster(booster);
		Chat.sendMessage(sender, "active-booster");
		Chat.sendBroadcast(ImmutableMap.of("%player%", boosterPlayer.getName()), "active-booster-broadcast");
	}

	private void doTime(@NotNull CommandSender sender, @NotNull String[] args) {
		if (!sender.hasPermission("gbooster.time")) {
			Chat.sendMessage(sender, "no-perms");
			return;
		}
		// Check args length
		if (args.length != 1) {
			Chat.sendMessage(sender, "invalid-id");
			return;
		}
		// Check if there are no active boosters
		if (main.getActiveBoostersManager().getActiveBoosters().size() == 0) {
			Chat.sendMessage(sender, "no-active-booster");
		} else {
			Chat.sendMessage(sender, ImmutableMap.of("%duration%", String.valueOf(main.getActiveBoostersManager().getMostOldBoosterInMinutes())), "booster-timer");
		}
	}

	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		@Nullable List<String> tabComplete = new ArrayList<>();
		if (args.length == 1) {
			if (sender.hasPermission("gbooster.give")) {
				tabComplete.add("give");
			}
			if (sender.hasPermission("gbooster.use")) {
				tabComplete.add("use");
			}
			if (sender.hasPermission("gbooster.time")) {
				tabComplete.add("time");
			}
		}
		if (args.length > 1) {
			List<String> keys = new ArrayList<>(Objects.requireNonNull(main.getConfig().getConfigurationSection("boosters")).getKeys(false));
			switch (args[0]) {
				case "give":
					if (sender.hasPermission("gbooster.give")) {
						switch (args.length) {
							case 3 -> tabComplete.addAll(keys);
							case 4 -> tabComplete.addAll(List.of(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}));
							default -> tabComplete = null;
						}
					}
					break;
				case "use":
					if (sender.hasPermission("gbooster.use") && args.length == 2) {
						tabComplete.addAll(keys);
					}
					break;
				default:
					tabComplete = null;
					break;
			}
		}
		return tabComplete;
	}
}