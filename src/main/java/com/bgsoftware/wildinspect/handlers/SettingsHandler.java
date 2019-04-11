package com.bgsoftware.wildinspect.handlers;

import com.bgsoftware.wildinspect.WildInspectPlugin;
import com.bgsoftware.wildinspect.config.CommentedConfiguration;
import com.bgsoftware.wildinspect.config.ConfigComments;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
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

        CommentedConfiguration cfg = new CommentedConfiguration(ConfigComments.class);
        cfg.load(file);

        if(cfg.contains("factions.required-role")) {
            //noinspection all
            cfg.set("required-roles", Arrays.asList(cfg.getString("factions.required-role")));
            cfg.set("factions", null);
            cfg.save(file);
        }
        if(cfg.contains("command")) {
            //noinspection all
            cfg.set("commands", Arrays.asList(cfg.getString("command")));
            cfg.set("command", null);
            cfg.save(file);
        }

        cfg.resetYamlFile(plugin, "config.yml");

        requiredRoles = cfg.getStringList("required-roles").toArray(new String[]{});
        commands = new HashSet<>(cfg.getStringList("commands"));

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
