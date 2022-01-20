package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.common.config.CommentedConfiguration;
import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class SettingsHandler {

    public final String[] requiredRoles;
    public final Set<String> commands;
    public final int historyLimitDate;
    public final int historyLimitPage;
    public final long cooldown;
    public final boolean hideOps;
    public final String inspectPermission;

    public SettingsHandler(WildInspectPlugin plugin){
        WildInspectPlugin.log("Loading configuration started...");
        long startTime = System.currentTimeMillis();
        File file = new File(plugin.getDataFolder(), "config.yml");

        if(!file.exists())
            plugin.saveResource("config.yml", false);

        CommentedConfiguration cfg = CommentedConfiguration.loadConfiguration(file);
        convertOldFile(cfg);

        try {
            cfg.syncWithConfig(file, plugin.getResource("config.yml"));
        } catch (IOException error) {
            error.printStackTrace();
        }

        requiredRoles = cfg.getStringList("required-roles").toArray(new String[]{});
        commands = new HashSet<>(cfg.getStringList("commands"));
        historyLimitDate = cfg.getInt("history-limit.date", -1) == -1 ? Integer.MAX_VALUE : cfg.getInt("history-limit.date", -1);
        historyLimitPage = cfg.getInt("history-limit.page", -1) == -1 ? Integer.MAX_VALUE : cfg.getInt("history-limit.page", -1);
        cooldown = cfg.getLong("cooldown", 5000);
        hideOps = cfg.getBoolean("hide-ops", true);
        inspectPermission = cfg.getString("inspect-permission", "");

        WildInspectPlugin.log(" - Found " + commands.size() + " commands in config.yml.");
        WildInspectPlugin.log("Loading configuration done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public static void reload(){
        WildInspectPlugin plugin = WildInspectPlugin.getPlugin();
        plugin.setSettings(new SettingsHandler(plugin));
    }

    private static void convertOldFile(YamlConfiguration cfg) {
        if(cfg.contains("factions.required-role")) {
            //noinspection all
            cfg.set("required-roles", Arrays.asList(cfg.getString("factions.required-role")));
            cfg.set("factions", null);
        }
        if(cfg.contains("command")) {
            //noinspection all
            cfg.set("commands", Arrays.asList(cfg.getString("command")));
            cfg.set("command", null);
        }
    }

}
