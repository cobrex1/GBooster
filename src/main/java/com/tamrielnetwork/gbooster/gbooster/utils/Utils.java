package com.tamrielnetwork.gbooster.gbooster.utils;

import com.tamrielnetwork.gbooster.gbooster.GBooster;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Utils {

    private static final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    public static void sendMessage(CommandSender player, Map<String, String> placeholders, String message) {
        List<String> messages;
        if (main.getMessages().getMessagesConf().isList(message)) {
            messages = Objects.requireNonNull(main.getMessages().getMessagesConf().getStringList(message));
        } else {
            messages = new ArrayList<>();
            messages.add(main.getMessages().getMessagesConf().getString(message));
        }
        for (String string : messages) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                if (string.contains(entry.getKey())) {
                    string = string.replace(entry.getKey(), entry.getValue());
                }
            }

            player.sendMessage(replaceColors(string));
        }
    }

    public static void sendMessage(CommandSender player, String message) {
        player.sendMessage(replaceColors(Objects.requireNonNull(main.getMessages().getMessagesConf().getString(message))));
    }

    public static void sendBroadcast(Map<String, String> placeholders, String message) {
        List<String> messages;
        if (main.getMessages().getMessagesConf().isList(message)) {
            messages = Objects.requireNonNull(main.getMessages().getMessagesConf().getStringList(message));
        } else {
            messages = new ArrayList<>();
            messages.add(main.getMessages().getMessagesConf().getString(message));
        }
        for (String string : messages) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                if (string.contains(entry.getKey())) {
                    string = string.replace(entry.getKey(), entry.getValue());
                }
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(replaceColors(string));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    public static String replaceColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}