package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public final class SettingsHandler {

    public final String[] requiredRoles;
    public final Set<String> commands;

    public SettingsHandler(WildInspectPlugin plugin){
        WildInspectPlugin.log("Loading configuration started...");
        long startTime = System.currentTimeMillis();
        File file = new File(plugin.getDataFolder(), "config.yml");

        if(!file.exists())
            plugin.saveResource("config.yml", false);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        if(cfg.contains("factions.required-role"))
            requiredRoles = new String[] { cfg.getString("factions.required-role") };
        else if(cfg.contains("required-roles"))
            requiredRoles = cfg.getStringList("required-roles").toArray(new String[]{});
        else
            requiredRoles = new String[] {};

        commands = new HashSet<>();
        if(cfg.contains("command"))
            commands.add(cfg.getString("command"));
        if(cfg.contains("commands"))
            commands.addAll(cfg.getStringList("commands"));

        WildInspectPlugin.log(" - Found " + commands.size() + " commands in config.yml.");
        WildInspectPlugin.log("Loading configuration done (Took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public static void reload(){
        try{
            WildInspectPlugin plugin = WildInspectPlugin.getPlugin();
            Field settings = WildInspectPlugin.class.getDeclaredField("settingsHandler");
            settings.setAccessible(true);
            settings.set(plugin, new SettingsHandler(plugin));
        } catch(NoSuchFieldException | IllegalAccessException ex){
            ex.printStackTrace();
        }
    }

}
