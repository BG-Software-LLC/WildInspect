package com.bgsoftware.wildinspect;

import com.bgsoftware.wildinspect.command.InspectCommand;
import com.bgsoftware.wildinspect.command.ReloadCommand;
import com.bgsoftware.wildinspect.listeners.PlayerListener;
import com.bgsoftware.wildinspect.metrics.Metrics;
import net.coreprotect.CoreProtect;
import com.bgsoftware.wildinspect.coreprotect.CoreProtectHook;
import com.bgsoftware.wildinspect.coreprotect.CoreProtectHook_API5;
import com.bgsoftware.wildinspect.coreprotect.CoreProtectHook_API6;
import com.bgsoftware.wildinspect.handlers.SettingsHandler;
import com.bgsoftware.wildinspect.handlers.HooksHandler;
import com.bgsoftware.wildinspect.listeners.BlockListener;

import org.bukkit.plugin.java.JavaPlugin;

public final class WildInspectPlugin extends JavaPlugin {

    private static WildInspectPlugin plugin;

    private SettingsHandler settingsHandler;
    private HooksHandler hooksHandler;

    private CoreProtectHook coreProtectHook;

    @Override
    public void onEnable() {
        plugin = this;
        new Metrics(this);

        log("******** ENABLE START ********");

        if(!getServer().getPluginManager().isPluginEnabled("CoreProtect")){
            log("Please install CoreProtect on your server.");
            setEnabled(false);
            return;
        }

        getServer().getPluginManager().registerEvents(new InspectCommand(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        getCommand("wildinspect").setExecutor(new ReloadCommand());
        getCommand("wildinspect").setTabCompleter(new ReloadCommand());

        settingsHandler = new SettingsHandler(this);
        hooksHandler = new HooksHandler(this);

        Locale.reload();
        loadCoreProtect();

        if(Updater.isOutdated()) {
            log("");
            log("A new version is available (v" + Updater.getLatestVersion() + ")!");
            log("Version's description: \"" + Updater.getVersionDescription() + "\"");
            log("");
        }

        log("******** ENABLE DONE ********");
    }

    private void loadCoreProtect(){
        int apiVersion = getPlugin(CoreProtect.class).getAPI().APIVersion();
        coreProtectHook = apiVersion == 5 ? new CoreProtectHook_API5() : new CoreProtectHook_API6();
    }

    public SettingsHandler getSettings() {
        return settingsHandler;
    }

    public HooksHandler getHooksHandler() {
        return hooksHandler;
    }

    public CoreProtectHook getCoreProtect() {
        return coreProtectHook;
    }

    public static void log(String message){
        plugin.getLogger().info(message);
    }

    public static WildInspectPlugin getPlugin(){
        return plugin;
    }

}
