/*
 * File: BoosterExpansion.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.gbooster.papi;

import dev.meinel.leo.gbooster.GBooster;
import dev.meinel.leo.gbooster.boosters.Booster;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BoosterExpansion
        extends PlaceholderExpansion {

    private final GBooster main = JavaPlugin.getPlugin(GBooster.class);

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return getAuthors();
    }

    @Override
    public @NotNull String getIdentifier() {
        return main.getPluginMeta().getName();
    }

    @Override
    public @NotNull String getVersion() {
        return main.getPluginMeta().getVersion();
    }

    @Override
    public String onRequest(@NotNull OfflinePlayer player, @NotNull String identifier) {
        for (Booster booster : main.getBoostersManager()
                .getBoosters()) {
            if (identifier.equals(booster.getId())) {
                return String.valueOf(main.getPlayerStorage()
                        .getBoosterPlayerByUUID(player.getUniqueId())
                        .getBoostersStorage()
                        .getOrDefault(identifier, 0));
            }
            if (identifier.equals(booster.getId() + "_multiplier")) {
                return String.valueOf((short) (booster.getMultiplier() * 100));
            }
            if (identifier.equals(booster.getId() + "_duration")) {
                return String.valueOf((short) (booster.getDuration() / 60));
            }
            if (identifier.equals("time")) {
                return String.valueOf(main.getActiveBoostersManager()
                        .getMostOldBoosterInMinutes());
            }
        }
        return "0";
    }

    private String getAuthors() {
        List<String> authors = main.getPluginMeta().getAuthors();

        StringBuilder authorBuilder = new StringBuilder();
        for (String author : authors) {
            if (author.equals(authors.get(0))) {
                authorBuilder.append(author);
                continue;
            }
            authorBuilder.append(", ")
                    .append(author);
        }
        return authorBuilder.toString();
    }
}
