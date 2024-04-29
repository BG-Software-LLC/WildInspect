package com.bgsoftware.wildinspect;

import com.bgsoftware.common.updater.Updater;
import com.bgsoftware.wildinspect.command.InspectCommand;
import com.bgsoftware.wildinspect.command.ReloadCommand;
import com.bgsoftware.wildinspect.coreprotect.CoreProtect;
import com.bgsoftware.wildinspect.handlers.HooksHandler;
import com.bgsoftware.wildinspect.handlers.SettingsHandler;
import com.bgsoftware.wildinspect.listeners.BlockListener;
import com.bgsoftware.wildinspect.listeners.PlayerListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class WildInspectPlugin extends JavaPlugin {

    private final Updater updater = new Updater(this, "wildinspect");

    private static WildInspectPlugin plugin;

    private SettingsHandler settingsHandler;
    private HooksHandler hooksHandler;

    private CoreProtect coreProtect;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this, 4104);

        Bukkit.getScheduler().runTask(this, () -> {
            log("******** ENABLE START ********");

            getServer().getPluginManager().registerEvents(new InspectCommand(this), this);
            getServer().getPluginManager().registerEvents(new BlockListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

            getCommand("wildinspect").setExecutor(new ReloadCommand());
            getCommand("wildinspect").setTabCompleter(new ReloadCommand());

            settingsHandler = new SettingsHandler(this);
            hooksHandler = new HooksHandler(this);
            coreProtect = new CoreProtect(this);

            Locale.reload();

            if (updater.isOutdated()) {
                log("");
                log("A new version is available (v" + updater.getLatestVersion() + ")!");
                log("Version's description: \"" + updater.getVersionDescription() + "\"");
                log("");
            }

            log("******** ENABLE DONE ********");
        });
    }

    public SettingsHandler getSettings() {
        return settingsHandler;
    }

    public void setSettings(SettingsHandler settingsHandler) {
        this.settingsHandler = settingsHandler;
    }

    public HooksHandler getHooksHandler() {
        return hooksHandler;
    }

    public CoreProtect getCoreProtect() {
        return coreProtect;
    }

    public Updater getUpdater() {
        return updater;
    }

    public static void log(String message) {
        plugin.getLogger().info(message);
    }

    public static WildInspectPlugin getPlugin() {
        return plugin;
    }

}
